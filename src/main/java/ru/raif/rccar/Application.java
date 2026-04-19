package ru.raif.rccar;

import ru.raif.rccar.config.AppiumProperties;
import ru.raif.rccar.service.CarControllerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(AppiumProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner demo(CarControllerService service) {
        return args -> {
            service.connect();
            service.runDemoSequence();
        };
    }
}
