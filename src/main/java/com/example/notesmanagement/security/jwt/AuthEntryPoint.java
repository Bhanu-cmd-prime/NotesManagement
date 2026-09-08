package com.example.notesmanagement.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{

    private final Logger logger=LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}",authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String,Object> boby=new HashMap<>();
        boby.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        boby.put("error", "Unauthorized");
        boby.put("message", authException.getMessage());
        boby.put("path", request.getServletPath());

        final ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), boby);
    }

}
