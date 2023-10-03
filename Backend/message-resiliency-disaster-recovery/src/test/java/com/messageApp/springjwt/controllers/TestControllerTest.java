package com.messageApp.springjwt.controllers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    public void testUserAccess() throws Exception {
        mockMvc.perform(get("/api/test/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User Content."));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testModeratorAccess() throws Exception {
        mockMvc.perform(get("/api/test/mod")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Moderator Board."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminAccess() throws Exception {
        mockMvc.perform(get("/api/test/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin Board."));
    }

    @Test
    public void testAllAccess() throws Exception {
        mockMvc.perform(get("/api/test/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Public Content."));
    }
}