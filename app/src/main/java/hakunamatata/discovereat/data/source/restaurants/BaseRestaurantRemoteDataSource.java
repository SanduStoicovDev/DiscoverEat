package hakunamatata.discovereat.data.source.restaurants;

public abstract class BaseRestaurantRemoteDataSource {
    protected RestaurantsCallback restaurantsCallback;

    public void setRestaurantCallback(RestaurantsCallback restaurantsCallback) {
        this.restaurantsCallback = restaurantsCallback;
    }

    public abstract void getRestaurant(String country);
}
