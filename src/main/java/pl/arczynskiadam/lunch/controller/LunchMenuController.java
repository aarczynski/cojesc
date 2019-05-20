package pl.arczynskiadam.lunch.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arczynskiadam.lunch.http.client.facebook.converter.RestaurantEnumConverter;
import pl.arczynskiadam.lunch.restaurant.Restaurant;
import pl.arczynskiadam.lunch.service.MenuImageFilterService;

@RestController
@RequestMapping("/menu/lunch")
public class LunchMenuController {

    private MenuImageFilterService menuService;

    public LunchMenuController(MenuImageFilterService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{restaurant}")
    public String lunchMenu(@PathVariable("restaurant") Restaurant restaurant) {
        return menuService.getLunchMenuImageLink(restaurant);
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Restaurant.class, new RestaurantEnumConverter());
    }
}
