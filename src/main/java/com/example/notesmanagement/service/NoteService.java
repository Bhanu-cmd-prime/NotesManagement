package com.example.notesmanagement.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.notesmanagement.exception.ApiException;
import com.example.notesmanagement.exception.ResourceNotFoundException;
import com.example.notesmanagement.model.Category;
import com.example.notesmanagement.model.Note;
import com.example.notesmanagement.model.Tag;
import com.example.notesmanagement.model.User;
import com.example.notesmanagement.payload.request.CreateNoteRequest;
import com.example.notesmanagement.payload.response.DashboardResponse;
import com.example.notesmanagement.payload.response.NoteDTOResponse;
import com.example.notesmanagement.payload.response.NoteResponse;
import com.example.notesmanagement.repository.CategoryRepository;
import com.example.notesmanagement.repository.NoteRepository;
import com.example.notesmanagement.repository.TagRepository;
import com.example.notesmanagement.util.AuthUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    public NoteResponse saveNotes(CreateNoteRequest noteRequest) {
        User loggedInUser=authUtil.loggedInUser();
        Note note=modelMapper.map(noteRequest, Note.class);
        note.setTags(new ArrayList<>());
        note.setCreatedAt();
        note.setUpdateAt();
        if(!categoryRepository.existsByNameAndUserUserId(noteRequest.getCategory(),authUtil.LoggedInUserId())){
            categoryService.saveCategoryByName(noteRequest.getCategory());
        }
        Category category=categoryRepository.findByNameAndUserUserId(noteRequest.getCategory(),authUtil.LoggedInUserId());
        note.setCategory(category);
        loggedInUser.getCategories().add(category);
        category.setUser(loggedInUser);
        //noteRepository.save(note);
        categoryRepository.save(category);
        List<Tag> allTags=new ArrayList<>();
        Iterator<String>it=noteRequest.getTags().iterator();
        while (it.hasNext()) {
            String tag=it.next();
            if(!tagRepository.existsByNameAndUserUserId(tag,loggedInUser.getUserId())){
                tagService.saveTagByNameAndUser(tag,loggedInUser);
            }
            Tag tagObject=tagRepository.findByNameAndUserUserId(tag,loggedInUser.getUserId());
            //tagObject.setUser(loggedInUser);
            //loggedInUser.getTags().add(tagObject);
            //tagObject.getNotes().add(note);
            allTags.add(tagObject);
        }
        note.getTags().addAll(allTags);
        loggedInUser.getNotes().add(note);
        //userRepository.save(loggedInUser);
        note.setUser(loggedInUser);
        noteRepository.save(note);
        return modelMapper.map(note, NoteResponse.class);
    }

    public NoteDTOResponse getAllNotes(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Note> notePage=noteRepository.findByArchivedFalseAndUserUserId(pageDetails,authUtil.LoggedInUserId());
        List<Note> notes=notePage.getContent();
        NoteDTOResponse noteResponse=new NoteDTOResponse();
        List<NoteResponse> respones=notes.stream().map(note->modelMapper.map(note,NoteResponse.class)).toList();
        noteResponse.setNotes(respones);
        noteResponse.setPageNumber(notePage.getNumber());
        noteResponse.setPageSize(notePage.getSize());
        noteResponse.setTotalPages(notePage.getTotalPages());
        noteResponse.setTotalElements(notePage.getTotalElements());
        noteResponse.setLastPage(notePage.isLast());
        return noteResponse;
    }

    public NoteResponse updateNote(CreateNoteRequest noteRequest,Long noteId) {
        Note note=modelMapper.map(noteRequest, Note.class);
        Note existingNote=noteRepository.findByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId()).orElseThrow(()->new IllegalArgumentException("No Note Found with the id"));
        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        existingNote.setUpdateAt();
        existingNote.setPinned(note.isPinned());
        existingNote.setArchived(note.isArchived());
        noteRepository.save(existingNote);
        return modelMapper.map(existingNote, NoteResponse.class);
    }

    public void deleteNote(Long noteId) {
        if(!noteRepository.existsByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId())){
            throw new ResourceNotFoundException("Note", "NoteId", noteId);
        }
        noteRepository.deleteById(noteId);
    }

    public NoteResponse getSingleNote(Long noteId) {
        Note note=noteRepository.findByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Note", "NoteId", noteId));
        NoteResponse noteResponse=modelMapper.map(note, NoteResponse.class);
        return noteResponse;
    }

    public NoteDTOResponse searchByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String keyword) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Note> notePage=noteRepository.findByTitleLikeIgnoreCaseAndUserUserId(pageDetails,"%"+keyword+"%",authUtil.LoggedInUserId());
        List<Note> notes=notePage.getContent();
        NoteDTOResponse noteResponse=new NoteDTOResponse();
        List<NoteResponse> respones=notes.stream().map(note->modelMapper.map(note,NoteResponse.class)).toList();
        noteResponse.setNotes(respones);
        noteResponse.setPageNumber(notePage.getNumber());
        noteResponse.setPageSize(notePage.getSize());
        noteResponse.setTotalPages(notePage.getTotalPages());
        noteResponse.setTotalElements(notePage.getTotalElements());
        noteResponse.setLastPage(notePage.isLast());
        return noteResponse;
    }

    public NoteDTOResponse filterNotes(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
            String categoryName, String tagName) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Note> notePage;
        if(tagName==null&&categoryName!=null){
            notePage=noteRepository.findByCategoryNameLikeIgnoreCaseAndUserUserId(pageDetails,"%"+categoryName+"%",authUtil.LoggedInUserId());
        }
        else if(categoryName==null&&tagName!=null){
            notePage=noteRepository.findByTagsNameLikeIgnoreCaseAndUserUserId(pageDetails,"%"+tagName+"%",authUtil.LoggedInUserId());
        }
        else if(tagName!=null && categoryName!=null){
            notePage=noteRepository.findByCategoryNameLikeIgnoreCaseAndTagsNameLikeIgnoreCaseAndUserUserId(pageDetails,"%"+categoryName+"%","%"+tagName+"%",authUtil.LoggedInUserId());
        }
        else{
            notePage=noteRepository.findByUserUserId(pageDetails,authUtil.LoggedInUserId());
        }
        List<Note> notes=notePage.getContent();
        NoteDTOResponse noteResponse=new NoteDTOResponse();
        List<NoteResponse> respones=notes.stream().map(note->modelMapper.map(note,NoteResponse.class)).toList();
        noteResponse.setNotes(respones);
        noteResponse.setPageNumber(notePage.getNumber());
        noteResponse.setPageSize(notePage.getSize());
        noteResponse.setTotalPages(notePage.getTotalPages());
        noteResponse.setTotalElements(notePage.getTotalElements());
        noteResponse.setLastPage(notePage.isLast());
        return noteResponse;
    }

    public NoteResponse pinNote(Long noteId) {
        Note note=noteRepository.findByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Note", "NoteId", noteId));
        if(note.isPinned()==true) throw new ApiException("Cannot Pin this Note");
        note.setPinned(true);
        noteRepository.save(note);
        NoteResponse noteResponse=modelMapper.map(note, NoteResponse.class);
        return noteResponse;
    }

    public NoteResponse unPinNote(Long noteId) {
        Note note=noteRepository.findByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Note", "NoteId", noteId));
        if(note.isPinned()==false) throw new ApiException("Cannot Unpin this Note");
        note.setPinned(false);
        noteRepository.save(note);
        NoteResponse noteResponse=modelMapper.map(note, NoteResponse.class);
        return noteResponse;
    }

    public NoteDTOResponse getAllPinned(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Note> notePage=noteRepository.findByPinnedTrueAndUserUserId(pageDetails,authUtil.LoggedInUserId());
        List<Note> notes=notePage.getContent();
        NoteDTOResponse noteResponse=new NoteDTOResponse();
        List<NoteResponse> respones=notes.stream().map(note->modelMapper.map(note,NoteResponse.class)).toList();
        noteResponse.setNotes(respones);
        noteResponse.setPageNumber(notePage.getNumber());
        noteResponse.setPageSize(notePage.getSize());
        noteResponse.setTotalPages(notePage.getTotalPages());
        noteResponse.setTotalElements(notePage.getTotalElements());
        noteResponse.setLastPage(notePage.isLast());
        return noteResponse;
    }

    public NoteResponse archiveNote(Long noteId) {
        Note note=noteRepository.findByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Note", "NoteId", noteId));
        if(note.isArchived()==true) throw new ApiException("Cannot Archive this Note");
        note.setArchived(true);
        noteRepository.save(note);
        NoteResponse noteResponse=modelMapper.map(note, NoteResponse.class);
        return noteResponse;
    }

    public NoteResponse unArchiveNote(Long noteId) {
        Note note=noteRepository.findByNoteIdAndUserUserId(noteId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Note", "NoteId", noteId));
        if(note.isArchived()==false) throw new ApiException("Cannot UnArchive this Note");
        note.setArchived(false);
        noteRepository.save(note);
        NoteResponse noteResponse=modelMapper.map(note, NoteResponse.class);
        return noteResponse;
    }

    public NoteDTOResponse getAllArchived(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Note> notePage=noteRepository.findByArchivedTrueAndUserUserId(pageDetails,authUtil.LoggedInUserId());
        List<Note> notes=notePage.getContent();
        NoteDTOResponse noteResponse=new NoteDTOResponse();
        List<NoteResponse> respones=notes.stream().map(note->modelMapper.map(note,NoteResponse.class)).toList();
        noteResponse.setNotes(respones);
        noteResponse.setPageNumber(notePage.getNumber());
        noteResponse.setPageSize(notePage.getSize());
        noteResponse.setTotalPages(notePage.getTotalPages());
        noteResponse.setTotalElements(notePage.getTotalElements());
        noteResponse.setLastPage(notePage.isLast());
        return noteResponse;
    }

    public DashboardResponse dashBoard() {
        User loggedInUser=authUtil.loggedInUser();
        DashboardResponse dashboardResponse=new DashboardResponse();
        dashboardResponse.setTotalNotes(noteRepository.countByUserUserId(loggedInUser.getUserId()));
        dashboardResponse.setPinnedNotes(noteRepository.countByPinnedTrueAndUserUserId(loggedInUser.getUserId()));
        dashboardResponse.setArchivedNotes(noteRepository.countByArchivedTrueAndUserUserId(loggedInUser.getUserId()));
        dashboardResponse.setTotalCategories(categoryRepository.countByUserUserId(loggedInUser.getUserId()));
        dashboardResponse.setTotalTags(tagRepository.countByUserUserId(loggedInUser.getUserId()));
        return dashboardResponse;
    }

}
