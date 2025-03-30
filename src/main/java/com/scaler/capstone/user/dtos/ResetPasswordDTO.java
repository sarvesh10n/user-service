package com.scaler.capstone.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Reset Password Question cannot be blank")
    private String resetPasswordQuestion;

    @NotBlank(message = "Reset Password Answer cannot be blank")
    private String resetPasswordAnswer;

    @NotBlank(message = "New Password cannot be blank")
    private String newPassword;
}
