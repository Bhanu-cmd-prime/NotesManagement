package com.example.notesmanagement.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.example.notesmanagement.security.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    private Logger logger=LoggerFactory.getLogger(JwtUtil.class);
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    @Value("${spring.app.jwtExpiration}")
    private Integer jwtExpiration;
    @Value("${spring.app.jwtCookieName}")
    private String jwtCookieName;

    private Key key(){
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtSecret)
        );
    }

    public String getJwtFromCookie(HttpServletRequest request){
        Cookie cookie=WebUtils.getCookie(request, jwtCookieName);
        if(cookie!=null) return cookie.getValue();
        return null;
    }

    public String generateJwtFromUsername(String userName){
        return Jwts.builder().subject(userName).issuedAt(new Date()).expiration(new Date(new Date().getTime()+jwtExpiration)).signWith(key()).compact();
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal){
        String jwt=generateJwtFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie=ResponseCookie.from(jwtCookieName,jwt).path("/").maxAge(24*60*60).httpOnly(false).build();
        return cookie;
    }

    public String extractUserNameFromToken(String token){
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean validate(String authToken){
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        }catch(MalformedJwtException e){
            logger.error("Invalid Jwt Token "+e.getMessage());
        }catch(ExpiredJwtException e){
            logger.error("Expired Jwt Token "+e.getMessage());
        }catch(UnsupportedJwtException e){
            logger.error("Unsupported Jwt Token "+e.getMessage());
        }catch (IllegalArgumentException e) {
            logger.error("Illegal Jwt Token "+e.getMessage());
        }
        return false;
    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie=ResponseCookie.from(jwtCookieName, null).path("/").build();
        return cookie;
    }
}
