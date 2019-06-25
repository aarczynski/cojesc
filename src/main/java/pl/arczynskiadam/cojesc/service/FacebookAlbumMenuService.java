package pl.arczynskiadam.cojesc.service;

import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.FacebookApiClient;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Album;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Image;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.ImageGroup;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Photos;
import pl.arczynskiadam.cojesc.client.google.ocr.GoogleOcrClient;
import pl.arczynskiadam.cojesc.restaurant.FacebookAlbumRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.stream.Collectors.toList;
import static pl.arczynskiadam.cojesc.util.UrlUtil.toUrl;

@Service
public class FacebookAlbumMenuService {
    private FacebookApiClient facebookClient;
    private GoogleOcrClient ocrClient;

    public FacebookAlbumMenuService(FacebookApiClient facebookClient, GoogleOcrClient ocrService) {
        this.facebookClient = facebookClient;
        this.ocrClient = ocrService;
    }

    public Optional<String> getLunchMenuImageLink(FacebookAlbumRestaurant restaurant) {
        var images = facebookClient.getAlbums(restaurant.getFacebookId()).getData()
                .stream()
                .filter(albumToSearch(restaurant))
                .map(getAlbumPhotos())
                .flatMap(getPhotosData())
                .collect(toList());
        return findNewestLunchMenuImageLink(images, restaurant);
    }

    private Predicate<Album> albumToSearch(FacebookAlbumRestaurant restaurant) {
        return album -> restaurant.getFacebookAlbumsToSearch().contains(album.getName());
    }

    private Function<Album, Photos> getAlbumPhotos() {
        return album -> facebookClient.getPhotos(album.getId());
    }

    private Function<Photos, Stream<? extends ImageGroup>> getPhotosData() {
        return photos -> photos.getData().stream();
    }

    private Optional<String> findNewestLunchMenuImageLink(List<ImageGroup> images, Restaurant restaurant) {
        return images.stream()
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
        return img -> ocrClient.imageContainsKeywords(toUrl(img.getSource()), restaurant.getMenuKeyWords());
    }

    private ZonedDateTime expectedMenuPublishDate(Restaurant restaurant) {
        var menuDuration = Duration.ofDays(restaurant.getMenuDuration());
        return ZonedDateTime.now().truncatedTo(DAYS).plus(18, HOURS).minus(menuDuration);
    }

    private Optional<String> toSource(Image image) {
        return Optional.of(image.getSource());
    }
}
