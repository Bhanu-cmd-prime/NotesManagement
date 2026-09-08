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
import com.example.notesmanagement.payload.request.CreateTagRequest;
import com.example.notesmanagement.payload.response.TagDTOResponse;
import com.example.notesmanagement.payload.response.TagResponse;
import com.example.notesmanagement.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    @PostMapping(value = "/tags")
    public ResponseEntity<?> saveTag(@RequestBody CreateTagRequest tagRequest){
        TagResponse tagResponse=tagService.saveTag(tagRequest);
        return ResponseEntity.ok(tagResponse);
    }

    @GetMapping(value = "/tags")
    public ResponseEntity<?> getAllTags(@RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
                                        @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                        @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_TAG_BY,required = false)String sortBy,
                                        @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder){
        TagDTOResponse tagDTOResponse=tagService.getAllTag(pageNumber,pageSize,sortBy,sortOrder);
                                            return ResponseEntity.ok(tagDTOResponse);                                    
    }

    @GetMapping(value = "/tags/{tag_id}")
    public ResponseEntity<?> getSingleTag(@PathVariable(name="tag_id") Long tagId){
        TagResponse tagResponse=tagService.getSingleTag(tagId);
        return ResponseEntity.ok(tagResponse);
    }

    @PutMapping(value = "/tags/{tag_id}")
    public ResponseEntity<?> updateTag(@RequestBody CreateTagRequest tagRequest,@PathVariable(name="tag_id") Long tagId){
        TagResponse tagResponse=tagService.updateTag(tagRequest,tagId);
        return ResponseEntity.ok(tagResponse);
    }

    @DeleteMapping(value = "/tags/{tag_id}")
    public ResponseEntity<?> deleteTag(@PathVariable(name="tag_id") Long tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("Tag deleted successfully");
    }
}
