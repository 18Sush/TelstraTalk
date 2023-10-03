package com.proj.kafkachat.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeadLetterMessage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String sender;
    private String content;
    private byte[] fileContent;
    private long expirationTimeInMillis;
    private String fileName;
    private String receiver;
	private long timestamp;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public long getExpirationTimeInMillis() {
		return expirationTimeInMillis;
	}
	public void setExpirationTimeInMillis(long expirationTimeInMillis) {
		this.expirationTimeInMillis = expirationTimeInMillis;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public DeadLetterMessage( String sender, String content, byte[] fileContent, long expirationTimeInMillis,
			String fileName) {
		this.sender = sender;
		this.content = content;
		this.fileContent = fileContent;
		this.expirationTimeInMillis = expirationTimeInMillis;
		this.fileName = fileName;
		this.receiver = receiver;
	}
	public DeadLetterMessage() {
	    this.timestamp = System.currentTimeMillis();
	}
	/*public DeadLetterMessage(Object object, String sender2, String receiver2, String content2, String content3,
			MessageType type, Object object2, String content4) {
		// TODO Auto-generated constructor stub
	}*/
	public MessageType getType() {
		return null;
	}
	
	public void setPoison(boolean b) {
		// TODO Auto-generated method stub
		
	}
   
}