package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FacebookAlbumRestaurant extends Restaurant {
    private String facebookAlbumId;
}
