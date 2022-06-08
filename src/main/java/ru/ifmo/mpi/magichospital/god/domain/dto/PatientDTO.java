package ru.ifmo.mpi.magichospital.god.domain.dto;

import lombok.Data;
import ru.ifmo.mpi.magichospital.god.domain.dao.Patient;

@Data
public class PatientDTO {

    private String patientName;    
    private String patientSurname;
    private String patientPatronymic;
    private boolean isMale;    
    private boolean isMage;
    private String socialStatus;
    
    public PatientDTO(Patient patient) {
    	this.patientName = patient.getName();
    	this.patientSurname = patient.getSurname();
    	this.patientPatronymic = patient.getPatronymic();
    	this.socialStatus = patient.getSocialStatus().getName();
    	this.isMale = patient.isMale();
    	this.isMage = patient.isMage();
    }
}
