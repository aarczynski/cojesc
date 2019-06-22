package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

@Service
public class MenuService {
    FacebookAlbumMenuService facebookAlbumMenuService;
    FacebookFeedMenuService facebookFeedMenuService;

    public MenuService(FacebookAlbumMenuService facebookAlbumMenuService, FacebookFeedMenuService facebookFeedMenuService) {
        this.facebookAlbumMenuService = facebookAlbumMenuService;
        this.facebookFeedMenuService = facebookFeedMenuService;
    }

    public Optional<String> findLunchMenuUrl(Restaurant restaurant) {
        if (restaurant instanceof FacebookAlbumRestaurant) {
            return facebookAlbumMenuService.getLunchMenuImageLink((FacebookAlbumRestaurant) restaurant);
        }
        if (restaurant instanceof FacebookFeedRestaurant) {
            return facebookFeedMenuService.getLunchMenuLink((FacebookFeedRestaurant) restaurant);
        }

        return Optional.empty();
    }
}
