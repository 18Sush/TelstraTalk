package com.proj.kafkachat.service;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.proj.kafkachat.model.Machine;

public class ProfileProducerService {
	
	

	@Autowired
	private KafkaTemplate<String, byte[]> kafkaTemplate;

	@PostMapping("/{userId}/upload-profile-picture")
	public ResponseEntity<?> uploadProfilePicture(
	        @PathVariable Long userId,
	        @RequestParam("file") MultipartFile file) {
	    CrudRepository<Machine, Long> machineRepository = null;
		java.util.Optional<Machine> optionalUser = machineRepository.findById(userId);
	    if (optionalUser.isPresent()) {
	        Machine user = optionalUser.get();
	        try {
	            if (!file.isEmpty() && isImageValid(file)) {
	                user.setProfilePicture(file.getBytes());
	                machineRepository.save(user);
	                
	                // Send the profile picture data to Kafka
	                kafkaTemplate.send("user-profile-picture-uploaded", user.getProfilePicture());
	                
	                return ResponseEntity.ok("Profile picture uploaded successfully.");
	            } else {
	                return ResponseEntity.badRequest().body("Invalid image format.");
	            }
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error uploading profile picture.");
	        }
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	private boolean isImageValid(MultipartFile file) {
		// TODO Auto-generated method stub
		return false;
	}
}
