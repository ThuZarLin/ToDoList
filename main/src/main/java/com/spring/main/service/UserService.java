package com.spring.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.main.enitity.Token;
import com.spring.main.enitity.User;
import com.spring.main.form.AuthenticationResponse;
import com.spring.main.form.ResetPasswordForm;
import com.spring.main.form.UserNameEditForm;
import com.spring.main.form.UserProfileEditForm;
import com.spring.main.repository.TokenRepository;
import com.spring.main.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FileUploadService fileUploadService;

    public void saveUserToken(String jwtToken, User user){
        Token token = Token.builder()
            .token(jwtToken)
            .isLoggedOut(false)
            .user(user)
            .build();
        
        tokenRepository.save(token);
    }
   
    public AuthenticationResponse reset(ResetPasswordForm request){

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        // if(!user.getRandomCode().equals(request.getRandomCode())){
        //     throw new IllegalArgumentException("Invalid random code");
        // }
        if(!user.getRandomCode().equals(request.getRandomCode()) && !request.getPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException("Invalid random code or Password doesn't match");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        saveUserToken(jwtToken, user);

        emailSender.sendEmail(request.getEmail(), "<html>" +
                        "<body>" +
                        "<h2>Dear "+ user.getFirstName() + " " + user.getLastName() + ",</h2>"
                        + "<br/> Your password reset successfully. " +
                        "</body>" +
                        "</html>");

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();

        
    }

    public String editUserName(@Valid UserNameEditForm request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userRepository.save(user);
        return "Update successfully";
    }

    public String editUserProfile(@Valid UserProfileEditForm request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        String filePath = fileUploadService.uploadImage(request.getProfilePath(), "user");
        user.setProfilePath(filePath);

        userRepository.save(user);
        return "Profile path updated successfully";
    }
}
