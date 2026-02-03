package com.practice.leetcode.design;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DESIGN PARKING LOT WITH TICKETING SYSTEM
 * 
 * Requirements:
 * 1. Support multiple vehicle types: Motorcycle, Car, Truck.
 * 2. Support multiple spot types: Small, Medium, Large.
 * 3. Vehicles can only park in compatible spots (Motorcycle -> any, Car -> Medium/Large, Truck -> Large).
 * 4. Issue a Ticket upon entry (contains entry time, vehicle info, spot ID).
 * 5. Calculate Fee upon exit based on duration and vehicle type.
 */
class ParkingLotWithTicketsTest {

    enum VehicleType {
        MOTORCYCLE(1), CAR(2), TRUCK(3);
        final int size;
        VehicleType(int size) { this.size = size; }
    }

    enum SpotType {
        SMALL(1), MEDIUM(2), LARGE(3);
        final int size;
        SpotType(int size) { this.size = size; }
    }

    static class Vehicle {
        String licensePlate;
        VehicleType type;

        public Vehicle(String licensePlate, VehicleType type) {
            this.licensePlate = licensePlate;
            this.type = type;
        }
    }

    static class Ticket {
        String id;
        Vehicle vehicle;
        int spotId;
        LocalDateTime entryTime;
        LocalDateTime exitTime;
        double fee;

        public Ticket(Vehicle vehicle, int spotId) {
            this.id = UUID.randomUUID().toString();
            this.vehicle = vehicle;
            this.spotId = spotId;
            this.entryTime = LocalDateTime.now();
        }
    }

    static class ParkingSpot {
        int id;
        SpotType type;
        boolean isOccupied;

        public ParkingSpot(int id, SpotType type) {
            this.id = id;
            this.type = type;
        }

        public boolean canFit(VehicleType vehicleType) {
            return !isOccupied && this.type.size >= vehicleType.size;
        }
    }

    static class ParkingLot {
        private Map<SpotType, List<ParkingSpot>> spotsByType = new HashMap<>();
        private Map<String, Ticket> activeTickets = new HashMap<>();

        public ParkingLot(int smallCount, int mediumCount, int largeCount) {
            int idCounter = 1;
            addSpots(SpotType.SMALL, smallCount, idCounter);
            idCounter += smallCount;
            addSpots(SpotType.MEDIUM, mediumCount, idCounter);
            idCounter += mediumCount;
            addSpots(SpotType.LARGE, largeCount, idCounter);
        }

        private void addSpots(SpotType type, int count, int startId) {
            spotsByType.putIfAbsent(type, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                spotsByType.get(type).add(new ParkingSpot(startId + i, type));
            }
        }

        public Ticket issueTicket(Vehicle vehicle) {
            ParkingSpot spot = findAvailableSpot(vehicle.type);
            if (spot == null) return null;

            spot.isOccupied = true;
            Ticket ticket = new Ticket(vehicle, spot.id);
            activeTickets.put(ticket.id, ticket);
            return ticket;
        }

        public double processExit(String ticketId, LocalDateTime exitTime) {
            Ticket ticket = activeTickets.remove(ticketId);
            if (ticket == null) return -1;

            ticket.exitTime = exitTime;
            releaseSpot(ticket.spotId);
            
            ticket.fee = calculateFee(ticket);
            return ticket.fee;
        }

        private ParkingSpot findAvailableSpot(VehicleType vType) {
            // Try smallest compatible spot first
            for (SpotType sType : SpotType.values()) {
                if (sType.size >= vType.size) {
                    for (ParkingSpot spot : spotsByType.get(sType)) {
                        if (!spot.isOccupied) return spot;
                    }
                }
            }
            return null;
        }

        private void releaseSpot(int spotId) {
            for (List<ParkingSpot> spots : spotsByType.values()) {
                for (ParkingSpot spot : spots) {
                    if (spot.id == spotId) {
                        spot.isOccupied = false;
                        return;
                    }
                }
            }
        }

        private double calculateFee(Ticket ticket) {
            long hours = Duration.between(ticket.entryTime, ticket.exitTime).toHours();
            if (hours == 0) hours = 1; // Minimum 1 hour charge

            double rate;
            switch (ticket.vehicle.type) {
                case MOTORCYCLE:
                    rate = 1.0;
                    break;
                case CAR:
                    rate = 2.0;
                    break;
                case TRUCK:
                    rate = 5.0;
                    break;
                default:
                    rate = 0.0;
            }
            return hours * rate;
        }
    }

    // ========== TESTS ==========

    @Test
    @DisplayName("Should issue ticket and calculate fee correctly")
    void testTicketingSystem() {
        ParkingLot lot = new ParkingLot(1, 1, 1); // 1 Small, 1 Medium, 1 Large

        Vehicle car = new Vehicle("CAR-001", VehicleType.CAR);
        Ticket ticket = lot.issueTicket(car);

        assertThat(ticket).isNotNull();
        assertThat(activeTicketsCount(lot)).isEqualTo(1);

        // Simulate 3 hours stay
        LocalDateTime exitTime = ticket.entryTime.plusHours(3);
        double fee = lot.processExit(ticket.id, exitTime);

        assertThat(fee).isEqualTo(6.0); // 3 hours * $2.0
        assertThat(activeTicketsCount(lot)).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle multiple vehicle types and spot compatibility")
    void testSpotCompatibility() {
        ParkingLot lot = new ParkingLot(1, 0, 1); // Only 1 Small and 1 Large

        Vehicle truck = new Vehicle("TRUCK-99", VehicleType.TRUCK);
        Vehicle moto = new Vehicle("MOTO-11", VehicleType.MOTORCYCLE);

        Ticket truckTicket = lot.issueTicket(truck);
        assertThat(truckTicket).isNotNull();
        // Truck should take the Large spot (only one available for it)
        assertThat(truckTicket.spotId).isGreaterThan(1); 

        Ticket motoTicket = lot.issueTicket(moto);
        assertThat(motoTicket).isNotNull();
        assertThat(motoTicket.spotId).isEqualTo(1); // Smallest first
    }

    @Test
    @DisplayName("Should return null if no compatible spots available")
    void testNoSpots() {
        ParkingLot lot = new ParkingLot(1, 0, 0); // Only 1 Small spot

        Vehicle car = new Vehicle("CAR-X", VehicleType.CAR);
        Ticket ticket = lot.issueTicket(car); // Car cannot fit in Small spot
        
        assertThat(ticket).isNull();
    }

    private int activeTicketsCount(ParkingLot lot) {
        return lot.activeTickets.size();
    }
}
