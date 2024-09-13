package com.spring.main.service;

import javax.crypto.SecretKey;
import java.util.function.Function;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "V6/RpCdYrJWkj87dnqz+ekvomWvtYwuI7e7FRCGXq+4cMX0VgPrc7ROJ4nVAJAxV";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //extract a claim
    //T means any type
    //Claims have getSubject, getExpiration...
    //if we use Claims::getSubject, it returns String. If we use getExpiration, it returns Date
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken( 
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ){
        return Jwts
            .builder()
            .claims(extraClaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //extract all claims
    //three sections in jwt - Header, Payload, Verify Signature
    //this method return Payload section
    //sample output
    //{
    //   "sub": "user123",
    //   "iat": 1615327632,
    //   "exp": 1615331232,
    //   "role": "admin"
    // }
    private Claims extractAllClaims(String token){
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); 
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
