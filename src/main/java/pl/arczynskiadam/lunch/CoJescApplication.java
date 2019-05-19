package pl.arczynskiadam.lunch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.arczynskiadam.lunch.restaurant.RestaurantsProperties;

@SpringBootApplication
@EnableConfigurationProperties(RestaurantsProperties.class)
public class CoJescApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoJescApplication.class, args);
    }

}
