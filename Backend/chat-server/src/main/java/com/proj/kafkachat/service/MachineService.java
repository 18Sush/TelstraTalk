package com.proj.kafkachat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.kafkachat.model.Machine;
import com.proj.kafkachat.repository.MachineRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MachineService {
    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }
    
    public List<Machine> getOnlineMachines() {
        return machineRepository.findByOnline(true);
    }
    
    public Machine registerMachine(Machine machine) {
        if (machine.getName() == null || machine.getIpAddress() == null) {
            throw new IllegalArgumentException("Machine name and IP address are required.");
        }
        if (machineRepository.existsByNameOrIpAddress(machine.getName(), machine.getIpAddress())) {
            Machine existingMachine = machineRepository.findByNameAndIpAddress(machine.getName(), machine.getIpAddress())
                    .orElseThrow(() -> new IllegalArgumentException("Machine with the same name or IP address already exists."));
           /* if (!existingMachine.isOnline()) {
                existingMachine.setOnline(true);
                machineRepository.save(existingMachine);
            }
            return existingMachine;
        }

        machine.setOnline(true);

        return machineRepository.save(machine);
    }*/
            existingMachine.setOnline(true);
            machineRepository.save(existingMachine);
            
            return existingMachine;
        }

        // Set the new machine as online
        machine.setOnline(true);

        return machineRepository.save(machine);
    }

    public void unregisterMachine(Long machineId) {
        Optional<Machine> optionalMachine = machineRepository.findById(machineId);
        optionalMachine.ifPresent(machine -> machineRepository.delete(machine));
    }

    public void updateMachineStatus(Long machineId, boolean online) {
        Optional<Machine> optionalMachine = machineRepository.findById(machineId);
        if (optionalMachine.isPresent()) {
            Machine machine = optionalMachine.get();
            machine.setOnline(online);
            machineRepository.save(machine);
        } else {
            throw new IllegalArgumentException("Machine with ID " + machineId + " not found.");
        }
    }

    public List<Machine> getAllMachinesWithStatus() {
        return machineRepository.findAll();
    }
}