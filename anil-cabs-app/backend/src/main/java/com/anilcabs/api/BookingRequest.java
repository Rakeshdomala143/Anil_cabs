package com.anilcabs.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookingRequest(
    @NotBlank @Size(max = 100) String fullName,
    @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must contain 10 digits") String mobileNumber,
    @NotBlank @Size(max = 250) String pickupPoint,
    @NotBlank @Size(max = 250) String destination,
    @Size(max = 250) String dropPoint,
    @Size(max = 30) String travelDate,
    @Size(max = 10) String travelTime,
    @NotBlank @Pattern(regexp = "^[1-7]$", message = "Passengers must be between 1 and 7") String passengers,
    @NotBlank @Pattern(regexp = "^(One Way|Round Trip|Local Trips|Airport Pickup & Drop)$", message = "Invalid trip type") String tripType,
    @Size(max = 500) String notes
) {}
