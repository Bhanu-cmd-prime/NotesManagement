package com.example.notesmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class DashboardResponse {
    private Integer totalNotes;
    private Integer pinnedNotes;
    private Integer archivedNotes;
    private Integer totalCategories;
    private Integer totalTags;
}
