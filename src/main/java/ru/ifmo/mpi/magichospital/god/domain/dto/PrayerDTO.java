package ru.ifmo.mpi.magichospital.god.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;

@Getter
public class PrayerDTO {

    private HealerDTO healer;
    private PatientDTO patient;    
    private String text;    
    private LocalDateTime time;    
    private String prayerStatus;
    
    public PrayerDTO(Prayer prayer) {
    	this.time = prayer.getTime();
    	this.prayerStatus = prayer.getStatus().getName();
    	this.text = prayer.getText();
    	this.patient = new PatientDTO(prayer.getPatient());
    	this.healer = new HealerDTO(prayer.getHealer());
    }
}
