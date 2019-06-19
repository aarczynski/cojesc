package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

@Service
public class MenuService {
    FacebookAlbumMenuService facebookAlbumMenuService;

    public MenuService(FacebookAlbumMenuService facebookAlbumMenuService) {
        this.facebookAlbumMenuService = facebookAlbumMenuService;
    }

    public Optional<String> findLunchMenuUrl(Restaurant restaurant) {
        if (restaurant instanceof FacebookAlbumRestaurant) {
            return facebookAlbumMenuService.getLunchMenuImageLink((FacebookAlbumRestaurant) restaurant);
        }

        return Optional.empty();
    }
}
