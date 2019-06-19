package pl.arczynskiadam.cojesc.restaurant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
@ConfigurationProperties(prefix = "cojesc.restaurants")
public class Restaurants {
    List<FacebookAlbumRestaurant> fbAlbumRestaurants = Collections.emptyList();
    List<FacebookAlbumRestaurant> fbFeedRestaurants = Collections.emptyList();

    public List<FacebookAlbumRestaurant> getFbAlbumRestaurants() {
        return fbAlbumRestaurants;
    }

    public void setFbAlbumRestaurants(List<FacebookAlbumRestaurant> fbAlbumRestaurants) {
        this.fbAlbumRestaurants = fbAlbumRestaurants;
    }

    public void setFbFeedRestaurants(List<FacebookAlbumRestaurant> fbFeedRestaurants) {
        this.fbFeedRestaurants = fbFeedRestaurants;
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
