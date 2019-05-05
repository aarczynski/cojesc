package pl.arczynskiadam.cojesc.http.client.facebook.dto.album;

public class Image {
    int width;
    int height;
    String source;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static int compareSize(Image a, Image b) {
        int sizeA = a.getHeight() * a.getWidth();
        int sizeB = b.getHeight() * a.getWidth();
        return Integer.compare(sizeA, sizeB);
    }
}
