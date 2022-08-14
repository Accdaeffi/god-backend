package ru.ifmo.mpi.magichospital.god.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.mpi.magichospital.god.domain.dao.dict.PrayerStatus;

@Repository
public interface PrayerStatusRepository extends JpaRepository<PrayerStatus, Integer> {
	Optional<PrayerStatus> findByCode(String code);
}
