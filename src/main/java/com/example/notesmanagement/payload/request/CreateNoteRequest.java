package com.example.notesmanagement.payload.request;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String category;
    private List<String> tags;
}
