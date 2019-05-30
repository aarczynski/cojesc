package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class ImageGroup {
    private String id;
    private List<Image> images;
    @JsonProperty("created_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime createdTime;

    public String getId() {
        return id;
    }

    public List<Image> getImages() {
        return images;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public Image findBiggestImage() {
        return images
                .stream()
                .max(Image::compareSize)
                .orElseThrow(() -> new RuntimeException("Received empty image group from Facebook"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageGroup that = (ImageGroup) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(images, that.images) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, images, createdTime);
    }
}
