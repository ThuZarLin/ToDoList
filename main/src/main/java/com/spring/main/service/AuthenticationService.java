package com.spring.main.service;

import java.util.List;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.main.enitity.Token;
import com.spring.main.enitity.User;
import com.spring.main.form.AuthenticationResponse;
import com.spring.main.form.LoginForm;
import com.spring.main.form.RegistrationForm;
import com.spring.main.repository.TokenRepository;
import com.spring.main.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final TokenRepository tokenRepository;

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    String randomCode = getRandomNumberString();

    public void saveUserToken(String jwtToken, User user){
        Token token = Token.builder()
            .token(jwtToken)
            .isLoggedOut(false)
            .user(user)
            .build();
        
        tokenRepository.save(token);
    }

    public AuthenticationResponse register(RegistrationForm request){
        var user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .profilePath("No profile to show")
            .randomCode(randomCode)
            .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        saveUserToken(jwtToken, user);

        //save token to Token table


        // String verificationLink = "http://localhost:8080/api/verify?token=" + jwtToken;

        // String emailContent = "Thank you for registering. Please click the following link to verify your account: " + verificationLink;

        // emailSender.sendEmail(request.getEmail(), emailContent);

        emailSender.sendEmail(request.getEmail(), "<html>" +
                        "<body>" +
                        "<h2>Dear "+ request.getFirstName() + " " + request.getLastName() + ",</h2>"
                        + "<br/> We're excited to have you get started. " +
                        "Your Verification code is ."
                        + "<br/> "  + "<u>" + randomCode +"</u>" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>");

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    private void getValidTokenListByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(token -> {
                token.setLoggedOut(true);
            });
        }

        tokenRepository.saveAll(validTokenListByUser);
    }

    public AuthenticationResponse login(LoginForm request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), 
                request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        getValidTokenListByUser(user);

        saveUserToken(jwtToken, user);

        if (user.getEmailVerifiedAt() == null){
            emailSender.sendEmail(request.getEmail(), "<html>" +
                        "<body>" +
                        "<h2>Dear, </h2>"
                        + "<br/> Your email is not verified yet. Please verify. <br/>" +
                        "Your Verification code is ."
                        + "<br/> "  + "<u>" + randomCode +"</u>" +
                        "<br/> Regards,<br/>" +
                        "MFA Registration team" +
                        "</body>" +
                        "</html>");

            return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
        }else{
            return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build(); 
        }
    }


}
