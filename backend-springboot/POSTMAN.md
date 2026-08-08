# AeroOps API – Postman checks

Base URL: `http://localhost:8081/api/dashboard`

| Method | URL | Purpose |
| --- | --- | --- |
| GET | `/summary` | Dashboard KPI totals |
| GET | `/flights` | All flights |
| GET | `/flights/1` | One flight |
| POST | `/flights` | Create a flight |
| PATCH | `/flights/1/status` | Change a flight status |

## POST body
```json
{
  "flightNumber": "AI 901", "origin": "BOM", "destination": "LHR", "gate": "A05",
  "scheduledDeparture": "2026-08-08T18:30:00", "aircraft": "Boeing 787-9",
  "status": "SCHEDULED", "passengerLoad": 42
}
```

## PATCH body
```json
{ "status": "BOARDING" }
```
