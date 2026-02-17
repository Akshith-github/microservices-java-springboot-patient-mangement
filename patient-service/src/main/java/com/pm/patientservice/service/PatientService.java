package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    public PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(PatientMapper::toDTO).toList();
        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists "+patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        //email address must be unique
        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID patientID, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(patientID).orElseThrow(
                ()->new PatientNotFoundException("Patient Not found Exception with ID " + patientID)
        );

        // prevent updating patient with other patient email
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),  patientID)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists "+patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patientRepository.save(patient);

        return PatientMapper.toDTO(patient);
    }

    public void deletePatient(UUID patientID) {
        patientRepository.deleteById(patientID);
    }
}
