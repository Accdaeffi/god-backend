package ru.ifmo.mpi.magichospital.god.domain.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "disease_dict")
public class Disease {
	
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name = "title")
	private String title;
    
	@Column(name = "symptoms")
	private String symptoms;
	
	@Column(name = "recipe")
	private String recipe;

}