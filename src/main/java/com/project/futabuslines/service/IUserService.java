package com.project.futabuslines.service;

import com.project.futabuslines.dtos.*;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.User;
import com.project.futabuslines.models.UserImage;
import com.project.futabuslines.responses.LoginResponseDTO;
import com.project.futabuslines.responses.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    LoginResponseDTO login(UserLoginDTO loginDTO) throws Exception;

    User updateUser(Long id, UserUploadDTO userDTO) throws DataNotFoundException;

    void deleUser(Long id);

    List<User> getAllUser();

    UserResponse findById(Long id);
    void resetPassword(ResetPasswordDTO dto) throws Exception;


    UserImage uploadUserImage(Long userId, UserImageDTO userImageDTO) throws Exception;
    User getUserById(long userId) throws Exception;
    List<User> getAllUsers();
    long countNewUsersLast7Days();
    long countNewUsersToday();
}
