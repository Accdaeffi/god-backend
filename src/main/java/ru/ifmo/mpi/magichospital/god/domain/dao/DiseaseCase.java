package ru.ifmo.mpi.magichospital.god.domain.dao;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "disease_case")
public class DiseaseCase {
	
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;
        
    @ManyToOne(targetEntity = Healer.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_id")
    private Healer healer;
    
    @Column(name = "patient_complaints")
    private String patientComplaints;
    
    @Column(name = "registration_time")
    private LocalDateTime registrationTime;
    
    @ManyToOne(targetEntity = Disease.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "disease_id")
    private Disease disease;
 
    @Column(name = "actions")
    private String actions;    

}