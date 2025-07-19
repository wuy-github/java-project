package com.project.futabuslines.service;

import com.project.futabuslines.components.JwtTokenUtil;
import com.project.futabuslines.dtos.*;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.PermissionDenyException;
import com.project.futabuslines.models.Role;
import com.project.futabuslines.models.Token;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.UserImage;
import com.project.futabuslines.repositories.RoleRepository;
import com.project.futabuslines.repositories.TokenRepository;
import com.project.futabuslines.repositories.UserImageRepository;
import com.project.futabuslines.repositories.UserRepository;
import com.project.futabuslines.responses.LoginResponseDTO;
import com.project.futabuslines.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserImageRepository userImageRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        String email = userDTO.getEmail();
        // Kiem tra xem so dien thoai da ton tai chua
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if(userRepository.existsByEmail(email)){
            throw new DataNotFoundException("Email đã tồn tại");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cannot register an admin account");
        }
        // convert userDTO -> user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();


        newUser.setRole(role);
        newUser.setIsActive(true);
        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    @Override
    public LoginResponseDTO login(UserLoginDTO loginDTO) throws Exception {
        String identifier = null;

        if (loginDTO.getEmail() != null && !loginDTO.getEmail().isBlank()) {
            identifier = loginDTO.getEmail();
        } else if (loginDTO.getPhoneNumber() != null && !loginDTO.getPhoneNumber().isBlank()) {
            identifier = loginDTO.getPhoneNumber();
        }

        if (identifier == null) {
            throw new IllegalArgumentException("Vui lòng nhập email hoặc số điện thoại.");
        }

        Optional<User> optionalUser;

        if (identifier.contains("@")) {
            optionalUser = userRepository.findByEmail(identifier);
        } else {
            optionalUser = userRepository.findByPhoneNumber(identifier);
        }

        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Email/Số điện thoại hoặc mật khẩu không đúng.");
        }

//        return optionalUser.get();
        // Muon tra ve JWT - token
        User existingUser = optionalUser.get();
        if (!existingUser.getIsActive()) {
            throw new BadCredentialsException("Your account is deactivated. Please contact support.");
        }
        // check Password
        if(!passwordEncoder.matches(loginDTO.getPassword(), existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                identifier,
                loginDTO.getPassword(),
                existingUser.getAuthorities()
        );
        // Authenticate with Java Spring Security
        authenticationManager.authenticate(authenticationToken);
String token = jwtTokenUtil.generateToken(existingUser);
        // Trả về cả token + thông tin user
        // Save token
        Token newToken = new Token();
        newToken.setToken(token);
        newToken.setUser(existingUser);
        newToken.setExpired(false); // Token chưa hết hạn
        newToken.setRevoked(false); // Token chưa bị thu hồi
        newToken.setExpirationDate(
                LocalDateTime.ofInstant(
                        Instant.now().plusSeconds(jwtTokenUtil.getExpiration()),
                        ZoneId.systemDefault()
                )
        );tokenRepository.save(newToken);
        String imageUrl = userImageRepository
                .findByUserId(existingUser.getId())
                .stream()
                .findFirst()
                .map(UserImage::getImageUrl)
                .orElse(null);
        return LoginResponseDTO.builder()
                .token(token)
                .userId(existingUser.getId())
                .fullName(existingUser.getFullName())
                .roleId(existingUser.getRole().getId())
                .imageUrl(imageUrl)
                .build();
    }


    @Override
    public User updateUser(Long id, UserUploadDTO userDTO) throws DataNotFoundException {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + id));
        String email = userDTO.getEmail();
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        modelMapper.typeMap(UserUploadDTO.class, User.class)
                .addMappings(mapper -> {// Bỏ qua trường password
                });

        // Mapping các trường từ DTO sang entity
        modelMapper.map(userDTO, existingUser);

        // Lưu lại vào database
        userRepository.save(existingUser);

        // Trả về user sau khi đã cập nhật
        return existingUser;
    }


    @Override
    public void deleUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
//        if(user != null) {
//            user.setIsActive(false);
//            userRepository.save(user);
//        }
        if(user != null){
            userRepository.delete(user);
        }
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID người dùng không đúng hoặc không tồn tại."));
        return UserResponse.fromUser(user);
    }


    @Override
    public UserImage uploadUserImage(
            Long userId,
            UserImageDTO userImageDTO
    ) throws Exception {
        User existingUser = userRepository.findById(
                userId)
                .orElseThrow(()->new DataNotFoundException("Cannot find user with id: "+ userId));
        UserImage newUserImage = UserImage.builder()
                .user(existingUser)
                .imageUrl(userImageDTO.getImageUrl())
                .build();
//        int size = userImageRepository.findByUserId(userId).size();
//        if(size >= UserImage.MAXIMUM_IMAGES_PER_USER){
//            throw new InvalidParamException("Number of images must be <= " + UserImage.MAXIMUM_IMAGES_PER_USER);
//        }
        return userImageRepository.save(newUserImage);
    }

    @Override
    public User getUserById(long userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(()->new DataNotFoundException("Cannot find product with id: " + userId));
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) throws Exception {
        Optional<User> optionalUser;

        // Cho phép reset bằng email hoặc số điện thoại
        if (dto.getContact().contains("@")) {
            optionalUser = userRepository.findByEmail(dto.getContact());
        } else {
            optionalUser = userRepository.findByPhoneNumber(dto.getContact());
        }

        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy tài khoản.");
        }

        User user = optionalUser.get();
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public long countNewUsersToday() {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        return userRepository.countByCreatedAtAfter(startOfToday);
    }

    @Override
    public long countNewUsersLast7Days() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return userRepository.countByCreatedAtAfter(sevenDaysAgo);
    }



}

















