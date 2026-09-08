package com.example.notesmanagement.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.notesmanagement.security.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private Logger logger=LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFiler in action getting request "+request.getRequestURI());
        try {
            String jwt=parse(request);
            if(jwt!=null && jwtUtil.validate(jwt)){
                String userName=jwtUtil.extractUserNameFromToken(jwt);
                UserDetails userDetails=userDetailsServiceImpl.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                authentication.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Roles from Jwt: {}",userDetails.getAuthorities());
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Cannot set Authentication "+e.getMessage());
        }
    }

    public String parse(HttpServletRequest request){
        String jwt=jwtUtil.getJwtFromCookie(request);
        return jwt;
    }

}
