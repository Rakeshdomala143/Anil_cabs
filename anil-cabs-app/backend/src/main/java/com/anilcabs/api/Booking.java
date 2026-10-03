package com.anilcabs.api;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cab_bookings", indexes = @Index(name = "idx_booking_code", columnList = "booking_code", unique = true))
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_code", nullable = false, unique = true, length = 32)
    private String bookingCode;
    @Column(nullable = false, length = 100) private String fullName;
    @Column(nullable = false, length = 10) private String mobileNumber;
    @Column(nullable = false, length = 250) private String pickupPoint;
    @Column(nullable = false, length = 250) private String destination;
    @Column(length = 250) private String dropPoint;
    @Column(length = 30) private String travelDate;
    @Column(length = 10) private String travelTime;
    @Column(nullable = false, length = 30) private String passengers;
    @Column(nullable = false, length = 40) private String tripType;
    @Column(length = 500) private String notes;
    @Column(nullable = false, length = 30) private String status = "PENDING_WHATSAPP_CONFIRMATION";
    @Column(nullable = false) private LocalDateTime createdAt;

    @PrePersist
    void beforeSave() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (bookingCode == null || bookingCode.isBlank()) {
            String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
            bookingCode = "ANIL-" + java.time.LocalDate.now().toString().replace("-", "") + "-" + suffix;
        }
    }

    public UUID getId() { return id; }
    public String getBookingCode() { return bookingCode; }
    public String getFullName() { return fullName; }
    public String getMobileNumber() { return mobileNumber; }
    public String getPickupPoint() { return pickupPoint; }
    public String getDestination() { return destination; }
    public String getDropPoint() { return dropPoint; }
    public String getTravelDate() { return travelDate; }
    public String getTravelTime() { return travelTime; }
    public String getPassengers() { return passengers; }
    public String getTripType() { return tripType; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setPickupPoint(String pickupPoint) { this.pickupPoint = pickupPoint; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDropPoint(String dropPoint) { this.dropPoint = dropPoint; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }
    public void setTravelTime(String travelTime) { this.travelTime = travelTime; }
    public void setPassengers(String passengers) { this.passengers = passengers; }
    public void setTripType(String tripType) { this.tripType = tripType; }
    public void setNotes(String notes) { this.notes = notes; }
}
