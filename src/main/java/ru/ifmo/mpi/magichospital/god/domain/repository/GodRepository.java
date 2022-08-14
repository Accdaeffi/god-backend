package ru.ifmo.mpi.magichospital.god.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.mpi.magichospital.god.domain.dao.God;

@Repository
public interface GodRepository extends JpaRepository<God, Integer> {
	Optional<God> findByLogin(String login);
}
