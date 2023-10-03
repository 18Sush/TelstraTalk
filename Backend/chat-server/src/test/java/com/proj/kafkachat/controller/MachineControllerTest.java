package com.proj.kafkachat.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.proj.kafkachat.model.Machine;
import com.proj.kafkachat.repository.MachineRepository;
import com.proj.kafkachat.service.MachineService;

public class MachineControllerTest {

    @InjectMocks
    private MachineController machineController;

    @Mock
    private MachineService machineService;

    @Mock
    private MachineRepository machineRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterMachineSuccess() {
        // Arrange
        Machine machine = new Machine();
        when(machineService.registerMachine(any())).thenReturn(machine);

        // Act
        ResponseEntity<?> response = machineController.registerMachine(machine);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Machine registered successfully.", response.getBody());
    }

    @Test
    public void testRegisterMachineFailure() {
        // Arrange
        Machine machine = new Machine();
        when(machineService.registerMachine(any())).thenThrow(new IllegalArgumentException("Invalid machine data"));

        // Act
        ResponseEntity<?> response = machineController.registerMachine(machine);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid machine data", response.getBody());
    }

    @Test
    public void testUploadProfilePictureSuccess() throws Exception {
        // Arrange
        Long userId = 1L;
        byte[] imageBytes = "test-image".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);
        Machine user = new Machine();
        user.setId(userId);

        when(machineRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(machineRepository.save(any())).thenReturn(user);

        // Act
        ResponseEntity<?> response = machineController.uploadProfilePicture(userId, mockMultipartFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile picture uploaded successfully.", response.getBody());
        assertArrayEquals(imageBytes, user.getProfilePicture());
    }

    @Test
    public void testUploadProfilePictureInvalidImageFormat() throws Exception {
        // Arrange
        Long userId = 1L;
        byte[] imageBytes = "test-image".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", imageBytes);
        Machine user = new Machine();
        user.setId(userId);

        when(machineRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> response = machineController.uploadProfilePicture(userId, mockMultipartFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid image format.", response.getBody());
        assertNull(user.getProfilePicture());
    }

    @Test
    public void testUploadProfilePictureUserNotFound() throws Exception {
        // Arrange
        Long userId = 1L;
        byte[] imageBytes = "test-image".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);

        when(machineRepository.findById(eq(userId))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = machineController.uploadProfilePicture(userId, mockMultipartFile);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteProfilePictureSuccess() {
        // Arrange
        Long userId = 1L;
        Machine user = new Machine();
        user.setId(userId);
        user.setProfilePicture("test-image".getBytes());

        when(machineRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(machineRepository.save(any())).thenReturn(user);

        // Act
        ResponseEntity<?> response = machineController.deleteProfilePicture(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile picture deleted successfully.", response.getBody());
        assertNull(user.getProfilePicture());
    }

    @Test
    public void testDeleteProfilePictureNoPictureFound() {
        // Arrange
        Long userId = 1L;
        Machine user = new Machine();
        user.setId(userId);

        when(machineRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> response = machineController.deleteProfilePicture(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile picture not found.", response.getBody());
        assertNull(user.getProfilePicture());
    }

    @Test
    public void testDeleteProfilePictureUserNotFound() {
        // Arrange
        Long userId = 1L;

        when(machineRepository.findById(eq(userId))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = machineController.deleteProfilePicture(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
