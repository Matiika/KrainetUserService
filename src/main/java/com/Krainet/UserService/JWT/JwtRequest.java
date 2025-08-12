package com.Krainet.UserService.JWT;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {

    @Schema(example = "user2")
    private String username;

    @Schema(example = "user123")
    private String password;

    public JwtRequest() {}

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
