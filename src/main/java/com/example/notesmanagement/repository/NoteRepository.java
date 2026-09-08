package com.example.notesmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notesmanagement.model.Note;

public interface NoteRepository extends JpaRepository<Note,Long>{

    Page<Note> findByUserUserId(Pageable pageDetails, Long loggedInUserId);

    Optional<Note> findByNoteIdAndUserUserId(Long noteId, Long loggedInUserId);

    boolean existsByNoteIdAndUserUserId(Long noteId, Long loggedInUserId);

    List<Note> findByTagsTagId(Long tagId);

    Page<Note> findByTitleLikeIgnoreCaseAndUserUserId(Pageable pageDetails, String string, Long loggedInUserId);

    Page<Note> findByCategoryNameLikeIgnoreCaseAndTagsNameLikeIgnoreCaseAndUserUserId(Pageable pageDetails, String string,
            String string2, Long loggedInUserId);

    Page<Note> findByCategoryNameLikeIgnoreCaseAndUserUserId(Pageable pageDetails, String string, Long loggedInUserId);

    Page<Note> findByTagsNameLikeIgnoreCaseAndUserUserId(Pageable pageDetails, String string, Long loggedInUserId);

    Page<Note> findByPinnedTrueAndUserUserId(Pageable pageDetails, Long loggedInUserId);

    Page<Note> findByArchivedTrueAndUserUserId(Pageable pageDetails, Long loggedInUserId);

    Page<Note> findByArchivedFalseAndUserUserId(Pageable pageDetails, Long loggedInUserId);

    Integer countByUserUserId(Long userId);

    Integer countByPinnedTrueAndUserUserId(Long userId);

    Integer countByArchivedTrueAndUserUserId(Long userId);


}
