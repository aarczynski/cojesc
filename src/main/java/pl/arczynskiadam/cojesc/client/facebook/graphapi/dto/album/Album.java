package pl.arczynskiadam.cojesc.client.facebook.graphapi.dto.album;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    private List<ImageGroup> data;
}
