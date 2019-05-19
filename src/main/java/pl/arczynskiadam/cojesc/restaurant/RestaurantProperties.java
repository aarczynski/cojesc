package pl.arczynskiadam.cojesc.restaurant;

public class RestaurantProperties {

        private String facebookAlbumId;
        private String[] keyWords;
        private int menuValidity;

    public String getFacebookAlbumId() {
        return facebookAlbumId;
    }

    public void setFacebookAlbumId(String facebookAlbumId) {
        this.facebookAlbumId = facebookAlbumId;
    }

    public String[] getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String[] keyWords) {
        this.keyWords = keyWords;
    }

    public int getMenuValidity() {
        return menuValidity;
    }

    public void setMenuValidity(int menuValidity) {
        this.menuValidity = menuValidity;
    }
}
