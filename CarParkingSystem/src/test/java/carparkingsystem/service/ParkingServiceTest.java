package carparkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import carparkingsystem.core.ParkingLot;
import carparkingsystem.core.ParkingSpot;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ParkingService.class})
@ExtendWith(SpringExtension.class)
class ParkingServiceTest {

    private ExecutorService executorService;

    @MockBean
    private ParkingChargeCalculatorService parkingChargeCalculatorService;

    @MockBean
    private ParkingLot parkingLot;

    @Autowired
    private ParkingService parkingService;

    @BeforeEach
    public void setup() {
       // Create a thread pool with 10 threads to simulate 10 cars arriving concurrently
        executorService = Executors.newFixedThreadPool(10);
    }
        @Test
        public void testParkingServiceConcurrent() throws InterruptedException {
            ParkingLot parkingLot = new ParkingLot(); // Create a parking lot with a capacity of 100 cars
            ParkingChargeCalculatorService parkingChargeCalculatorService = new ParkingChargeCalculatorService();
            ParkingService parkingService = new ParkingService(parkingLot, parkingChargeCalculatorService);

            int numCars = 100; // Number of cars to simulate
            CountDownLatch latch = new CountDownLatch(numCars);
            ExecutorService executorService = Executors.newFixedThreadPool(numCars);

            for (int i = 1; i <= numCars; i++) {
                int carLicensePlateNumber = (int) (Math.random() * 240) + 1; // Random parking time between 1 and 240 minutes
                executorService.submit(() -> {
                    parkingService.parkCar("Car"+carLicensePlateNumber);
                    latch.countDown();
                });
                executorService.submit(() -> {
                    parkingService.exitCar("Car"+carLicensePlateNumber);
                    latch.countDown();
                });
            }

            // Wait for all threads to finish
            latch.await();
            executorService.shutdown();

            // Check the parking lot status after all cars have parked and few exited as well
            int availableParkingSpots = parkingService.getAvailableParkingSpots();

            // The parking spot should always be present considering atleast one car will exit
            assertTrue(availableParkingSpots > 0);

        }


    /**
     * Method under test: {@link ParkingService#parkCar(String)}
     */
    @Test
    void testParkCar() {
        when(parkingLot.parkCar((String) any())).thenReturn(new ParkingSpot(1));
        parkingService.parkCar("License Plate");
        verify(parkingLot).parkCar((String) any());
    }

    /**
     * Method under test: {@link ParkingService#parkCar(String)}
     */
    @Test
    void testParkCar2() {
        when(parkingLot.parkCar((String) any())).thenReturn(null);
        parkingService.parkCar("License Plate");
        verify(parkingLot).parkCar((String) any());
    }

    /**
     * Method under test: {@link ParkingService#exitCar(String)}
     */
    @Test
    void testExitCar() {
        when(parkingChargeCalculatorService.calculateParkingCharge(anyLong())).thenReturn(10.0d);
        when(parkingLot.exitCar((String) any())).thenReturn(1L);
        assertEquals(10.0d, parkingService.exitCar("License Plate"));
        verify(parkingChargeCalculatorService).calculateParkingCharge(anyLong());
        verify(parkingLot).exitCar((String) any());
    }

    @AfterEach
    void tearDown() {
        executorService = null;
    }
}

