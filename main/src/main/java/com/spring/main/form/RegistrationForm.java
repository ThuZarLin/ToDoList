package com.spring.main.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {
    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "email\' format is wrong")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should have min 8 characters")
    private String password;

    // @NotEmpty(message = "Profile Path is required")
    // private String profilePath;
}
