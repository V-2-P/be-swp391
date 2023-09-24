package com.v2p.swp391.security;

import com.v2p.swp391.config.AppProperties;

import com.v2p.swp391.exception.AppException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            if (ex instanceof SignatureException) {
                logger.error("Invalid JWT signature");
                throw new AppException(HttpStatus.UNAUTHORIZED,"Invalid JWT signature");
            } else if (ex instanceof MalformedJwtException) {
                logger.error("Invalid JWT token");
                throw new AppException(HttpStatus.UNAUTHORIZED,"Invalid JWT token");
            } else if (ex instanceof ExpiredJwtException) {
                logger.error("Expired JWT token");
                throw new AppException(HttpStatus.UNAUTHORIZED,"Expired JWT token");
            } else if (ex instanceof UnsupportedJwtException) {
                logger.error("Unsupported JWT token");
                throw new AppException(HttpStatus.UNAUTHORIZED,"Unsupported JWT token");
            } else if (ex instanceof IllegalArgumentException) {
                logger.error("JWT claims string is empty.");
                throw new AppException(HttpStatus.UNAUTHORIZED,"JWT claims string is empty.");
            }
        }
        return false;
    }

}
