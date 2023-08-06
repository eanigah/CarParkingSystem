package carparkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ParkingChargeCalculatorService.class})
@ExtendWith(SpringExtension.class)
class ParkingChargeCalculatorServiceTest {
    @Autowired
    private ParkingChargeCalculatorService parkingChargeCalculatorService;

    /**
     * Method under test: {@link ParkingChargeCalculatorService#calculateParkingCharge(long)}
     */
    @Test
    void testCalculateParkingCharge() {
        //if one minute a car spends in parking it will be charged 2 GBP
        assertEquals(2.0d, parkingChargeCalculatorService.calculateParkingCharge(1L));
        assertEquals(0.0d, parkingChargeCalculatorService.calculateParkingCharge(0L));
        assertEquals(4.0d, parkingChargeCalculatorService.calculateParkingCharge(100L));
        assertEquals(2.0d, parkingChargeCalculatorService.calculateParkingCharge(30L));
        assertEquals(2.0d, parkingChargeCalculatorService.calculateParkingCharge(25L));
    }
}

