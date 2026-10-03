# Anil Cabs API Documentation

## Base URL

Local development: `http://localhost:8080/api`

## Endpoints

### Health check

- `GET /api/health`
- Returns service and status information

### Create booking

- `POST /api/bookings`
- Request body:

```json
{
  "fullName": "Ravi Kumar",
  "mobileNumber": "9876543210",
  "pickupPoint": "Hitech City, Hyderabad",
  "destination": "Rajiv Gandhi International Airport",
  "dropPoint": "Airport departures",
  "travelDate": "2026-10-02",
  "travelTime": "08:30",
  "passengers": "2",
  "tripType": "One Way",
  "notes": "Two suitcases"
}
```

- Response: `201 Created` with a booking summary containing the generated public booking ID

### Fetch booking by ID

- `GET /api/bookings/{bookingId}`
- Returns the saved booking summary if found

## Validation rules

- `fullName`: required, max 100 chars
- `mobileNumber`: required, exactly 10 digits
- `pickupPoint`: required, max 250 chars
- `destination`: required, max 250 chars
- `travelDate`: optional, must be today or a future date in `YYYY-MM-DD` format
- `travelTime`: optional, must be in `HH:mm` format
- `passengers`: required, 1-7
- `tripType`: required and one of `One Way`, `Round Trip`, `Local Trips`, `Airport Pickup & Drop`

## Error responses

The API returns JSON error bodies with a timestamp, HTTP status code, and a human-readable message or validation errors.
