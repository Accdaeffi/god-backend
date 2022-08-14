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

	/**
	 * Получение ВСЕХ молитв, относящихся к определённому богу
	 * 
	 * @param login Логин бога
	 * @return Лист молитв
	 * @throws NotFoundException Если такого бога не существует
	 * @throws PossibleSqlInjectionAttackException Если в названии имени бога есть SQL-иньекция
	 */
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
	
	/**
	 * Получение всех неотвеченных молитв, относящихся к определённому богу
	 * 
	 * @param login Логин бога
	 * @return Лист молитв
	 * @throws NotFoundException Если такого бога не существует
	 * @throws PossibleSqlInjectionAttackException Если в названии имени бога есть SQL-иньекция
	 * @throws DictContentException Если кто-то нахимичил в БД и теперь у нас нет статуса с кодом "Новый"

	 */
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
	
	/**
	 * Получение последней неотвеченной молтвы, относящиейся к определённому богу
	 * 
	 * @param login Логин бога
	 * @return Одна молитва
	 * @throws NotFoundException Если такого бога не существует
	 * @throws PossibleSqlInjectionAttackException Если в названии имени бога есть SQL-иньекция
	 * @throws DictContentException Если кто-то нахимичил в БД и теперь у нас нет статуса с кодом "Новый"
	 */
	public Prayer getLastUnansweredPrayer(String login) 
			throws NotFoundException, DictContentException, PossibleSqlInjectionAttackException {
		List<Prayer> prayers = getUnansweredPrayers(login);
		
		if (prayers == null || prayers.size() == 0) {
			return null;
		} else {
			return getUnansweredPrayers(login).get(0);
		}
	}

	/**
	 * Изменяет статус молитвы определённого бога на указанный.
	 * 
	 * @param login Логин бога
	 * @param prayerId id молитвы
	 * @param statusCode Код нового статуса
	 * @throws SecurityException Попытка заменить статус чужой молитвы
	 * @throws PrayerAlreadyAnsweredException Попытка изменить статус молитвы, на которую уже был дан ответ
	 * @throws NotFoundException Если не найдена молитва, бог или нужный статус
	 * @throws MeaninglessDataException Попытка изменить статус на "Новый"
	 * @throws PossibleSqlInjectionAttackException Если в названии имени бога есть SQL-иньекция 
	 */
	public void setPrayerStatus(String login, int prayerId, String statusCode) 
			throws SecurityException, PrayerAlreadyAnsweredException, NotFoundException, MeaninglessDataException, PossibleSqlInjectionAttackException {
		
		if (!SqlSafeUtil.isSqlInjectionSafe(login)) {
            throw new PossibleSqlInjectionAttackException("Possible sql injection attack!");
        }

		System.out.println(login);
		System.out.println(prayerId);
		System.out.println(statusCode);

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
                		prayerRepository.saveAndFlush(prayer);
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
