package pl.arczynskiadam.cojesc.restaurant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
@ConfigurationProperties(prefix = "cojesc.restaurants")
public class Restaurants {
    List<FacebookAlbumRestaurant> fbAlbumRestaurants;

    public List<FacebookAlbumRestaurant> getFbAlbumRestaurants() {
        return fbAlbumRestaurants;
    }

    public void setFbAlbumRestaurants(List<FacebookAlbumRestaurant> fbAlbumRestaurants) {
        this.fbAlbumRestaurants = fbAlbumRestaurants;
    }

    public FacebookAlbumRestaurant getByName(String name) {
        return fbAlbumRestaurants
                .stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElseThrow(noSuchRestaurantException(name));
    }

    private Supplier<RuntimeException> noSuchRestaurantException(String name) {
        return () -> new RuntimeException("No such restaurant: " + name + " supported");
    }
}
