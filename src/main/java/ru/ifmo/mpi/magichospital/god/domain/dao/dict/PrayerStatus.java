package ru.ifmo.mpi.magichospital.god.domain.dao.dict;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "prayer_status_dict")
public class PrayerStatus {
	
	public static final String CODE_NEW = "new";
	public static final String CODE_REJECTED = "rejected";
	public static final String CODE_ACCEPTED = "accepted";
	
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name = "name")
	private String name;
    
	@Column(name = "code")
	private String code;

}
