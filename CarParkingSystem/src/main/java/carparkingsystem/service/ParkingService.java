package carparkingsystem.service;

import carparkingsystem.core.ParkingLot;
import carparkingsystem.core.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {
    private final ParkingLot parkingLot;

    private final ParkingChargeCalculatorService parkingChargeCalculatorService;


    @Autowired
    public ParkingService(ParkingLot parkingLot, ParkingChargeCalculatorService parkingChargeCalculatorService) {
        this.parkingLot = parkingLot;
        this.parkingChargeCalculatorService = parkingChargeCalculatorService;
    }

    public void parkCar(String licensePlate) {
        ParkingSpot spot = parkingLot.parkCar(licensePlate);
        if (spot == null) {
            System.out.println("Car park is full.");
        }
    }

    public double exitCar(String licensePlate) {
        return parkingChargeCalculatorService.calculateParkingCharge(parkingLot.exitCar(licensePlate));
    }

    public int getAvailableParkingSpots() {
        return parkingLot.getAvailableParkingSpots();
    }

}
