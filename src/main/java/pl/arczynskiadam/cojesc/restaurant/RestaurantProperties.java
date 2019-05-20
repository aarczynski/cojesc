package pl.arczynskiadam.cojesc.restaurant;

public class RestaurantProperties {

        private String facebookAlbumId;
        private String[] menuKeyWords;
        private int menuValidity;

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

    public int getMenuValidity() {
        return menuValidity;
    }

    public void setMenuValidity(int menuValidity) {
        this.menuValidity = menuValidity;
    }
}
