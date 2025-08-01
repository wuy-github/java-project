package com.project.futabuslines.components;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final JwtTokenUtil jwtTokenUtil;
    public Long extractUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtTokenUtil.extractUserId(jwt);
        }
        return null;
    }

}
