package ru.ifmo.mpi.magichospital.god.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rkpunjal.sqlsafe.SqlSafeUtil;

import ru.ifmo.mpi.magichospital.god.domain.dao.LoginInfo;
import ru.ifmo.mpi.magichospital.god.domain.repository.LoginInfoRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
	@Autowired
    private LoginInfoRepository loginInfoRepository;
    
	@Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if (!SqlSafeUtil.isSqlInjectionSafe(userName)) {
            throw new UsernameNotFoundException("Possible sql injection attack!");
        }
		
        Optional<LoginInfo> optionalDbUser = loginInfoRepository.findByLogin(userName);
        
        if (optionalDbUser.isPresent()) {
        	LoginInfo dbUser = optionalDbUser.get();
            UserDetails user = User.builder()
                    .username(dbUser.getLogin())
                    .password(dbUser.getPassword())
                    .roles(dbUser.getRole().getCode())
                    .build();
            return user;
        } else {
        		throw new UsernameNotFoundException("Username: "+userName);
        }
    }
}