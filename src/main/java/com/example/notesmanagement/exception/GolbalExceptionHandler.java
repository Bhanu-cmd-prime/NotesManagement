package com.example.notesmanagement.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.notesmanagement.payload.response.ApiResponse;

@RestControllerAdvice
public class GolbalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> myMethodArgumentException(MethodArgumentNotValidException e){
        Map<String,Object> map=new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err->{
            String fieldName=((FieldError) err).getField();
            String message=err.getDefaultMessage();
            map.put(fieldName, message);
        });
        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> myResourceException(ResourceNotFoundException e){
        String message=e.getMessage();
        ApiResponse apiResponse=new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<?> myApiException(ApiException e){
        String message=e.getMessage();
        ApiResponse apiResponse=new ApiResponse(message, false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}
