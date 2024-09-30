package com.example.demoSpringBoot.controller;

import com.example.demoSpringBoot.dto.request.ApiReponse;
import com.example.demoSpringBoot.dto.request.UserCreationRequest;
import com.example.demoSpringBoot.dto.request.UserUpdateRequest;
import com.example.demoSpringBoot.dto.response.UserResponse;
import com.example.demoSpringBoot.entity.User;
import com.example.demoSpringBoot.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor // No need @Autowire. Check in target\...\controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Make fields private final
public class UserController {
    //    @Autowired
    UserService userService;

    @PostMapping
    ApiReponse<User> createUser(@RequestBody @Valid UserCreationRequest request) { //@Valid: validate request via rules defined in UserCreationRequest
        ApiReponse<User> apiResp = new ApiReponse<>();
        apiResp.setCode(1000);
        apiResp.setResult(userService.createUser(request));
        return apiResp;
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userID}")
    UserResponse getUserByID(@PathVariable String userID) {
        return userService.getUserByID(userID);
    }

    @PutMapping("/{userID}")
    UserResponse updateUser(@PathVariable String userID, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userID, request);
    }

    @DeleteMapping("/{userID}")
    String deleteUser(@PathVariable String userID) {
        userService.deleteUser(userID);
        return "Deleted user whose id " + userID;
    }
}
