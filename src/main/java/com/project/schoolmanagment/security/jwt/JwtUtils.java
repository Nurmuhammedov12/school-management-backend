package com.project.schoolmanagment.security.jwt;

import com.project.schoolmanagment.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtExpirationMs}")
    private long jwtExpirations;

    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecret;


    /**
     * Generates a JSON Web Token (JWT) based on the provided authentication.
     *
     * @param authentication the authentication object representing the current user
     * @return the generated JWT token
     */
    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return buildTokenFromUsername(userDetails.getUsername());
    }

    /**
     * Builds a JSON Web Token (JWT) using the provided username.
     *
     * @param username the username used as the subject of the JWT
     * @return the generated JWT token
     */
    private String buildTokenFromUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirations))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    /**
     * Validates a JSON Web Token (JWT).
     *
     * @param jwtToken the JWT token to be validated
     * @return true if the JWT token is valid, false otherwise
     */
    public boolean validateJwtToken(String jwtToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


    /**
     * Retrieves the username from a JSON Web Token (JWT).
     *
     * @param token the JWT token from which to retrieve the username
     * @return the username extracted from the JWT token
     */
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
