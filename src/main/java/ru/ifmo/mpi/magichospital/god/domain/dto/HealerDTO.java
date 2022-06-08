package ru.ifmo.mpi.magichospital.god.domain.dto;

import lombok.Data;
import ru.ifmo.mpi.magichospital.god.domain.dao.Healer;

@Data
public class HealerDTO {

    private String healerName;
    private String healerSurname;
    private boolean isMale;
    private String socialStatus;
    
    public HealerDTO(Healer healer) {
    	this.healerName = healer.getName();
    	this.healerSurname = healer.getSurname();
    	this.socialStatus = healer.getSocialStatus().getName();
    	this.isMale = healer.isMale();
    }
	
}
