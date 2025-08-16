package com.project.futabuslines.filter;

import com.project.futabuslines.components.JwtTokenUtil;
import com.project.futabuslines.models.Token;
import com.project.futabuslines.models.User;
import com.project.futabuslines.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("Path: " + request.getServletPath());
        System.out.println("Method: " + request.getMethod());

        try {
            if(isByPassToken(request)){
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            //                 if - SecurityContextHolder.getContext().getAuthentication() == null)
            if(authHeader == null && !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            if(authHeader.startsWith("Bearer ")){
                final String token = authHeader.substring(7);
                Token storedToken = tokenRepository.findByToken(token).orElse(null);
                if (storedToken == null || storedToken.getRevoked()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or revoked");
                    return;
                }
                final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
                if(phoneNumber != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null){
                    User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                    if(jwtTokenUtil.validateToken(token, userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response); // enable bypass

        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isByPassToken( @NonNull HttpServletRequest request){
        final String path = request.getServletPath();
        final String method = request.getMethod().toUpperCase();
        // Bo qua khong can token
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/route/all-routes",apiPrefix), "GET"),
                Pair.of(String.format("%s/categories",apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register",apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login",apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login",apiPrefix), "POST"),
                Pair.of(String.format("%s/users/auth/reset-password",apiPrefix), "POST"),
                Pair.of(String.format("%s/otp/send",apiPrefix), "POST"),
                Pair.of(String.format("%s/otp/reset-password",apiPrefix), "POST"),
                Pair.of(String.format("%s/otp/verify",apiPrefix), "POST"),
                Pair.of(String.format("%s/orders/vnpay-return", apiPrefix), "GET"),
                Pair.of(String.format("%s/orders/vnpay/notify", apiPrefix), "POST"),
                Pair.of(String.format("%s/feedbacks", apiPrefix), "POST"),
                Pair.of(String.format("%s/orders/momo/callback", apiPrefix), "GET"),
                Pair.of(String.format("%s/watch/watches", apiPrefix), "GET"),
                Pair.of("/uploads/", "GET")


        );
//        System.out.println("apiPrefix = " + apiPrefix);
//        System.out.println("Path = " + request.getServletPath());
//        System.out.println(path + " - " + method);

        for (Pair<String, String> bypass : bypassTokens) {
            if (path.equals(bypass.getFirst()) && method.equals(bypass.getSecond())) {
                return true;
            }
        }

        for (Pair<String, String> bypassToken: bypassTokens){
            if (request.getServletPath().equals(bypassToken.getFirst())
                    && request.getMethod().equalsIgnoreCase(bypassToken.getSecond())) {
                return true;
            }
            if (request.getServletPath().contains(bypassToken.getFirst())
                    && request.getMethod().equalsIgnoreCase(bypassToken.getSecond())) {
                return true;
            }
            if (request.getServletPath().startsWith(bypassToken.getFirst())
                    && request.getMethod().equalsIgnoreCase(bypassToken.getSecond())) {
                return true;
            }
            if (request.getServletPath().equalsIgnoreCase(bypassToken.getFirst()) &&
                    request.getMethod().equalsIgnoreCase(bypassToken.getSecond())) {
                return true;
            }

        }
        return false;
    }
}
