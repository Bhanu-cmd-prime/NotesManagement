package com.example.notesmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notesmanagement.model.User;
import com.example.notesmanagement.repository.UserRepository;
import com.example.notesmanagement.security.UserDetailsImpl;
import com.example.notesmanagement.security.jwt.JwtUtil;
import com.example.notesmanagement.security.request.LoginRequest;
import com.example.notesmanagement.security.request.RegisterRequest;
import com.example.notesmanagement.security.response.MessageResponse;
import com.example.notesmanagement.security.response.UserInfoResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try {
            authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String,Object> map=new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status",false);
            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie=jwtUtil.generateJwtCookie(userDetails);
        List<String> roles=userDetails.getAuthorities().stream().map(item->item.getAuthority()).toList();
        UserInfoResponse userInfoResponse=new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(userInfoResponse);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest){
        if(userRepository.existsByUserName(signUpRequest.getUserName())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username Already Taken"));
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email Already in use"));
        }
        User user=new User(signUpRequest.getUserName(),signUpRequest.getEmail(),passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully"));
    }
    
    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if(authentication !=null){
            return authentication.getName();
        }else{
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        List<String> roles=userDetails.getAuthorities().stream().map(item->item.getAuthority()).toList();
        UserInfoResponse loginResponse=new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie =jwtUtil.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Successfully Signed Out"));
    }
}
