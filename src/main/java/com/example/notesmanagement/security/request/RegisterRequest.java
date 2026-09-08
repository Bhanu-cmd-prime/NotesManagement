package com.example.notesmanagement.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 5, message = "Username Should be atlest 5 characters long")
    private String userName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
