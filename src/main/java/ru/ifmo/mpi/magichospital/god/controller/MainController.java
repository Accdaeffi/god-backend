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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ru.ifmo.mpi.magichospital.god.domain.dao.Prayer;
import ru.ifmo.mpi.magichospital.god.domain.dto.PrayerDTO;
import ru.ifmo.mpi.magichospital.god.domain.dto.PrayerListDTO;
import ru.ifmo.mpi.magichospital.god.exception.DictContentException;
import ru.ifmo.mpi.magichospital.god.exception.MeaninglessDataException;
import ru.ifmo.mpi.magichospital.god.exception.NotFoundException;
import ru.ifmo.mpi.magichospital.god.exception.PossibleSqlInjectionAttackException;
import ru.ifmo.mpi.magichospital.god.exception.PrayerAlreadyAnsweredException;
import ru.ifmo.mpi.magichospital.god.service.PrayerService;

@RestController
public class MainController {
	
	private static final String API_PREFIX = "/api/v1";
	private static final String GOD_PREFIX = "/god";
	
	@Autowired
	PrayerService prayerService;

	@Operation(summary = "Get list of all prayers")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Get list", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = PrayerListDTO.class)) }),
			  @ApiResponse(responseCode = "400", description = "Incorrect values (SQL injection, for example). Full description in \"message\" field", 
			    content = @Content),
			  @ApiResponse(responseCode = "401", description = "Trying to get list of another god", 
			    content = @Content),
			  @ApiResponse(responseCode = "404", description = "No such god", 
			    content = @Content),
			  @ApiResponse(responseCode = "500", description = "I really don't know how this can happen, so, if you get this exception - just try to output \"message\" field", 
			    content = @Content)})
	@GetMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers")
	public PrayerListDTO getPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException, PossibleSqlInjectionAttackException {
		if (principal.getName().equals(login)) {
			List<Prayer> prayers = prayerService.getPrayers(login);		
			return new PrayerListDTO(convertPrayerListToPrayerDTOList(prayers));
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@Operation(summary = "Get list of unanswered prayers")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Get list", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = PrayerListDTO.class)) }),
			  @ApiResponse(responseCode = "400", description = "Incorrect values (SQL injection, for example). Full description in \"message\" field", 
			    content = @Content),
			  @ApiResponse(responseCode = "401", description = "Trying to get list of another god", 
			    content = @Content),
			  @ApiResponse(responseCode = "404", description = "No such god", 
			    content = @Content),
			  @ApiResponse(responseCode = "500", description = "Inner database exception. Description in \"message\" field.", 
			    content = @Content)})
	@GetMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers/unanswered")
	public PrayerListDTO getUnansweredPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException, DictContentException, PossibleSqlInjectionAttackException {
		if (principal.getName().equals(login)) {
			
			List<Prayer> prayers = prayerService.getUnansweredPrayers(login);		
			return new PrayerListDTO(convertPrayerListToPrayerDTOList(prayers));
			
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@Operation(summary = "Get last unanswered prayer")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Get one prayer", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = PrayerDTO.class)) }),
			  @ApiResponse(responseCode = "400", description = "Incorrect values (SQL injection, for example). Full description in \"message\" field", 
			    content = @Content),
			  @ApiResponse(responseCode = "401", description = "Trying to get list of another god", 
			    content = @Content),
			  @ApiResponse(responseCode = "404", description = "No such god", 
			    content = @Content),
			  @ApiResponse(responseCode = "500", description = "Inner database exception. Description in \"message\" field.", 
			    content = @Content)})
	@GetMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers/unanswered/last")
	public PrayerDTO getLastUnansweredPrayers(@PathVariable String login, Principal principal) 
			throws NotFoundException, DictContentException, PossibleSqlInjectionAttackException {
		if (principal.getName().equals(login)) {
			
			Prayer prayer = prayerService.getLastUnansweredPrayer(login);
			if (prayer != null) {
				return new PrayerDTO(prayer);
			} else {
				return null;
			}
			
		} else {
			throw new SecurityException("Forbidden");
		}
	}
	
	@Operation(summary = "Update status of prayer")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Get list", 
			    content = @Content),
			  @ApiResponse(responseCode = "400", description = "Incorrect values (SQL injection, for example). Full description in \"message\" field", 
			    content = @Content),
			  @ApiResponse(responseCode = "401", description = "Trying to answer prayer of another god", 
			    content = @Content),
			  @ApiResponse(responseCode = "404", description = "No such god", 
			    content = @Content),
			  @ApiResponse(responseCode = "500", description = "Inner exception - like no such prayer status, or prayer already answered, or you trying to make strange prayer status. Description in \"message\" field.", 
			    content = @Content)})
	@PostMapping(API_PREFIX+GOD_PREFIX+"/{login}/prayers/{prayerId}/status/{statusId}")
	public ResponseEntity<?> updatePrayerStatus(@PathVariable String login, 
			@PathVariable int prayerId, 
			@PathVariable int statusId,
			Principal principal) 
					throws SecurityException, PrayerAlreadyAnsweredException, NotFoundException, MeaninglessDataException, PossibleSqlInjectionAttackException {
		
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
