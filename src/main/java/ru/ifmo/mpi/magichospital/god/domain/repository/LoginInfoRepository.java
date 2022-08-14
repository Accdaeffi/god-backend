package ru.ifmo.mpi.magichospital.god.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ifmo.mpi.magichospital.god.domain.dao.LoginInfo;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, String> {
	Optional<LoginInfo> findByLogin(String login);
}
