package pl.arczynskiadam.cojesc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

@Slf4j
@Service
public class MenuService {

    @Value("${cojesc.cache.name}")
    private String cacheName;

    private FacebookAlbumMenuService facebookAlbumMenuService;
    private FacebookFeedMenuService facebookFeedMenuService;

    public MenuService(FacebookAlbumMenuService facebookAlbumMenuService, FacebookFeedMenuService facebookFeedMenuService) {
        this.facebookAlbumMenuService = facebookAlbumMenuService;
        this.facebookFeedMenuService = facebookFeedMenuService;
    }

    @Cacheable(cacheNames = { "#{cojesc.cache.name}" }, unless = "#result == null")
    public Optional<String> findLunchMenu(Restaurant restaurant) {
        log.info("Lunch menu for {} not found in cache - downloading from the Internet", restaurant.getName());
        if (restaurant instanceof FacebookAlbumRestaurant) {
            return facebookAlbumMenuService.getLunchMenuImageLink((FacebookAlbumRestaurant) restaurant)
                    .map(imgUrl -> String.format("<img src=\"%s\"/>", imgUrl));
        }
        if (restaurant instanceof FacebookFeedRestaurant) {
            return facebookFeedMenuService.getLunchMenuLink((FacebookFeedRestaurant) restaurant)
                    .map(url -> String.format("<div class=\"fb-post\" data-href=\"%s\"></div>", url));
        }

        log.info("Lunch menu for {} not found in the Internet", restaurant);
        return Optional.empty();
    }

    @Scheduled(cron = "${cojesc.cache.eviction}")
    @CacheEvict(cacheNames = { "#{cojesc.cache.name}" }, allEntries = true)
    public void clearCache() {
        log.info("Lunch menu cache evicted");
    }
}
