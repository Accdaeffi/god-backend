package ru.ifmo.mpi.magichospital.god.domain.dto;

import java.time.LocalDateTime;

import lombok.Data;
import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;

@Data
public class PrayerDTO {

	private int prayerId;
    private HealerDTO healer;
    private PatientDTO patient; 
    private String diseaseName;
    private String infoMsg;    
    private LocalDateTime time;    
    private String prayerStatus;
    
    public PrayerDTO(Prayer prayer) {
    	this.prayerId = prayer.getId();
    	this.time = prayer.getTime();
    	this.prayerStatus = prayer.getStatus().getCode();
    	this.infoMsg = prayer.getText();
    	this.patient = new PatientDTO(prayer.getDiseaseCase().getPatient());
    	this.healer = new HealerDTO(prayer.getDiseaseCase().getHealer());
    	this.diseaseName = prayer.getDiseaseCase().getDisease().getTitle();
    }
}
