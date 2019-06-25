package pl.arczynskiadam.cojesc.client.facebook.graphapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.config.FeignFacebookApiClientConfig;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Photos;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.feed.Feed;

@FeignClient(name = "facebookApiClient", url = "${cojesc.http.client.facebook.url}", configuration = FeignFacebookApiClientConfig.class)
public interface FacebookApiClient {

    @RequestMapping("/v3.3/{albumId}/photos?fields=images,created_time")
    Photos getPhotos(@PathVariable("albumId") String albumId);

    @RequestMapping("/v3.3/{restaurantId}/posts?fields=message,permalink_url,created_time")
    Feed getPosts(@PathVariable("restaurantId") String restaurantId);
}
