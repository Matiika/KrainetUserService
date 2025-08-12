package com.Krainet.UserService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    @Schema(example = "Updated_Username")
    private String username;

    @Schema(example = "user123")
    private String password;

    @Schema(example = "updated@email.com")
    private String email;

    @Schema(example = "UpdatedFirstName")
    private String firstName;

    @Schema(example = "UpdatedLastName")
    private String lastName;

    @Schema(example = "USER")
    private String role;

}
