package ru.ifmo.mpi.magichospital.god.domain.dto;

import lombok.Getter;
import ru.ifmo.mpi.magichospital.god.domain.dao.Patient;

@Getter
public class PatientDTO {

    private String name;    
    private String surname;
    private String patronymic;
    private boolean isMale;    
    private boolean isMage;
    private String socialStatus;
    
    public PatientDTO(Patient patient) {
    	this.name = patient.getName();
    	this.surname = patient.getSurname();
    	this.patronymic = patient.getPatronymic();
    	this.socialStatus = patient.getSocialStatus().getName();
    	this.isMale = patient.isMale();
    	this.isMage = patient.isMage();
    }
}
