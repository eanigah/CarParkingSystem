package carparkingsystem.core;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Parking lot class is @ThreadSafe
 */
@Component
public class ParkingLot {
    private final List<ParkingSpot> parkingSpots;
    private final int capacity;

    private static final Map<String, Map.Entry<ParkingSpot, LocalDateTime>> vehicleInTimeMap = new HashMap<>();

    public ParkingLot() {
        this.capacity = 100; // Default capacity of 100 cars mentioned in problem statement can be extracted from properties
        parkingSpots = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            parkingSpots.add(new ParkingSpot(i));
        }
    }

    public synchronized boolean isFull() {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isOccupied()) {
                return false;
            }
        }
        return true;
    }

    public synchronized int getAvailableParkingSpots() {
        int counter = 0;
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isOccupied()) {
                counter++;
            }
        }
        return counter;
    }

    public synchronized ParkingSpot parkCar(String licensePlate) {
        if (isFull()) {
            return null; // Parking lot is full
        }

        for (ParkingSpot spot : parkingSpots) {
            if (spot.isOccupied()) {
                spot.setOccupied(true);
                vehicleInTimeMap.put(licensePlate,  new AbstractMap.SimpleEntry<>(spot, LocalDateTime.now()));
                return spot;
            }
        }

        return null; // Unexpected case (should not happen if not full)
    }

    public synchronized long exitCar(String licensePlate) {
        Map.Entry<ParkingSpot, LocalDateTime> entry = vehicleInTimeMap.get(licensePlate);
        entry.getKey().setOccupied(false);
        long totalTimeInParkingLot = calculateTimeDifferenceInMinutes(entry.getValue(), LocalDateTime.now());
        vehicleInTimeMap.remove(licensePlate);
        return totalTimeInParkingLot;
    }

    private long calculateTimeDifferenceInMinutes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        return duration.toMinutes();
    }
}

