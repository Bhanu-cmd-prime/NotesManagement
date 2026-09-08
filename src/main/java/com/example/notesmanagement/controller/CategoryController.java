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
import com.example.notesmanagement.payload.request.CreateCategoryRequest;
import com.example.notesmanagement.payload.response.CategoryDTOResponse;
import com.example.notesmanagement.payload.response.CategoryResponse;
import com.example.notesmanagement.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping(value = "/categories")
    public ResponseEntity<?> saveCategory(@RequestBody CreateCategoryRequest categoryRequest){
        CategoryResponse categoryResponse=categoryService.saveCategory(categoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<?> getCategories(@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORY_BY,required = false) String sortBy,
                                         @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        CategoryDTOResponse categoryDTOResponse=categoryService.getCategories(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(categoryDTOResponse);
    }

    @GetMapping(value ="/categories/{category_id}")
    public ResponseEntity<?> getSingleCategory(@PathVariable(value = "category_id") Long categoryId){
        CategoryResponse categoryResponse=categoryService.getSingleCategory(categoryId);
        return ResponseEntity.ok(categoryResponse);
    }

    @PutMapping(value="/categories/{category_id}")
    public ResponseEntity<?> updateCategory(@RequestBody CreateCategoryRequest categoryRequest,@PathVariable(value = "category_id") Long categoryId){
        CategoryResponse categoryResponse=categoryService.updateCategory(categoryRequest,categoryId);
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping(value = "/categories/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "category_id") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category Deleted Successfully");
    }
}
