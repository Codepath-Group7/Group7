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
    private String name;

    public FoodTruckMapViewModel(String id, String name, Coordinates coordinates, Location location, Float rating) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.location = location;
        this.rating = rating;
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

    public static FoodTruckMapViewModel convert(Business business) {
        return new FoodTruckMapViewModel(
                business.getId(),
                business.getName(),
                business.getCoordinates(),
                business.getLocation(),
                business.getRating()
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
