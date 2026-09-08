package com.example.notesmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users",uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(min = 5, message = "Username Should be atlest 5 characters long")
    @Column(name = "username")
    private String userName;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<Note> notes=new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Category> categories=new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Tag> tags=new HashSet<>();

    public User(@NotBlank @Size(min = 5, message = "Username Should be atlest 5 characters long") String userName,
            @NotBlank @Email String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }
    
}
