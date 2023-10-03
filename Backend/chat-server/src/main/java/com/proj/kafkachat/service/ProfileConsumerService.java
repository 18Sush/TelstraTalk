package com.proj.kafkachat.service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BooleanSupplier;
@Service
public class ProfileConsumerService {
	
	@KafkaListener(topics = "user-profile-picture-uploaded", groupId = "GROUP_ID")
    public void consumeUserProfilePicture(byte[] profilePicture) {
        try {
            // Define the file path where you want to save the profile picture
            String filePath = "/path/to/save/profile_picture.jpg"; // Replace with your desired file path

            // Create a FileOutputStream to write the profile picture data to a file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(profilePicture);
            }

            // You can add additional logic here if needed, such as updating the user entity in the database.
            // For example, you might want to store the file path in the user's profile.

            System.out.println("Profile picture saved to: " + filePath);
        } catch (IOException e) {
            // Handle any exceptions that may occur during file writing
            e.printStackTrace();
            // You can log the error or take appropriate actions as needed
        }
    }
}
