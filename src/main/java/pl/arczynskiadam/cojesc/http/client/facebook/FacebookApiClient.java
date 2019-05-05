package pl.arczynskiadam.cojesc.http.client.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.arczynskiadam.cojesc.http.client.facebook.dto.album.Album;

@FeignClient(name = "facebookApiClient", url = "https://graph.facebook.com")
public interface FacebookApiClient {

    @RequestMapping("/v3.3/{albumId}/photos?fields=images,created_time")
    Album getAlbum(@PathVariable("albumId") String albumId);
}
