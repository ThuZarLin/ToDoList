package com.spring.main.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.main.enitity.User;
import com.spring.main.form.AuthenticationResponse;
import com.spring.main.form.ResetPasswordForm;
import com.spring.main.form.UserNameEditForm;
import com.spring.main.form.UserProfileEditForm;
import com.spring.main.repository.UserRepository;
import com.spring.main.service.EmailSender;
import com.spring.main.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserRepository userRepository;


    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    String randomCode = getRandomNumberString();

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("User not found"));
        emailSender.sendEmail(email, "<html>" +
                        "<body>" +
                        "OTP Code for reset password is "
                        + "<br/> "  + "<u>" + randomCode +"</u>" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>");

        user.setRandomCode(randomCode);
        userRepository.save(user);
        return ("Code sent successfully.");
        
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<AuthenticationResponse> resetPassword(@Valid @RequestBody ResetPasswordForm request) {
        
        return ResponseEntity.ok(userService.reset(request));
    }

    @PutMapping("/editUserName")
    public ResponseEntity<String> editUsername(@Valid @RequestBody UserNameEditForm request) {
        userService.editUserName(request);
        return ResponseEntity.ok("Update completed");
    }

    @PutMapping("/editUserProfile")
    public ResponseEntity<String> editUserProfile(@Valid @RequestBody UserProfileEditForm request) {
        userService.editUserProfile(request);
        return ResponseEntity.ok("Profile path updateed completed");
    }

}
