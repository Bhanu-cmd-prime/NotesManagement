package com.example.notesmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.notesmanagement.configuration.AppConstants;
import com.example.notesmanagement.payload.request.CreateNoteRequest;
import com.example.notesmanagement.payload.response.DashboardResponse;
import com.example.notesmanagement.payload.response.NoteDTOResponse;
import com.example.notesmanagement.payload.response.NoteResponse;
import com.example.notesmanagement.service.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping(value = "/notes")
    public ResponseEntity<?> saveNotes(@Valid @RequestBody CreateNoteRequest noteRequest){
        NoteResponse noteResponse=noteService.saveNotes(noteRequest);
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping(value = "/notes")
    public ResponseEntity<?> getAllNotes(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_NOTES_BY,required = false) String sortBy,
                                         @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        NoteDTOResponse noteResponse=noteService.getAllNotes(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping(value = "/notes/filter")
    public ResponseEntity<?> filterNotes(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_NOTES_BY,required = false) String sortBy,
                                         @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder,
                                         @RequestParam(name="category",required = false) String categoryName,
                                        @RequestParam(name="tag",required = false) String tagName){
        NoteDTOResponse noteResponse=noteService.filterNotes(pageNumber,pageSize,sortBy,sortOrder,categoryName,tagName);
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping(value = "/notes/search")
    public ResponseEntity<?> searchByKeyword(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_NOTES_BY,required = false) String sortBy,
                                         @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder,
                                        @RequestParam (name="keyword",required = false) String keyword){
        NoteDTOResponse noteResponse=noteService.searchByKeyword(pageNumber,pageSize,sortBy,sortOrder,keyword);
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping(value = "/notes/{note_id}")
    public ResponseEntity<?> getSingleNote(@PathVariable(name = "note_id") Long noteId){
        NoteResponse noteResponse=noteService.getSingleNote(noteId);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping(value = "/notes/{note_id}/pin")
    public ResponseEntity<?> pinNote(@PathVariable(name="note_id") Long noteId){
        NoteResponse noteResponse=noteService.pinNote(noteId);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping(value = "/notes/{note_id}/unpin")
    public ResponseEntity<?> unPinNote(@PathVariable(name="note_id") Long noteId){
        NoteResponse noteResponse=noteService.unPinNote(noteId);
        return ResponseEntity.ok(noteResponse);
    }


    @GetMapping (value = "/notes/pinned")
    public ResponseEntity<?> getAllPinned(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_NOTES_BY,required = false) String sortBy,
                                         @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        NoteDTOResponse noteResponse=noteService.getAllPinned(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping(value = "/notes/{note_id}/archive")
    public ResponseEntity<?> archiveNote(@PathVariable(name="note_id") Long noteId){
        NoteResponse noteResponse=noteService.archiveNote(noteId);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping(value = "/notes/{note_id}/unarchive")
    public ResponseEntity<?> unarchive(@PathVariable(name="note_id") Long noteId){
        NoteResponse noteResponse=noteService.unArchiveNote(noteId);
        return ResponseEntity.ok(noteResponse);
    }


    @GetMapping (value = "/notes/archived")
    public ResponseEntity<?> getAllArchived(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_NOTES_BY,required = false) String sortBy,
                                         @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        NoteDTOResponse noteResponse=noteService.getAllArchived(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping(value = "/notes/{note_id}")
    public ResponseEntity<?> updateNote(@Valid @RequestBody CreateNoteRequest noteRequest,@PathVariable(name = "note_id") Long noteId){
        NoteResponse noteResponse=noteService.updateNote(noteRequest,noteId);
        return ResponseEntity.ok(noteResponse);
    }

    @DeleteMapping(value = "/notes/{note_id}")
    public ResponseEntity<?> deleteNote(@PathVariable(name = "note_id") Long noteId){
        noteService.deleteNote(noteId);
        return ResponseEntity.ok("Successfully deleted the note");
    }

    @GetMapping("/auth/dashboard")
    public ResponseEntity<?> dashBoard(){
        DashboardResponse dashboardResponse=noteService.dashBoard();
        return ResponseEntity.ok(dashboardResponse);
    }
}
