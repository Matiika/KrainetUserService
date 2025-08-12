package com.Krainet.UserService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

}
