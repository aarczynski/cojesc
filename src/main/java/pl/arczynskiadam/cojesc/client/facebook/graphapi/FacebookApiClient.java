package pl.arczynskiadam.cojesc.client.facebook.graphapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.config.FeignFacebookApiClientConfig;
import pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album.Album;

@FeignClient(name = "facebookApiClient", url = "${cojesc.http.client.facebook.url}", configuration = FeignFacebookApiClientConfig.class)
public interface FacebookApiClient {

    @RequestMapping("/v3.3/{albumId}/photos")
    Album getAlbum(@PathVariable("albumId") String albumId);
}
