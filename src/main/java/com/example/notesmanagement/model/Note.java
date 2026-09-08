package com.example.notesmanagement.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long noteId;

    @NotBlank
    @Column(name = "title")
    private String title;
    @NotBlank
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_pinned")
    private boolean pinned;
    @Column(name = "is_archived")
    private boolean archived;
    
    public Note(@NotBlank String title, @NotBlank String content,
            boolean isPinned, boolean isArchived) {
        this.title = title;
        this.content = content;
        this.pinned = isPinned;
        this.archived = isArchived;
    }

    public void setCreatedAt(){
        this.createdAt=LocalDateTime.now();
    }
    public void setUpdateAt(){
        this.updatedAt=LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name ="category_id")
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "note_tag",joinColumns = @JoinColumn(name="note_id"),inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags=new ArrayList<>();
}
