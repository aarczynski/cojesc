package pl.arczynskiadam.cojesc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.FacebookApiClient;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.feed.Post;
import pl.arczynskiadam.cojesc.restaurant.FacebookFeedRestaurant;
import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

@Slf4j
@Service
public class FacebookFeedMenuService {
    private FacebookApiClient facebookClient;

    public FacebookFeedMenuService(FacebookApiClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    public Optional<String> getLunchMenuLink(FacebookFeedRestaurant restaurant) {
        var feed = facebookClient.getPosts(restaurant.getFacebookId());

        Optional<String> lunchUrl = feed.getData().stream()
                .filter(after(expectedMenuPublishDate(restaurant)))
                .filter(withKeyWords(restaurant))
                .findFirst()
                .flatMap(this::toPermalink);

        lunchUrl.ifPresentOrElse(
                url -> log.info("Found lunch menu url for {}: {}", restaurant.getName(), url),
                () -> log.info("Lunch menu for {} not found", restaurant.getName()));

        return lunchUrl;
    }

    private Predicate<Post> after(ZonedDateTime time) {
        return post -> post.getCreatedTime().isAfter(time);
    }

    private Predicate<Post> withKeyWords(FacebookFeedRestaurant restaurant) {
        return p -> containsAll(p.getMessage(), restaurant.getMenuKeyWords());
    }

    private ZonedDateTime expectedMenuPublishDate(Restaurant restaurant) {
        var menuDuration = Duration.ofDays(restaurant.getMenuDuration());
        return ZonedDateTime.now().truncatedTo(DAYS).plus(18, HOURS).minus(menuDuration);
    }

    private boolean containsAll(String message, String... keyWords) {
        return Stream.of(keyWords)
                .map(String::toLowerCase)
                .allMatch(message.toLowerCase()::contains);
    }

    private Optional<String> toPermalink(Post post) {
        return Optional.of(post.getPermalink());
    }
}
