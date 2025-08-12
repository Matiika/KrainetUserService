package com.Krainet.UserService.JWT;

import com.Krainet.UserService.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private UserDTO user;

    public JwtResponse() {}

    public JwtResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

}
