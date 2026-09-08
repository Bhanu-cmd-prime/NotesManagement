package com.example.notesmanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.notesmanagement.model.Tag;

public interface TagRepository extends JpaRepository<Tag,Long>{

    boolean existsByName(String tag);

    Tag findByName(String tag);

    @Query("SELECT t FROM Tag t WHERE t.name=?1 AND t.user.userId=?2")
    Tag findByNameAndUser(String tag, Long userId);

    boolean existsByNameAndUserUserId(String tag, Long userId);

    Tag findByNameAndUserUserId(String tag, Long userId);

    Page<Tag> findByUserUserId(Pageable pageDetails, Long loggedInUserId);

    Optional<Tag> findByTagIdAndUserUserId(Long tagId, Long loggedInUserId);

    boolean existsByTagIdAndUserUserId(Long tagId, Long loggedInUserId);

    Integer countByUserUserId(Long userId);

}
