package pl.arczynskiadam.cojesc.http.client.facebook.converter;

import pl.arczynskiadam.cojesc.restaurant.Restaurant;

import java.beans.PropertyEditorSupport;

public class RestaurantEnumConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Restaurant restaurant = Restaurant.valueOf(text.toUpperCase());
        setValue(restaurant);
    }
}