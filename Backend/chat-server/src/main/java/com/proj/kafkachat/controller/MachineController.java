package com.proj.kafkachat.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.proj.kafkachat.model.Machine;
import com.proj.kafkachat.repository.MachineRepository;
import com.proj.kafkachat.service.MachineService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/machines")
public class MachineController {
    
	@Autowired
    private MachineService machineService;

	 @Autowired
	 private MachineRepository machineRepository;
    
	@GetMapping
    public List<Machine> getAllMachines() {
        return machineService.getAllMachinesWithStatus();
    }

@PostMapping
	public ResponseEntity<?> registerMachine(@RequestBody Machine machine) {
	    try {
	        Machine registeredMachine = machineService.registerMachine(machine);
	        // Create a simple success message response
	        String successMessage = "Machine registered successfully.";
	        return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
	    } catch (IllegalArgumentException e) {
	        // Return an error message response
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}
@DeleteMapping("/{id}")
public ResponseEntity<?> unregisterMachine(@PathVariable Long id) {
    try {
        machineService.unregisterMachine(id);
        return ResponseEntity.ok("Machine unregistered successfully.");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


    
@PostMapping("/{id}/status")
    public ResponseEntity<?> updateMachineStatus(@PathVariable Long id, @RequestParam("online") boolean online) {
        try {
            machineService.updateMachineStatus(id, online);
            return ResponseEntity.ok("Machine status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/online")
    public ResponseEntity<List<Machine>> getOnlineMachines() {
        List<Machine> onlineMachines = machineService.getOnlineMachines();
        return ResponseEntity.ok(onlineMachines);
    }
    
    @PostMapping("/{userId}/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

		// Check if the user exists
        Optional<Machine> optionalUser = machineRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Machine user = optionalUser.get();
            try {
                // Validate and save the image
                if (!file.isEmpty() && isImageValid(file)) {
                    user.setProfilePicture(file.getBytes());
                    machineRepository.save(user);
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
    
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable Long userId) {
        // Check if the user exists
        java.util.Optional<Machine> optionalUser = machineRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Machine user = optionalUser.get();
            byte[] profilePicture = user.getProfilePicture();
            if (profilePicture != null) {
                // Delete the profile picture
                user.setProfilePicture(null);
                machineRepository.save(user);
                return ResponseEntity.ok("Profile picture deleted successfully.");
            } else {
                return ResponseEntity.ok("Profile picture not found.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private boolean isImageValid(MultipartFile file) {
        // Get the file's original filename
        String originalFilename = file.getOriginalFilename();
        
        // Check if the original filename is not null and ends with a valid image extension
        if (originalFilename != null && (originalFilename.toLowerCase().endsWith(".jpg") || originalFilename.toLowerCase().endsWith(".jpeg"))) {
            return true; // Valid image file
        }
        
        return false; // Invalid image file
    }
    
    
   
}