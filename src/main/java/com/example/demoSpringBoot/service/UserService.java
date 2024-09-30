package com.example.demoSpringBoot.service;

import com.example.demoSpringBoot.dto.request.UserCreationRequest;
import com.example.demoSpringBoot.dto.request.UserUpdateRequest;
import com.example.demoSpringBoot.dto.response.UserResponse;
import com.example.demoSpringBoot.entity.User;
import com.example.demoSpringBoot.exceptions.AppException;
import com.example.demoSpringBoot.exceptions.ErrorCode;
import com.example.demoSpringBoot.mapper.UserMapper;
import com.example.demoSpringBoot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // No need @Autowire. Check in target\...\service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Make fields private final
public class UserService {
    //    @Autowired
    UserRepository userRepository;
    //    @Autowired
    UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        /*
        // Example using annotation @Builder in UserCreationRequest DTO
        UserCreationRequest u = UserCreationRequest.builder()
                .username("joss")
                .firstName("joss")
                .build();

         */
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
//            throw new RuntimeException("test exception"); //return code OTHER - 9999
        }

        User user = userMapper.toUser(request);
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserResponse getUserByID(String userID) {
        return userMapper.toUserResponse(userRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }
}
