package com.proj.kafkachat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proj.kafkachat.model.Machine;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    
    @Query("SELECT COUNT(m) > 0 FROM Machine m WHERE m.name = :name OR m.ipAddress = :ipAddress")
    boolean existsByNameOrIpAddress(String name, String ipAddress);

    @Query("SELECT m FROM Machine m WHERE m.name = :name AND m.ipAddress = :ipAddress")
    Optional<Machine> findByNameAndIpAddress(String name, String ipAddress);

	List<Machine> findByOnline(boolean b);

	

}