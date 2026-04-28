package ru.raif.rccar;

import ru.raif.rccar.config.AppiumProperties;
import ru.raif.rccar.service.CarControllerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Forklift control program - use CarControllerService for control
 * available commands:
 *  - forward(int ms)
 *  - forwardLeft(int ms)
 *  - forwardRight(int ms)
 *  - backward(int ms)
 *  - backwardLeft(int ms)
 *  - backwardRight(int ms)
 *  - liftUp(int ms) - 3000ms from bottom to top
 *  - liftDown(int ms) - 3000ms from bottom to top
 */
@SpringBootApplication
@EnableConfigurationProperties(AppiumProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner run(CarControllerService service) {
        return args -> {
            service.connect();
//            service.forward(200);
            service.liftUp(3000);
            service.liftDown(3000);
            //service.forwardLeft(3000);
            //service.forwardRight(3000);
            //service.neutral(1000);
            //service.backwardRight(2000);
        };
    }
}
