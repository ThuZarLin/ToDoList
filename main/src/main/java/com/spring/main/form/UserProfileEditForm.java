package com.spring.main.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEditForm {

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    
    private MultipartFile profilePath;
}
