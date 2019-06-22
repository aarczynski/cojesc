package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Image {
    private int width;
    private int height;
    private String source;

    public static int compareSize(Image a, Image b) {
        int sizeA = a.getHeight() * a.getWidth();
        int sizeB = b.getHeight() * b.getWidth();
        return Integer.compare(sizeA, sizeB);
    }
}
