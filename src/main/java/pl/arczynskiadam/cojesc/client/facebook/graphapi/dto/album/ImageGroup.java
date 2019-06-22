package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ImageGroup {
    private String id;
    private List<Image> images;
    @JsonProperty("created_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime createdTime;

    public Image findBiggestImage() {
        return images
                .stream()
                .max(Image::compareSize)
                .orElseThrow(() -> new RuntimeException("Received empty image group from Facebook"));
    }
}
