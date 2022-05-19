package ru.ifmo.mpi.magichospital.god.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;
import ru.ifmo.mpi.magichospital.god.domain.dto.PrayerDTO;
import ru.ifmo.mpi.magichospital.god.domain.dto.PrayerListDTO;
import ru.ifmo.mpi.magichospital.god.exception.DictContentException;
import ru.ifmo.mpi.magichospital.god.exception.MeaninglessDataException;
import ru.ifmo.mpi.magichospital.god.exception.NotFoundException;
import ru.ifmo.mpi.magichospital.god.exception.PrayerAlreadyAnsweredException;
import ru.ifmo.mpi.magichospital.god.service.PrayerService;

@RestController
public class MainController {
	
	private static final String API_PREFIX = "/api/v1";
	private static final String GOD_PREFIX = "/god";
	
	@Autowired
	PrayerService prayerService;

	@GetMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers")
	public PrayerListDTO getPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException {
		if (principal.getName().equals(login)) {
			
			List<Prayer> prayers = prayerService.getPrayers(login);		
			return new PrayerListDTO(convertPrayerListToPrayerDTOList(prayers));
			
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@GetMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers/unanswered")
	public PrayerListDTO getUnansweredPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException, DictContentException {
		if (principal.getName().equals(login)) {
			
			List<Prayer> prayers = prayerService.getUnansweredPrayers(login);		
			return new PrayerListDTO(convertPrayerListToPrayerDTOList(prayers));
			
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@PostMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers/{prayerId}/status/{statusId}")
	public ResponseEntity<?> updatePrayerStatus(@PathVariable String login, 
			@PathVariable int prayerId, 
			@PathVariable int statusId,
			Principal principal) 
					throws SecurityException, PrayerAlreadyAnsweredException, NotFoundException, MeaninglessDataException {
		
		if (principal.getName().equals(login)) {
			prayerService.setPrayerStatus(login, prayerId, statusId);
			return ResponseEntity.ok().build();
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	private List<PrayerDTO> convertPrayerListToPrayerDTOList(List<Prayer> prayers) {
		List<PrayerDTO> prayerDTOs =  prayers.stream()
				.map(prayer -> new PrayerDTO(prayer))
				.collect(Collectors.toList());
			
			return prayerDTOs;
	}
	
}
