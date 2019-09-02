package pl.arczynskiadam.cojesc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

@Slf4j
@Service
public class MenuService {

    private static final String CACHE_NAME = "lunch-menu";

    private MenuRetrieveService menuRetrieveService;

    public MenuService(MenuRetrieveService menuRetrieveService) {
        this.menuRetrieveService = menuRetrieveService;
    }

    @Cacheable(cacheNames = { CACHE_NAME }, key = "#restaurant.id", unless = "#result == null")
    public Optional<String> findLunchMenu(Restaurant restaurant) {
        log.info("Lunch menu for {} not found in cache - downloading from the Internet", restaurant.getName());
        return menuRetrieveService.findLunchMenu(restaurant);
    }

    @Scheduled(cron = "${cojesc.cache.eviction}")
    @CacheEvict(cacheNames = { CACHE_NAME }, allEntries = true)
    public void clearCache() {
        log.info("{} cache evicted", CACHE_NAME);
    }
}
