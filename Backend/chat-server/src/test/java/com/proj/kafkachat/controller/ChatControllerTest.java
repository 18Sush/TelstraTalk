package com.proj.kafkachat.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.proj.kafkachat.model.Message;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.FileAppender;

public class ChatControllerTest {

   

    @Test
    public void testDownloadRecentFileWithNoRecentMessage() {
        // Arrange
        ChatController chatController = new ChatController(null, null);

        // Act
        ResponseEntity<byte[]> response = chatController.downloadRecentFile();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void testKafkaConsumerFileAppenderConfiguration() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger("com.proj.kafkachatserver.controllers.ChatController");
        FileAppender<?> appender = (FileAppender<?>) ((ch.qos.logback.classic.Logger) logger).getAppender("KafkaConsumerFileAppender");

        assertNotNull(appender);
        assertEquals("C:\\kafka\\recoveryfiles\\disaster2.txt", appender.getFile());
        assertTrue(appender.isAppend());
        // Add more assertions as needed for encoder and pattern
    }
    
    @Test
    public void testChatControllerLoggerLevel() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger("com.proj.kafkachatserver.controllers.ChatController");

        assertNotNull(logger);
        assertEquals("INFO", ((ch.qos.logback.classic.Logger) logger).getLevel().toString());
    }
    
    @Test
    public void testRootLoggerConfiguration() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        assertNotNull(rootLogger);
        assertEquals("INFO", rootLogger.getLevel().toString());
        // Add assertions for appender references if needed
    }

    
}
