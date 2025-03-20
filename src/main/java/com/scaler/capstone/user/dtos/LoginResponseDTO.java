package com.scaler.capstone.user.dtos;

import com.scaler.capstone.user.models.Token;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginResponseDTO {
    private String token;

    public static LoginResponseDTO fromToken(Token token) {
        LoginResponseDTO ldto = new LoginResponseDTO();
        ldto.setToken(token.getTokenValue());
        return ldto;
    }
}
