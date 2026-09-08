package com.example.notesmanagement.exception;

public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID=1L;
    private String resourceName;
    private String resourseField;
    private Long resourseId;
    

    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public String getResourseField() {
        return resourseField;
    }
    public void setResourseField(String resourseField) {
        this.resourseField = resourseField;
    }
    public Long getResourseId() {
        return resourseId;
    }
    public void setResourseId(Long resourseId) {
        this.resourseId = resourseId;
    }
    public ResourceNotFoundException(String resourceName, String resourseField, Long resourseId) {
        super(String.format("%s not found with %s: %d",resourceName,resourseField,resourseId));
        this.resourceName = resourceName;
        this.resourseField = resourseField;
        this.resourseId = resourseId;
    }
    public ResourceNotFoundException(String message){
        super(message);
    }
}
