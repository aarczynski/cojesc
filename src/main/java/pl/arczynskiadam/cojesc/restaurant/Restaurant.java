package pl.arczynskiadam.cojesc.restaurant;

import lombok.Data;

@Data
public abstract class Restaurant {
    private String name;
    private int menuDuration;
}
