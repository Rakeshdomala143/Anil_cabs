package com.anilcabs.api;

import java.time.LocalDateTime;

public record BookingSummary(
        String bookingCode,
        String fullName,
        String mobileNumber,
        String pickupPoint,
        String destination,
        String dropPoint,
        String travelDate,
        String travelTime,
        String passengers,
        String tripType,
        String notes,
        String status,
        LocalDateTime createdAt
) {
    public static BookingSummary from(Booking booking) {
        return new BookingSummary(
                booking.getBookingCode(),
                booking.getFullName(),
                booking.getMobileNumber(),
                booking.getPickupPoint(),
                booking.getDestination(),
                booking.getDropPoint(),
                booking.getTravelDate(),
                booking.getTravelTime(),
                booking.getPassengers(),
                booking.getTripType(),
                booking.getNotes(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}
