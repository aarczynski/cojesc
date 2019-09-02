package pl.arczynskiadam.cojesc.restaurant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.net.URL;
import java.time.Duration;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurant {
    private String id;
    private String name;
    private Duration menuDuration;
    private URL menuUr;
}
