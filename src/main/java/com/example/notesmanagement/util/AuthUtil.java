package com.example.notesmanagement.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.notesmanagement.model.User;
import com.example.notesmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserRepository userRepository;
    public String loggedInEmail(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("No User Found with this name "+authentication.getName()));
        return user.getEmail();
    }

    public Long LoggedInUserId(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("No User Found with this name "+authentication.getName()));
        return user.getUserId();
    }

    public User loggedInUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("No User Found with this name "+authentication.getName()));
        return user;
    }
}
