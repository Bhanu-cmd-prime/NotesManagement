package com.example.notesmanagement.service;

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
import com.example.notesmanagement.payload.request.CreateCategoryRequest;
import com.example.notesmanagement.payload.response.CategoryDTOResponse;
import com.example.notesmanagement.payload.response.CategoryResponse;
import com.example.notesmanagement.repository.CategoryRepository;
import com.example.notesmanagement.util.AuthUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    public void saveCategoryByName(String category) {
        Category categoryObject=new Category(category);
        categoryObject.setUser(authUtil.loggedInUser());
        categoryRepository.save(categoryObject);
    }
    public CategoryResponse saveCategory(CreateCategoryRequest categoryRequest) {
        Category category=modelMapper.map(categoryRequest, Category.class);
        if(categoryRepository.existsByNameAndUserUserId(category.getName(),authUtil.LoggedInUserId())){
            throw new ApiException("Category Already Exists");
        }
        category.setUser(authUtil.loggedInUser());
        categoryRepository.save(category);
        //authUtil.loggedInUser().getCategories().add(category);
        return modelMapper.map(category, CategoryResponse.class);
    }
    public CategoryDTOResponse getCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage=categoryRepository.findByUserUserId(pageDetails,authUtil.LoggedInUserId());
        List<Category> categories=categoryPage.getContent();
        List<CategoryResponse> responses=categories.stream().map(category->modelMapper.map(category, CategoryResponse.class)).toList();
        CategoryDTOResponse categoryDTOResponse=new CategoryDTOResponse();
        categoryDTOResponse.setCategories(responses);
        categoryDTOResponse.setPageNumber(categoryPage.getNumber());
        categoryDTOResponse.setPageSize(categoryPage.getSize());
        categoryDTOResponse.setTotalPages(categoryPage.getTotalPages());
        categoryDTOResponse.setTotalElements(categoryPage.getTotalElements());
        categoryDTOResponse.setLastPage(categoryPage.isLast());
        return categoryDTOResponse;
    }
    public CategoryResponse getSingleCategory(Long categoryId) {
        Category category=categoryRepository.findByCategoryIdAndUserUserId(categoryId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
        return modelMapper.map(category, CategoryResponse.class);
    }
    public CategoryResponse updateCategory(CreateCategoryRequest categoryRequest, Long categoryId) {
        Category existingCategory=categoryRepository.findByCategoryIdAndUserUserId(categoryId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
        Category category=modelMapper.map(categoryRequest, Category.class);
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return modelMapper.map(existingCategory, CategoryResponse.class);
    }
    public void deleteCategory(Long categoryId) {
        if(!categoryRepository.existsByCategoryIdAndUserUserId(categoryId,authUtil.LoggedInUserId())){
            throw new ResourceNotFoundException("Category", "CategoryId", categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

}
