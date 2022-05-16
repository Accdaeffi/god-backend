package ru.ifmo.mpi.magichospital.god.domain.dto;

import lombok.Getter;
import ru.ifmo.mpi.magichospital.god.domain.dao.Healer;

@Getter
public class HealerDTO {

    private String name;
    private String surname;
    private boolean isMale;
    private String socialStatus;
    
    public HealerDTO(Healer healer) {
    	this.name = healer.getName();
    	this.surname = healer.getSurname();
    	this.socialStatus = healer.getSocialStatus().getName();
    	this.isMale = healer.isMale();
    }
	
}
