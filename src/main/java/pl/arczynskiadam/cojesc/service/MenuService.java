package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
public class MenuService {
    private HtmlRetrieveService htmlRetrieveService;
    private FacebookAlbumMenuService facebookAlbumMenuService;
    private FacebookFeedMenuService facebookFeedMenuService;

    public MenuService(HtmlRetrieveService htmlRetrieveService, FacebookAlbumMenuService facebookAlbumMenuService, FacebookFeedMenuService facebookFeedMenuService) {
        this.htmlRetrieveService = htmlRetrieveService;
        this.facebookAlbumMenuService = facebookAlbumMenuService;
        this.facebookFeedMenuService = facebookFeedMenuService;
    }

    public Optional<String> findLunchMenu(Restaurant restaurant) {
        if (restaurant instanceof FacebookAlbumRestaurant) {
            return facebookAlbumMenuService.getLunchMenuImageLink((FacebookAlbumRestaurant) restaurant)
                    .map(s -> String.format("<img src=\"%s\"/>", s));
        }
        if (restaurant instanceof FacebookFeedRestaurant) {
            return facebookFeedMenuService.getLunchMenuLink((FacebookFeedRestaurant) restaurant)
                    .map(s -> htmlRetrieveService.fetchByCssClass(convertToUrl(s), "_4-u2 _39j6 _4-u8").get(0));
        }

        return Optional.empty();
    }

    private URL convertToUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(url + "is not a valid url");
        }
    }
}
