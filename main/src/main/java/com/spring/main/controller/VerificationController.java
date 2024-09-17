package com.spring.main.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.main.enitity.User;
import com.spring.main.form.VerificationForm;
import com.spring.main.repository.UserRepository;
import com.spring.main.service.EmailSender;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/")
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender; 

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    String randomCode = getRandomNumberString();

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@Valid @RequestBody VerificationForm request) {
        // String userEmail = jwtService.extractUsername(token); 

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        // System.out.print(user);

        if(user.getRandomCode().equalsIgnoreCase(request.getRandomCode()) ){
            user.setEmailVerifiedAt(new Date()); 
            userRepository.save(user);
            return ResponseEntity.ok(
                "Email verified successfully." + 
                "\n{\n\tFirstName: " + user.getFirstName() + 
                ",\n\tLastName: " + user.getLastName() + 
                ",\n\tEmail: " + user.getEmail() +
                ",\n\tVerified At: " + user.getEmailVerifiedAt() +
                "\n}");
        }else{
            return ResponseEntity.ok("Verification is not successfully " + request.getRandomCode() + request.getEmail());
        }
    }

    @GetMapping("/resend")
    public String resend(@RequestBody User request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalStateException("User Not Found"));

        emailSender.sendEmail(request.getEmail(), "<html>" +
                        "<body>" +
                        "Your Verification code is ."
                        + "<br/> "  + "<u>" + randomCode +"</u>" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>");

        user.setEmailVerifiedAt(new Date()); 
        user.setRandomCode(randomCode);
        userRepository.save(user);
        return (
            "Email verified successfully." + 
            "\n{\n\tFirstName: " + user.getFirstName() + 
            ",\n\tLastName: " + user.getLastName() + 
            ",\n\tEmail: " + user.getEmail() +
            ",\n\tVerified At: " + user.getEmailVerifiedAt() +
            "\n}");
        
    }
    
}

