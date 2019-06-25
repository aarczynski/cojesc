package pl.arczynskiadam.cojesc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;
import pl.arczynskiadam.cojesc.util.UrlUtil;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class MenuService {

    private static final String CACHE_NAME = "lunch-menu";

    private FacebookAlbumMenuService facebookAlbumMenuService;
    private FacebookFeedMenuService facebookFeedMenuService;
    private HtmlRetrieveService htmlRetrieveService;

    public MenuService(FacebookAlbumMenuService facebookAlbumMenuService, FacebookFeedMenuService facebookFeedMenuService, HtmlRetrieveService htmlRetrieveService) {
        this.facebookAlbumMenuService = facebookAlbumMenuService;
        this.facebookFeedMenuService = facebookFeedMenuService;
        this.htmlRetrieveService = htmlRetrieveService;
    }

    @Cacheable(cacheNames = { CACHE_NAME }, unless = "#result == null")
    public Optional<String> findLunchMenu(Restaurant restaurant) {
        log.info("Lunch menu for {} not found in cache - downloading from the Internet", restaurant.getName());
        if (restaurant instanceof FacebookAlbumRestaurant) {
            return facebookAlbumMenuService.getLunchMenuImageLink((FacebookAlbumRestaurant) restaurant)
                    .map(imgUrl -> String.format("<img src=\"%s\"/>", imgUrl));
        }
        if (restaurant instanceof FacebookFeedRestaurant) {
            return facebookFeedMenuService.getLunchMenuLink((FacebookFeedRestaurant) restaurant)
                    .map(getFirstByCssClass("_4-u2 _39j6 _4-u8"));
        }

        log.info("Lunch menu for {} not found in the Internet", restaurant);
        return Optional.empty();
    }

    private Function<String, String> getFirstByCssClass(String cssClass) {
        return permalink -> htmlRetrieveService.fetchByCssClass(UrlUtil.toUrl(permalink), cssClass).get(0);
    }

    @Scheduled(cron = "${cojesc.cache.eviction}")
    @CacheEvict(cacheNames = { CACHE_NAME }, allEntries = true)
    public void clearCache() {
        log.info("{} cache evicted", CACHE_NAME);
    }
}
