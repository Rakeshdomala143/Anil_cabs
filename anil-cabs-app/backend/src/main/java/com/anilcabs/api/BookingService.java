package com.anilcabs.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public BookingSummary createBooking(BookingRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking request is required");
        }

        String fullName = normalizeText(request.fullName());
        String mobileNumber = normalizeText(request.mobileNumber());
        String pickupPoint = normalizeText(request.pickupPoint());
        String destination = normalizeText(request.destination());
        String dropPoint = normalizeText(request.dropPoint());
        String travelDate = normalizeText(request.travelDate());
        String travelTime = normalizeText(request.travelTime());
        String passengers = normalizeText(request.passengers());
        String tripType = normalizeText(request.tripType());
        String notes = normalizeText(request.notes());

        if (fullName.isEmpty() || mobileNumber.isEmpty() || pickupPoint.isEmpty() || destination.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All required booking fields must be supplied");
        }

        if (!mobileNumber.matches("^[0-9]{10}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mobile number must contain exactly 10 digits");
        }

        if (travelDate != null && !travelDate.isBlank()) {
            validateDate(travelDate);
        }

        if (travelTime != null && !travelTime.isBlank()) {
            validateTime(travelTime);
        }

        Booking booking = new Booking();
        booking.setFullName(fullName);
        booking.setMobileNumber(mobileNumber);
        booking.setPickupPoint(pickupPoint);
        booking.setDestination(destination);
        booking.setDropPoint(dropPoint);
        booking.setTravelDate(travelDate);
        booking.setTravelTime(travelTime);
        booking.setPassengers(passengers);
        booking.setTripType(tripType);
        booking.setNotes(notes);
        booking.setBookingCode(generateUniqueBookingCode());

        Booking saved = repository.save(booking);
        return BookingSummary.from(saved);
    }

    public BookingSummary getBooking(String bookingCode) {
        if (bookingCode == null || bookingCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking ID is required");
        }

        Booking booking = repository.findByBookingCode(bookingCode.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        return BookingSummary.from(booking);
    }

    private String generateUniqueBookingCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String suffix;
        do {
            suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        } while (repository.findByBookingCode("ANIL-" + datePart + "-" + suffix).isPresent());

        return "ANIL-" + datePart + "-" + suffix;
    }

    private void validateDate(String dateText) {
        try {
            LocalDate parsed = LocalDate.parse(dateText);
            if (parsed.isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Travel date cannot be in the past");
            }
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Travel date must be in YYYY-MM-DD format");
        }
    }

    private void validateTime(String timeText) {
        try {
            LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Travel time must be in HH:mm format");
        }
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}
