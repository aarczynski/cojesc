package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

@Data
@Component
@ConfigurationProperties(prefix = "cojesc.restaurants")
public class Restaurants {
    private List<FacebookAlbumRestaurant> fbAlbumRestaurants = Collections.emptyList();
    private List<FacebookFeedRestaurant> fbFeedRestaurants = Collections.emptyList();
    private List<WwwRestaurant> wwwRestaurants = Collections.emptyList();

    public List<Restaurant> getAll() {
        return Stream.of(
                fbAlbumRestaurants.stream(),
                fbFeedRestaurants.stream(),
                wwwRestaurants.stream()
        ).reduce(Stream::concat).
                orElseGet(Stream::empty)
                .collect(toUnmodifiableList());
    }

    public Restaurant getByName(String name) {
        return getAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElseThrow(noSuchRestaurantException(name));
    }

    private Supplier<RuntimeException> noSuchRestaurantException(String name) {
        return () -> new RuntimeException("No such restaurant: " + name + " supported");
    }
}
