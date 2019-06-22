package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.FacebookApiClient;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Album;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Image;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.ImageGroup;
import pl.arczynskiadam.cojesc.client.google.ocr.GoogleOcrClient;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class FacebookAlbumMenuService {
    private FacebookApiClient facebookClient;
    private GoogleOcrClient ocrClient;

    public FacebookAlbumMenuService(FacebookApiClient facebookClient, GoogleOcrClient ocrService) {
        this.facebookClient = facebookClient;
        this.ocrClient = ocrService;
    }

    public Optional<String> getLunchMenuImageLink(FacebookAlbumRestaurant restaurant) {
        var album = facebookClient.getAlbum(restaurant.getFacebookAlbumId());
        return findNewestLunchMenuImageLink(album, restaurant);
    }

    private Optional<String> findNewestLunchMenuImageLink(Album album, Restaurant restaurant) {
        return album.getData().stream()
                .filter(after(expectedMenuPublishDate(restaurant)))
                .map(ImageGroup::findBiggestImage)
                .filter(menuImagesOnly(restaurant))
                .findFirst()
                .flatMap(this::toSource);
    }

    private Predicate<ImageGroup> after(ZonedDateTime time) {
        return imgGr -> imgGr.getCreatedTime().isAfter(time);
    }

    private Predicate<Image> menuImagesOnly(Restaurant restaurant) {
        return img -> {
            try {
                return ocrClient.imageContainsKeywords(
                        new URL(img.getSource()),
                        restaurant.getMenuKeyWords()
                );
            } catch (MalformedURLException e) {
                throw new RuntimeException(img.getSource() + " is not a correct url");
            }
        };
    }

    private ZonedDateTime expectedMenuPublishDate(Restaurant restaurant) {
        var menuDuration = Duration.ofDays(restaurant.getMenuDuration());
        return ZonedDateTime.now().truncatedTo(DAYS).plus(18, HOURS).minus(menuDuration);
    }

    private Optional<String> toSource(Image image) {
        return Optional.of(image.getSource());
    }
}
