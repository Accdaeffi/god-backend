package ru.ifmo.mpi.magichospital.god.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;

import java.util.List;

@Repository
public interface PrayerRepository extends JpaRepository<Prayer, Integer> {
	List<Prayer> findByGodId(int godId);
	List<Prayer> findByGodIdAndStatusId(int godId, int statusId);
}
