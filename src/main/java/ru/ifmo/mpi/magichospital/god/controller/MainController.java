package ru.ifmo.mpi.magichospital.god.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;
import ru.ifmo.mpi.magichospital.god.exception.DictContentException;
import ru.ifmo.mpi.magichospital.god.exception.NotFoundException;
import ru.ifmo.mpi.magichospital.god.exception.PrayerAlreadyAnsweredException;
import ru.ifmo.mpi.magichospital.god.service.PrayerService;

@RestController
public class MainController {
	
	@Autowired
	PrayerService prayerService;

	@GetMapping("/god/{login}/prayers")
	public List<Prayer> getPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException {
		if (principal.getName().equals(login)) {
			return prayerService.getPrayers(login);
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@GetMapping("/god/{login}/prayers/unanswered")
	public List<Prayer> getUnansweredPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException, DictContentException {
		if (principal.getName().equals(login)) {
			return prayerService.getUnansweredPrayers(login);
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@PostMapping("/god/{login}/prayers/{prayerId}/status")
	public void updatePrayerStatus(@PathVariable String login, 
			@PathVariable int prayerId, 
			@RequestBody int status,
			Principal principal) 
					throws SecurityException, PrayerAlreadyAnsweredException, NotFoundException {
		if (principal.getName().equals(login)) {
			prayerService.setPrayerStatus(login, prayerId, status);
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
}
