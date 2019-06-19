package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

@Service
public class MenuService {
    MenuImageFilterService menuImageFilterService;

    public MenuService(MenuImageFilterService menuImageFilterService) {
        this.menuImageFilterService = menuImageFilterService;
    }

    public Optional<String> findLunchMenuUrl(Restaurant restaurant) {
        if (restaurant instanceof FacebookAlbumRestaurant) {
            return menuImageFilterService.getLunchMenuImageLink((FacebookAlbumRestaurant) restaurant);
        }

        return Optional.empty();
    }
}
