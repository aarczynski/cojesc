package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Data
@Component
@ConfigurationProperties(prefix = "cojesc")
public class Restaurants {
    private List<Restaurant> restaurants = Collections.emptyList();

    public List<Restaurant> getAll() {
        return restaurants;
    }

    public Restaurant getById(String id) {
        return getAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(noSuchRestaurantException(id));
    }

    private Supplier<RuntimeException> noSuchRestaurantException(String name) {
        return () -> new RuntimeException("No such restaurant: " + name + " supported");
    }
}
