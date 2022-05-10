package ru.ifmo.mpi.magichospital.god.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.mpi.magichospital.god.domain.dao.LoginInfo;

@Repository
public interface LoginInfoRepository extends CrudRepository<LoginInfo, Integer> {
	Optional<LoginInfo> findByLogin(String login);
}
