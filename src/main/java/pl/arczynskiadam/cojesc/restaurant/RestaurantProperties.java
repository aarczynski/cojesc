package pl.arczynskiadam.cojesc.restaurant;

public class RestaurantProperties {

    private String facebookAlbumId;
    private String[] menuKeyWords;
    private int menuDuration;

    public String getFacebookAlbumId() {
        return facebookAlbumId;
    }

    public void setFacebookAlbumId(String facebookAlbumId) {
        this.facebookAlbumId = facebookAlbumId;
    }

    public String[] getMenuKeyWords() {
        return menuKeyWords;
    }

    public void setMenuKeyWords(String[] menuKeyWords) {
        this.menuKeyWords = menuKeyWords;
    }

    public int getMenuDuration() {
        return menuDuration;
    }

    public void setMenuDuration(int menuDuration) {
        this.menuDuration = menuDuration;
    }
}
