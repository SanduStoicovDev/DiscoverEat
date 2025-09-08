package hakunamatata.discovereat.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantApiResponse extends RestaurantResponse{

    private String status;
    @SerializedName("businesses")
    private List<Restaurant> restaurants;

    public RestaurantApiResponse() {
    }

    public RestaurantApiResponse(String status, int total, List<Restaurant> restaurants) {
        super(restaurants);
        this.status = status;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

// Getter e setter per tutti i campi
}

