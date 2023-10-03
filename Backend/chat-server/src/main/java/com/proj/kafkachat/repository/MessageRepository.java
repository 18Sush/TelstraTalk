package com.proj.kafkachat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proj.kafkachat.model.Message;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Define custom query methods if needed
	
}
