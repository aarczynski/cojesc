package pl.arczynskiadam.lunch.http.client.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.arczynskiadam.lunch.http.client.facebook.config.FeignFacebookApiClientConfig;
import pl.arczynskiadam.lunch.http.client.facebook.dto.album.Album;

@FeignClient(name = "facebookApiClient", url = "${cojesc.http.client.facebook.url}", configuration = FeignFacebookApiClientConfig.class)
public interface FacebookApiClient {

    @RequestMapping("/v3.3/{albumId}/photos")
    Album getAlbum(@PathVariable("albumId") String albumId);
}
