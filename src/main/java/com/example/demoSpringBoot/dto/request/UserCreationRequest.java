package com.example.demoSpringBoot.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Lombok annotations
//@FieldDefaults(level = AccessLevel.PRIVATE) // Set private of fields level if not explicit defined
//@Getter // auto gen getters. Check in target\classes\...\dto\...
//@Setter // auto gen setters
@Data // equivalent to @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID") // Key of ErrorCode in String format
    private String username;

    @Size(min = 8, message = "PASSWORD_INVALID") // Key of ErrorCode in String format
    private String password;

    private String lastName;
    private String firstName;
    private LocalDate dob;

//    public LocalDate getDob() {
//        return dob;
//    }
//
//    public void setDob(LocalDate dob) {
//        this.dob = dob;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
}
