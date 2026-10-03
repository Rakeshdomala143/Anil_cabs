# Anil Cabs — React + Spring Boot

A responsive cab-booking web app using React/Vite for the frontend and Java 17 + Spring Boot for the REST API. Each booking is stored in the database and assigned a unique reference such as `ANIL-20260930-8A12BC`. After the API saves the booking, the browser opens WhatsApp with the booking details pre-filled for the customer to review and send.

## Included features

- Responsive Anil Cabs website in orange, black and white
- Booking form: full name, mobile number, pickup point, destination, drop point, trip type, travel date/time, passenger count and notes
- Server-side validation and unique booking reference
- REST endpoints: health check, create booking, fetch booking by reference
- CORS configuration for React frontend to call Java backend
- Local H2 database for development; PostgreSQL supported for deployment
- WhatsApp click-to-chat flow using a configurable phone number
- If the booking API is unavailable, the form opens WhatsApp with a direct request and clearly indicates that it was not saved to the website database

> WhatsApp click-to-chat does not send a message automatically. The customer must tap **Send** in WhatsApp. Automatic outbound messages, delivery notifications and WhatsApp webhooks require the official WhatsApp Business Platform and its credentials.

## Requirements

- Node.js 20+ and npm
- Java 17+
- Use the included Gradle wrapper; no separate Gradle installation is required

## Run locally

### 1. Start the Java backend

Configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` for your local MySQL server before starting the backend. The default URL targets `localhost:3306/Anil_cabs`. Keep real credentials in environment variables; do not commit them.

```bash
cd backend
.\gradlew.bat bootRun
```

Backend health check: <http://localhost:8080/api/health>

The backend uses MySQL; bookings persist across application restarts.

### 2. Start the React frontend

Open a second terminal:

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

Open the URL printed by Vite, usually <http://localhost:5173>.

Edit `frontend/.env`:

```env
VITE_API_BASE_URL=/api
VITE_WHATSAPP_NUMBER=919014726337
```

For local development, keep `VITE_API_BASE_URL` set to `/api`; Vite forwards requests to the Java backend. Do not use `localhost:8080` when opening the frontend from another device, because that address would point to that device. In production, set `VITE_API_BASE_URL` to the deployed backend URL.

`VITE_WHATSAPP_NUMBER` must be the business WhatsApp number in international format, digits only. The supplied value uses the phone number shown on the provided Anil Cabs artwork; change it if the business uses another WhatsApp number. Restart Vite after changing environment variables.

For Docker Compose, copy the project `.env.example` to `.env`, set unique strong values for `DB_PASSWORD` and `MYSQL_ROOT_PASSWORD`, then run `docker compose up --build`. Compose starts MySQL with a persistent named volume.

## API examples

### Health

```http
GET /api/health
```

### Create a booking

```http
POST /api/bookings
Content-Type: application/json
```

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

### Look up a booking

```http
GET /api/bookings/ANIL-YYYYMMDD-ABC123
```

## Deployment guide (production)

A straightforward setup is **Render for the Java API + PostgreSQL** and **Vercel or Render Static Sites for React**.

### A. Deploy the PostgreSQL database

1. Create a managed PostgreSQL database with your hosting provider.
2. Keep the database credentials private. Do not commit them to Git.
3. Copy the database host, port, database name, username and password.

### B. Deploy the backend on Render

1. Push this project to a GitHub repository.
2. Create a Render **Web Service** from the repository.
3. Set the Root Directory to `backend`.
4. Build command: `chmod +x gradlew && ./gradlew --no-daemon bootJar`
5. Start command: `java -jar build/libs/anil-cabs-api-1.0.0.jar`
6. Add these environment variables, using values from your PostgreSQL provider:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://YOUR_DB_HOST:5432/YOUR_DB_NAME
SPRING_DATASOURCE_USERNAME=YOUR_DB_USER
SPRING_DATASOURCE_PASSWORD=YOUR_DB_PASSWORD
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=update
APP_CORS_ALLOWED_ORIGIN=https://YOUR-FRONTEND-DOMAIN
```

Render provides the `PORT` environment variable; the app reads it automatically. If your database requires SSL, use the provider's recommended JDBC SSL parameters. Confirm the backend works at `https://YOUR-BACKEND-DOMAIN/api/health`.

### C. Deploy the frontend

For Vercel, import the repository and set the project Root Directory to `frontend`. Build command: `npm run build`; output directory: `dist`.

Set frontend environment variables before deploying:

```env
VITE_API_BASE_URL=https://YOUR-BACKEND-DOMAIN/api
VITE_WHATSAPP_NUMBER=919014726337
```

For a Render Static Site, use Root Directory `frontend`, Build Command `npm install && npm run build`, Publish Directory `dist`, and the same environment variables.

After deployment, update the backend's `APP_CORS_ALLOWED_ORIGIN` to the exact deployed frontend origin (include `https://`, no trailing path). Redeploy the backend after changing it. If using multiple frontend domains, separate origins with commas.

If the booking API is unreachable, the form still opens WhatsApp with the trip details as a direct request. That fallback is not stored in the website database and does not have a booking ID; deploy and configure the Spring Boot API to enable saved bookings.

### D. Before sharing with customers

- Test a booking from the deployed site and confirm a record is saved in PostgreSQL.
- Confirm WhatsApp opens to the correct business number and the message has the booking ID.
- Test invalid phone numbers and missing required fields.
- Configure database backups and provider monitoring.
- Add a privacy notice and define how long customer phone numbers and trip details are retained.
- Add authentication and authorization before building any admin dashboard or exposing booking lists.
- Consider rate limiting, spam protection and server-side logging before advertising the site publicly.

## Important production notes

- The included default H2 database is in-memory and is only for local development.
- This starter does not yet include an admin dashboard, cab/driver assignment, fare calculation, online payments, SMS, or automatic WhatsApp Business API messages.
- Booking creation stores a request; it does not mean a cab has been confirmed. The initial status is `PENDING_WHATSAPP_CONFIRMATION`.
- Do not publish real customer booking data in public logs or screenshots.
