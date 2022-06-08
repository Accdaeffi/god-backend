package ru.ifmo.mpi.magichospital.god.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.rkpunjal.sqlsafe.SqlSafeUtil;

import ru.ifmo.mpi.magichospital.god.domain.dao.God;
import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;
import ru.ifmo.mpi.magichospital.god.domain.dao.dict.PrayerStatus;
import ru.ifmo.mpi.magichospital.god.domain.repository.GodRepository;
import ru.ifmo.mpi.magichospital.god.domain.repository.PrayerRepository;
import ru.ifmo.mpi.magichospital.god.domain.repository.PrayerStatusRepository;
import ru.ifmo.mpi.magichospital.god.exception.DictContentException;
import ru.ifmo.mpi.magichospital.god.exception.MeaninglessDataException;
import ru.ifmo.mpi.magichospital.god.exception.NotFoundException;
import ru.ifmo.mpi.magichospital.god.exception.PossibleSqlInjectionAttackException;
import ru.ifmo.mpi.magichospital.god.exception.PrayerAlreadyAnsweredException;

@Service
public class PrayerService {
	
	GodRepository godRepository;
	PrayerRepository prayerRepository;
	PrayerStatusRepository prayerStatusRepository;
	
	public PrayerService(GodRepository godRepository, 
			PrayerRepository prayerRepository, 
			PrayerStatusRepository prayerStatusRepository) {
		this.godRepository = godRepository;
		this.prayerRepository = prayerRepository;
		this.prayerStatusRepository = prayerStatusRepository;
	}

	public List<Prayer> getPrayers(String login) 
			throws NotFoundException, PossibleSqlInjectionAttackException {
		
		if (!SqlSafeUtil.isSqlInjectionSafe(login)) {
            throw new PossibleSqlInjectionAttackException("Possible sql injection attack!");
        }
		
		Optional<God> optionalGod = godRepository.findByLogin(login);
        if (optionalGod.isPresent()) {
        	return prayerRepository.findByGodId(optionalGod.get().getId());
        } else {
        	throw new NotFoundException("Not found!");
        }
	}
	
	public List<Prayer> getUnansweredPrayers(String login) 
			throws NotFoundException, DictContentException, PossibleSqlInjectionAttackException {
		
		if (!SqlSafeUtil.isSqlInjectionSafe(login)) {
            throw new PossibleSqlInjectionAttackException("Possible sql injection attack!");
        }
		
		Optional<God> optionalGod = godRepository.findByLogin(login);
        if (optionalGod.isPresent()) {
        	PrayerStatus newStatus = prayerStatusRepository.findByCode(PrayerStatus.CODE_NEW)
        			.orElseThrow(DictContentException::new);
        	
        	return prayerRepository.findByGodIdAndStatusId(optionalGod.get().getId(), newStatus.getId());
        } else {
        	throw new NotFoundException("Not found!");
        }
	}
	

	public Prayer getLastUnansweredPrayer(String login) 
			throws NotFoundException, DictContentException, PossibleSqlInjectionAttackException {
		List<Prayer> prayers = getUnansweredPrayers(login);
		
		if (prayers == null || prayers.size() == 0) {
			return null;
		} else {
			return getUnansweredPrayers(login).get(0);
		}
	}

	public void setPrayerStatus(String login, int prayerId, String statusCode) 
			throws SecurityException, PrayerAlreadyAnsweredException, NotFoundException, MeaninglessDataException, PossibleSqlInjectionAttackException {
		
		if (!SqlSafeUtil.isSqlInjectionSafe(login)) {
            throw new PossibleSqlInjectionAttackException("Possible sql injection attack!");
        }
		
		Optional<God> optionalGod = godRepository.findByLogin(login);
		Optional<Prayer> optionalPrayer = prayerRepository.findById(prayerId);
		Optional<PrayerStatus> optionalStatus = prayerStatusRepository.findByCode(statusCode);
		
        if (optionalGod.isPresent() && 
        		optionalPrayer.isPresent() && 
        		optionalStatus.isPresent()) {
        	
        	God god = optionalGod.get();
        	Prayer prayer = optionalPrayer.get();
        	PrayerStatus status = optionalStatus.get(); 
        	
        	if (!status.getCode().equals(PrayerStatus.CODE_NEW)) {
            	if (god.equals(prayer.getGod())) {
            		if (prayer.getStatus().getCode().equals(PrayerStatus.CODE_NEW)) {
                		prayer.setStatus(status);
                		prayerRepository.save(prayer);
            		} else {
            			throw new PrayerAlreadyAnsweredException("We already received answer for this prayer!");
            		}
            	} else {
            		throw new SecurityException("Forbidden");
            	}
        	} else {
        		throw new MeaninglessDataException("You can't set \"new\" status!");
        	}
        } else {
        	throw new NotFoundException("Not found!");
        }
        
		
	}

}
