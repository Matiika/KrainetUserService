package com.Krainet.UserService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @Schema(example = "Username")
    private String username;

    @Schema(example = "user123")
    private String password;

    @Schema(example = "email@email.com")
    private String email;

    @Schema(example = "FirstName")
    private String firstName;

    @Schema(example = "LastName")
    private String lastName;

    @Schema(example = "USER")
    private String role;

}
