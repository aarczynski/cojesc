package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.FacebookApiClient;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Album;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Image;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.ImageGroup;
import pl.arczynskiadam.cojesc.client.google.ocr.GoogleOcrClient;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;
import pl.arczynskiadam.cojesc.restaurant.RestaurantsProperties;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class MenuImageFilterService {
    private FacebookApiClient facebookClient;
    private RestaurantsProperties restaurantsProperties;
    private GoogleOcrClient ocrClient;

    public MenuImageFilterService(FacebookApiClient facebookClient, RestaurantsProperties restaurantsProperties, GoogleOcrClient ocrService) {
        this.facebookClient = facebookClient;
        this.restaurantsProperties = restaurantsProperties;
        this.ocrClient = ocrService;
    }

    public String getLunchMenuImageLink(Restaurant restaurant) {
        var restaurantProperties = restaurantsProperties.getForRestaurant(restaurant);
        var album = facebookClient.getAlbum(restaurantProperties.getFacebookAlbumId());
        return findNewestLunchMenuImageLink(album, restaurant);
    }

    private String findNewestLunchMenuImageLink(Album album, Restaurant restaurant) {
        return album.getData().stream()
                .filter(after(expectedMenuPublishDate(restaurant)))
                .map(ImageGroup::findBiggestImage)
                .filter(isMenuImage(restaurant))
                .findFirst()
                .get()
                .getSource();
    }

    private Predicate<Image> isMenuImage(Restaurant restaurant) {
        return img -> {
            try {
                return isMenuImg(new URL(img.getSource()), restaurant);
            } catch (MalformedURLException e) {
                throw new RuntimeException(img.getSource() + " is incorrect url");
            }
        };
    }

    private Predicate<ImageGroup> after(ZonedDateTime time) {
        return imgGr -> imgGr.getCreatedTime().isAfter(time);
    }

    private boolean isMenuImg(URL imgUrl, Restaurant restaurant) {
        return ocrClient.imageContainsKeywords(imgUrl, restaurantsProperties.getForRestaurant(restaurant).getMenuKeyWords());
    }

    private ZonedDateTime expectedMenuPublishDate(Restaurant restaurant) {
        var menuDuration = Duration.ofDays(restaurantsProperties.getForRestaurant(restaurant).getMenuDuration());
        return ZonedDateTime.now().truncatedTo(DAYS).plus(18, HOURS).minus(menuDuration);
    }
}
