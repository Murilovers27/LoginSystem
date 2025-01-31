package com.login.login.Infra.Security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.login.login.Entity.User;



@Service
public class TokenService {
    @org.springframework.beans.factory.annotation.Value("${api.security.token.secret}")
    private String secret
;
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
            .withIssuer("Login-api")
            .withSubject(user.getEmail())
            .withExpiresAt(this.generateExp())
            .sign(algorithm);
        return token;
            
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error during token creation", exception);
        }
    }
    
    public String validateTk(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
            .withIssuer("login-auth")
            .build()
            .verify(token)
            .getSubject();
        } catch(JWTCreationException exception){
            return null;
        }
    }
    private Instant generateExp(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(3));
    }
}

