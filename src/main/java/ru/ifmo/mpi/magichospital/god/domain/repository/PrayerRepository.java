package ru.ifmo.mpi.magichospital.god.domain.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;

@Repository
public interface PrayerRepository extends CrudRepository<Prayer, Integer> {
	List<Prayer> findByGodId(int godId);
	List<Prayer> findByGodIdAndStatusId(int godId, int statusId);
}
