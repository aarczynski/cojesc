package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album;

import java.util.Objects;

public class Image {
    private int width;
    private int height;
    private String source;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSource() {
        return source;
    }

    public static int compareSize(Image a, Image b) {
        int sizeA = a.getHeight() * a.getWidth();
        int sizeB = b.getHeight() * b.getWidth();
        return Integer.compare(sizeA, sizeB);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return width == image.width &&
                height == image.height &&
                Objects.equals(source, image.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, source);
    }
}
