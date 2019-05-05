package pl.arczynskiadam.cojesc.http.client.facebook.dto.album;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    List<ImageGroup> data;

    public List<ImageGroup> getData() {
        return data;
    }

    public void setData(List<ImageGroup> data) {
        this.data = data;
    }
}
