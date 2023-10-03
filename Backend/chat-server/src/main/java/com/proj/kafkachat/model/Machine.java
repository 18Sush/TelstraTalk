package com.proj.kafkachat.model;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ipAddress;
    private boolean online; // New field for machine status
    
    @Lob
    private byte[] profilePicture;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Machine() {
        // Default constructor
    }

	@Override
	public String toString() {
		return "Machine [id=" + id + ", name=" + name + ", ipAddress=" + ipAddress + ", online=" + online
				+ ", profilePicture=" + Arrays.toString(profilePicture) + "]";
	}

	public Machine(Long id, String name, String ipAddress, boolean online, byte[] profilePicture) {
		super();
		this.id = id;
		this.name = name;
		this.ipAddress = ipAddress;
		this.online = online;
		this.profilePicture = profilePicture;
	}
	

}