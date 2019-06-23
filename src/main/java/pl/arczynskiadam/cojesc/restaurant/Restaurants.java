package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Data
@Component
@ConfigurationProperties(prefix = "cojesc.restaurants")
public class Restaurants {
    List<FacebookAlbumRestaurant> fbAlbumRestaurants = Collections.emptyList();
    List<FacebookFeedRestaurant> fbFeedRestaurants = Collections.emptyList();

    public List<Restaurant> getAll() {
        return Stream.concat(fbAlbumRestaurants.stream(), fbFeedRestaurants.stream()).collect(toList());
    }

    public Restaurant getByName(String name) {
        return Stream.concat(fbAlbumRestaurants.stream(), fbFeedRestaurants.stream())
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElseThrow(noSuchRestaurantException(name));
    }

    private Supplier<RuntimeException> noSuchRestaurantException(String name) {
        return () -> new RuntimeException("No such restaurant: " + name + " supported");
    }
}
