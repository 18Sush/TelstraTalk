package com.proj.kafkachat.model;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.web.multipart.MultipartFile;


@Entity
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String sender;
    private String receiver;
    private String content;
    private String timestamp;
    private MessageType type;
    private byte[] fileContent; // for files
    private String fileName; // original file name
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", content=" + content
				+ ", timestamp=" + timestamp + ", type=" + type + ", fileContent=" + Arrays.toString(fileContent)
				+ ", fileName=" + fileName + "]";
	}
	public Message(Long id, String sender, String receiver, String content, String timestamp, MessageType type,
			byte[] fileContent, String fileName) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
		this.type = type;
		this.fileContent = fileContent;
		this.fileName = fileName;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Message(String sender2, String receiver2, MessageType normal, String content2, byte[] fileContent2, long currentTimeMillis) {
		// TODO Auto-generated constructor stub
	}
	public Message(String sender2, String receiver2, String content2, byte[] fileContent2, long currentTimeMillis) {
		// TODO Auto-generated constructor stub
	}
	public Message(String sender2, String receiver2, String content2, String fileName2, MessageType type2,
			byte[] fileContent2) {
		// TODO Auto-generated constructor stub
	}
	
    
    
}
    
    
   