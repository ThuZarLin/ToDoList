package com.spring.main.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationForm {
    @NotBlank(message = "email is required")
    @Email(message = "email\'s format is wrong")
    private String email;

    @NotBlank(message = "random code is required")
    private String randomCode;
}
