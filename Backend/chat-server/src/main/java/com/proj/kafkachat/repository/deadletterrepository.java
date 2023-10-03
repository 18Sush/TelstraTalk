package com.proj.kafkachat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proj.kafkachat.model.DeadLetterMessage;

import org.springframework.stereotype.Repository;

@Repository
public interface deadletterrepository extends JpaRepository<DeadLetterMessage, Long>  {


	List<DeadLetterMessage> findBySender(String sender);

		


	//void save(com.proj.kafkachatserver.models.DeadLetterMessage deadLetterMessage);


	//List<com.proj.kafkachatserver.models.DeadLetterMessage> findBySender(String sender);
	


}