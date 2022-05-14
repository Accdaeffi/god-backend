package ru.ifmo.mpi.magichospital.god.domain.dto;

import lombok.Data;
import ru.ifmo.mpi.magichospital.god.domain.dao.Patient;

@Data
public class PatientDTO {

    private String name;    
    private String surname;
    private boolean isMale;    
    private boolean isMage;
    private String socialStatus;
    
    public PatientDTO(Patient patient) {
    	this.name = patient.getName();
    	this.surname = patient.getSurname();
    	this.socialStatus = patient.getSocialStatus().getName();
    	this.isMale = patient.isMale();
    	this.isMage = patient.isMage();
    }
}
