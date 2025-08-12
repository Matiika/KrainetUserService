package com.Krainet.UserService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @Schema(example = "newuser")
    private String username;

    @Schema(example = "user123")
    private String password;

    @Schema(example = "newuser@krainet.com")
    private String email;

    @Schema(example = "Alex")
    private String firstName;

    @Schema(example = "Smirnov")
    private String lastName;

}
