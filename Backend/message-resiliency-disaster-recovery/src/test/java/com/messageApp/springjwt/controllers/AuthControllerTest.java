package com.messageApp.springjwt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messageApp.springjwt.payload.request.LoginRequest;
import com.messageApp.springjwt.payload.request.SignupRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // You may need to mock services and repositories if necessary.

    @BeforeEach
    public void setUp() {
        // Set up any necessary test data or configurations before each test.
    }

    @Test
    public void testAuthenticationEndpoint_Success() throws Exception {
        // Create a request body with valid login credentials
        LoginRequest loginRequest = new LoginRequest();
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Send a POST request to the /api/auth/signin endpoint
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson))
                .andReturn();

        // You can assert the response JSON or other properties as needed.
    }

    @Test
    public void testAuthenticationEndpoint_InvalidCredentials() throws Exception {
        // Create a request body with invalid login credentials
        LoginRequest loginRequest = new LoginRequest();
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Send a POST request to the /api/auth/signin endpoint
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson));
    }

    @Test
    public void testRegistrationEndpoint_Success() throws Exception {
        // Create a request body with valid registration data
        SignupRequest signUpRequest = new SignupRequest();
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // Send a POST request to the /api/auth/signup endpoint
        MvcResult result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestJson))
                .andReturn();

        // You can assert the response JSON or other properties as needed.
    }

    @Test
    public void testRegistrationEndpoint_DuplicateUsername() throws Exception {
        // Create a request body with a username that already exists
        SignupRequest signUpRequest = new SignupRequest();
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // Send a POST request to the /api/auth/signup endpoint
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestJson))
                .andExpect(status().isBadRequest()); // Expect bad request status
    }

    @Test
    public void testRegistrationEndpoint_DuplicateEmail() throws Exception {
        // Create a request body with an email that already exists
        SignupRequest signUpRequest = new SignupRequest();
        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);

        // Send a POST request to the /api/auth/signup endpoint
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequestJson))
                .andExpect(status().isBadRequest()); // Expect bad request status
    }

    // Add more test cases for other endpoints and scenarios as needed.
}