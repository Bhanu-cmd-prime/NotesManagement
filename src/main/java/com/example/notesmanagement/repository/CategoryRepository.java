package com.example.notesmanagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notesmanagement.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{

    boolean existsByName(String noteRequest);

    Category findByName(String category);

    boolean existsByNameAndUserUserId(String category, Long loggedInUserId);

    Category findByNameAndUserUserId(String category, Long loggedInUserId);

    Page<Category> findByUserUserId(Pageable pageDetails, Object log);

    Optional<Category> findByCategoryIdAndUserUserId(Long categoryId, Long loggedInUserId);

    boolean existsByCategoryIdAndUserUserId(Long categoryId, Long loggedInUserId);

    Integer countByUserUserId(Long userId);

}
