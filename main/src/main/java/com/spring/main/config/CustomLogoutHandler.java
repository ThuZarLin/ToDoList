package com.spring.main.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.spring.main.enitity.Token;
import com.spring.main.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler{

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ") ){
            return;
        }
        String jwtToken = authHeader.substring(7);
        
        //get stored token from db
        Token storedToken = tokenRepository.findByToken(jwtToken).orElse(null);

        //invalidate the token i.e make logout true

        if(jwtToken != null){
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
       }

}
