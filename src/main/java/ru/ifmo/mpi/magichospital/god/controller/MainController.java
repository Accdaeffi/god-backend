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
import ru.ifmo.mpi.magichospital.god.exception.DictContentException;
import ru.ifmo.mpi.magichospital.god.exception.MeaninglessDataException;
import ru.ifmo.mpi.magichospital.god.exception.NotFoundException;
import ru.ifmo.mpi.magichospital.god.exception.PrayerAlreadyAnsweredException;
import ru.ifmo.mpi.magichospital.god.service.PrayerService;

@RestController
public class MainController {
	
	@Autowired
	PrayerService prayerService;

	@GetMapping("/god/{login}/prayers")
	public List<PrayerDTO> getPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException {
		if (principal.getName().equals(login)) {
			
			List<Prayer> prayers = prayerService.getPrayers(login);		
			return convertPrayerListToPrayerDTOList(prayers);
			
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@GetMapping("/god/{login}/prayers/unanswered")
	public List<PrayerDTO> getUnansweredPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException, DictContentException {
		if (principal.getName().equals(login)) {
			
			List<Prayer> prayers = prayerService.getUnansweredPrayers(login);		
			return convertPrayerListToPrayerDTOList(prayers);
			
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@PostMapping("/god/{login}/prayers/{prayerId}/status/{statusId}")
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
