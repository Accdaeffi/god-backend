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
import javax.persistence.OneToOne;
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
    
    @ManyToOne(targetEntity = God.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "god_id")
    private God god;
    
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "prayer_id")
    private DiseaseCase diseaseCase;
    
    @Column(name = "pray_text")
    private String text;
    
    @Column(name = "pray_time")
    private LocalDateTime time;
    
    @ManyToOne(targetEntity = PrayerStatus.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private PrayerStatus status;

}
