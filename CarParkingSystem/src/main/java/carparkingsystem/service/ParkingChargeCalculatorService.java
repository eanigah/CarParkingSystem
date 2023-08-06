package carparkingsystem.service;

import org.springframework.stereotype.Service;

@Service
public class ParkingChargeCalculatorService {
    private static final double CHARGE_PER_HOUR = 2.0;

    public double calculateParkingCharge(long parkingTimeInMinutes) {
        if (parkingTimeInMinutes > 0L) {
            long parkingTimeInHours = (long) Math.ceil(parkingTimeInMinutes / 60.0);
            return parkingTimeInHours * CHARGE_PER_HOUR;
        }
        else return 0.0d;
    }
}
