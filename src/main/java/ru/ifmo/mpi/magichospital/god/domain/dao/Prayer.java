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
import ru.ifmo.mpi.magichospital.god.domain.dao.dict.PrayerStatus;

@Entity
@Data
@Table(name = "prayer")
public class Prayer {
	
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @ManyToOne(targetEntity = Healer.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_id")
    private Healer healer;
    
    @ManyToOne(targetEntity = God.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "god_id")
    private God god;
    
    @ManyToOne(targetEntity = Patient.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @Column(name = "pray_text")
    private String text;
    
    @Column(name = "pray_time")
    private LocalDateTime time;
    
    @ManyToOne(targetEntity = PrayerStatus.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private PrayerStatus status;

}
