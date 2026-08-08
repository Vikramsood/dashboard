package com.aeroops.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {
  private final FlightRepository flights;
  private final BookingRepository bookings;
  DashboardController(FlightRepository flights, BookingRepository bookings) { this.flights = flights; this.bookings = bookings; }

  @GetMapping("/flights") List<Flight> flights() { return flights.findAll(); }
  @GetMapping("/flights/{id}") Flight flight(@PathVariable Long id) { return flights.findById(id).orElseThrow(() -> new FlightNotFoundException(id)); }
  @PostMapping("/flights") @ResponseStatus(HttpStatus.CREATED) Flight create(@RequestBody CreateFlight request) { Flight flight = new Flight(); flight.flightNumber=request.flightNumber(); flight.origin=request.origin(); flight.destination=request.destination(); flight.gate=request.gate(); flight.scheduledDeparture=request.scheduledDeparture(); flight.aircraft=request.aircraft(); flight.status=request.status()==null ? "SCHEDULED" : request.status(); flight.passengerLoad=request.passengerLoad()==null ? 0 : request.passengerLoad(); return flights.save(flight); }
  @PatchMapping("/flights/{id}/status") Flight updateStatus(@PathVariable Long id, @RequestBody UpdateFlightStatus request) { Flight flight = flight(id); flight.status = request.status(); return flights.save(flight); }
  @GetMapping("/summary") Summary summary() { long total = flights.count(); long delayed = flights.countByStatus("DELAYED"); long onTime = flights.countByStatus("SCHEDULED") + flights.countByStatus("BOARDING") + flights.countByStatus("DEPARTED") + flights.countByStatus("IN_AIR"); double onTimeRate = total == 0 ? 0 : Math.round((onTime * 1000.0 / total)) / 10.0; return new Summary(total, bookings.count(), flights.averagePassengerLoad(), bookings.totalRevenue(), delayed, onTimeRate); }
  record Summary(long activeFlights, long totalBookings, double averagePassengerLoad, BigDecimal totalRevenue, long delayedFlights, double onTimeRate) {}
  record CreateFlight(String flightNumber, String origin, String destination, String gate, LocalDateTime scheduledDeparture, String aircraft, String status, Integer passengerLoad) {}
  record UpdateFlightStatus(String status) {}
  @ResponseStatus(HttpStatus.NOT_FOUND) static class FlightNotFoundException extends RuntimeException { FlightNotFoundException(Long id) { super("Flight " + id + " not found"); } }
}

interface FlightRepository extends JpaRepository<Flight, Long> { long countByStatus(String status); @Query("select coalesce(avg(f.passengerLoad), 0) from Flight f") double averagePassengerLoad(); }
interface BookingRepository extends JpaRepository<Booking, Long> { @Query("select coalesce(sum(b.fareAmount), 0) from Booking b") BigDecimal totalRevenue(); }

@Entity @Table(name="flights", schema="aeroops") class Flight { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; @Column(name="flight_number") String flightNumber; String origin; String destination; String gate; @Column(name="scheduled_departure") LocalDateTime scheduledDeparture; String aircraft; String status; @Column(name="passenger_load") Integer passengerLoad; public Long getId(){return id;} public String getFlightNumber(){return flightNumber;} public String getOrigin(){return origin;} public String getDestination(){return destination;} public String getGate(){return gate;} public LocalDateTime getScheduledDeparture(){return scheduledDeparture;} public String getAircraft(){return aircraft;} public String getStatus(){return status;} public Integer getPassengerLoad(){return passengerLoad;} }
@Entity @Table(name="bookings", schema="aeroops") class Booking { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; @Column(name="fare_amount") BigDecimal fareAmount; }
