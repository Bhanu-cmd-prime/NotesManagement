package com.example.notesmanagement.payload.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String category;
    private Set<String> tags;
    private boolean isPinned;
    private boolean isArchived;
}
