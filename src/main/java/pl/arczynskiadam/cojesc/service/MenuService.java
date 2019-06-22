package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.util.Optional;

import static pl.arczynskiadam.cojesc.util.UrlUtil.toUrl;

@Service
public class MenuService {
    private static final String FACEBOOK_POST_CSS_CLASS = "_4-u2 _39j6 _4-u8";

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
                    .map(imgUrl -> String.format("<img src=\"%s\"/>", imgUrl));
        }
        if (restaurant instanceof FacebookFeedRestaurant) {
            return facebookFeedMenuService.getLunchMenuLink((FacebookFeedRestaurant) restaurant)
                    .map(html -> htmlRetrieveService.fetchByCssClass(toUrl(html), FACEBOOK_POST_CSS_CLASS).get(0));
        }

        return Optional.empty();
    }
}
