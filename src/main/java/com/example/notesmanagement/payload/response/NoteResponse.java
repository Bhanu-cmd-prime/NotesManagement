package com.example.notesmanagement.payload.response;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private Long noteId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPinned;
    private boolean isArchived;
    private CategoryResponse category;
    private Set<TagResponse> tags;
}
