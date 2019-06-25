package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacebookAlbumRestaurant extends FacebookRestaurant {
    private String facebookAlbumsToSearch;
}
