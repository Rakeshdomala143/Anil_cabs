package com.anilcabs.api;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {
	private final BookingRepository bookingRepository;

	public HealthController(BookingRepository bookingRepository) {
		this.bookingRepository = bookingRepository;
	}

	@GetMapping("/health")
	public ResponseEntity<Map<String, String>> health() {
		try {
			bookingRepository.count();
			return ResponseEntity.ok(Map.of("status", "UP", "app", "Anil Cabs API"));
		} catch (DataAccessException exception) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(Map.of("status", "DOWN", "app", "Anil Cabs API"));
		}
	}
}
