package hakunamatata.discovereat.model;

import java.util.List;

public class RestaurantResponse {
    private List<Restaurant> restaurants;
    private int total;

    public RestaurantResponse() {
    }

    public RestaurantResponse(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}