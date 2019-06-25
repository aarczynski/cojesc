package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacebookAlbumRestaurant extends FacebookRestaurant {
    private List<String> facebookAlbumsToSearch;
}
