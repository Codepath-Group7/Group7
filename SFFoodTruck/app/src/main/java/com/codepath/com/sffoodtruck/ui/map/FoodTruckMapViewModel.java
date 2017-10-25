package com.codepath.com.sffoodtruck.ui.map;

import com.codepath.com.sffoodtruck.data.model.Business;
import com.codepath.com.sffoodtruck.data.model.Coordinates;
import com.codepath.com.sffoodtruck.data.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robl2e on 10/13/17.
 */

public class FoodTruckMapViewModel {
    private final String id;
    private final Location location;
    private final Float rating;
    private final Coordinates coordinates;
    private final String name;
    private final String imageUrl;
    private final Business business;
    private final String price;

    public FoodTruckMapViewModel(String id, String name, Coordinates coordinates, Location location, Float rating, String imageUrl, String price, Business business) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.location = location;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.price = price;
        this.business = business;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Float getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public Business getBusiness() {
        return business;
    }

    public static FoodTruckMapViewModel convert(Business business) {
        return new FoodTruckMapViewModel(
                business.getId(),
                business.getName(),
                business.getCoordinates(),
                business.getLocation(),
                business.getRating(),
                business.getImageUrl(),
                business.getPrice(),
                business
        );
    }

    public static List<FoodTruckMapViewModel> convert(List<Business> businesses) {
        if (businesses == null) return new ArrayList<>();

        List<FoodTruckMapViewModel> viewModels = new ArrayList<>();
        for (Business business : businesses) {
               viewModels.add(convert(business));
        }
        return viewModels;
    }

}
