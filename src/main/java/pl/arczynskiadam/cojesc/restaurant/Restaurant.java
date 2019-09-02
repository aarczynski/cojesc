package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;

import java.net.URL;
import java.time.Duration;

@Data
public class Restaurant {
    private String name;
    private Duration menuDuration;
    private URL menuUr;
}
