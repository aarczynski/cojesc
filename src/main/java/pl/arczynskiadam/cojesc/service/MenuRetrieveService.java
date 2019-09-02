package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

@Service
public class MenuRetrieveService {

    public Optional<String> findLunchMenu(Restaurant restaurant) {
        return Optional.of("dummy");
    }
}
