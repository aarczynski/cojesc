package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Comparator;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {
    List<Post> data;

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }

    public static Comparator<Post> newest() {
        return Comparator.comparing(post -> post.getCreatedTime());
    }
}