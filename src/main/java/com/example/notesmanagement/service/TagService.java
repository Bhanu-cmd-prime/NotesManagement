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
import com.example.notesmanagement.model.Tag;
import com.example.notesmanagement.model.User;
import com.example.notesmanagement.payload.request.CreateTagRequest;
import com.example.notesmanagement.payload.response.TagDTOResponse;
import com.example.notesmanagement.payload.response.TagResponse;
import com.example.notesmanagement.repository.NoteRepository;
import com.example.notesmanagement.repository.TagRepository;
import com.example.notesmanagement.util.AuthUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final NoteRepository noteRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    public void saveTagByName(String tag) {
        Tag tagObject=new Tag(tag);
        tagRepository.save(tagObject);
    }
    public TagResponse saveTag(CreateTagRequest tagRequest) {
        Tag tag=modelMapper.map(tagRequest, Tag.class);
        if(tagRepository.existsByName(tag.getName())){
            throw new ApiException("Tag with this name already exists");
        }
        tag.setUser(authUtil.loggedInUser());
        tagRepository.save(tag);
        return modelMapper.map(tag, TagResponse.class);
    }
    public TagDTOResponse getAllTag(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails=PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Tag> tagPage=tagRepository.findByUserUserId(pageDetails,authUtil.LoggedInUserId());
        List<Tag> response=tagPage.getContent();
        List<TagResponse> tagResponse=response.stream().map(tag->modelMapper.map(tag, TagResponse.class)).toList();
        TagDTOResponse tagDTOResponse=new TagDTOResponse();
        tagDTOResponse.setTags(tagResponse);
        tagDTOResponse.setPageNumber(tagPage.getNumber());
        tagDTOResponse.setPageSize(tagPage.getSize());
        tagDTOResponse.setTotalPages(tagPage.getTotalPages());
        tagDTOResponse.setTotalElements(tagPage.getTotalElements());
        tagDTOResponse.setLastPage(tagPage.isLast());
        return tagDTOResponse; 
    }
    public TagResponse getSingleTag(Long tagId) {
        Tag tag=tagRepository.findByTagIdAndUserUserId(tagId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Tag", "TagId", tagId));
        return modelMapper.map(tag, TagResponse.class);
    }
    public TagResponse updateTag(CreateTagRequest tagRequest, Long tagId) {
        Tag existingTag=tagRepository.findByTagIdAndUserUserId(tagId,authUtil.LoggedInUserId()).orElseThrow(()->new ResourceNotFoundException("Tag", "TagId", tagId));
        Tag tag=modelMapper.map(tagRequest, Tag.class);
        existingTag.setName(tag.getName());
        tagRepository.save(existingTag);
        return modelMapper.map(existingTag, TagResponse.class);
    }
    @Transactional
    public void deleteTag(Long tagId) {
        if(!tagRepository.existsByTagIdAndUserUserId(tagId,authUtil.LoggedInUserId())){
            throw new ApiException("No Tag found with this Id");
        }
        Tag tag=tagRepository.findById(tagId).orElseThrow(()->new ResourceNotFoundException("Tag", "TagId", tagId));
        noteRepository.findByTagsTagId(tagId).forEach(note->{note.getTags().remove(tag);noteRepository.save(note);});
        tagRepository.deleteById(tagId);
    }
    public void saveTagByNameAndUser(String tagName, User loggedInUser) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setUser(loggedInUser);
        tagRepository.save(tag);
    }

}
