package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {
    List<Post> data;
}