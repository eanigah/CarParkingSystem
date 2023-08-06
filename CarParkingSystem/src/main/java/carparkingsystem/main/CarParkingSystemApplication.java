package carparkingsystem.main;

import carparkingsystem.service.ParkingService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "carparkingsystem")
public class CarParkingSystemApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CarParkingSystemApplication.class);
        ParkingService ps = context.getBean(ParkingService.class);
        context.close();
    }

}
