package com.proj.kafkachat.configuration;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class ProfileUploadExceptionHandler {
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity
                .badRequest()
                .body("File size exceeds the allowed limit.");
    }
}
