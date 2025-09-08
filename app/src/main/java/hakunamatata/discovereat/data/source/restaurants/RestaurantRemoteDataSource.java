package hakunamatata.discovereat.data.source.restaurants;

import static hakunamatata.discovereat.util.Constants.API_KEY_ERROR;
import static hakunamatata.discovereat.util.Constants.RESTAURANTS_API_TERM;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import hakunamatata.discovereat.data.service.RestaurantsApiService;
import hakunamatata.discovereat.model.Location;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRemoteDataSource extends BaseRestaurantRemoteDataSource{

    private static final String TAG = RestaurantRemoteDataSource.class.getSimpleName();
    private final RestaurantsApiService restaurantsApiService;
    private final String apiKey;

    public RestaurantRemoteDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.restaurantsApiService = ServiceLocator.getInstance().getRestaurantsApiService();
    }

    @Override
    public void getRestaurant(String location) {
        long currentTime = System.currentTimeMillis();

        //String auth = application.getString(R.string.yelp_api_key);
        String term = "restaurants";
        List<Restaurant> restaurantList = new ArrayList<>();

        //boolean isRunApi = currentTime - lastUpdate > FRESH_TIMEOUT;

        Log.d(TAG, "Running Restaurants Api");
        Call<RestaurantApiResponse> restaurantsResponseCall = restaurantsApiService.searchRestaurants(apiKey, RESTAURANTS_API_TERM, location, "distance", 50);
        restaurantsResponseCall.enqueue(new Callback<RestaurantApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantApiResponse> call,
                                   @NonNull Response<RestaurantApiResponse> response) {
                Log.d(TAG, "Restaurants Api Response Body : " + response.body());
                Log.d(TAG, "Restaurants Api Response isSuccessful() : " + response.isSuccessful());
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Entrato dentro l'if");
                    List<Restaurant> restaurants = response.body().getRestaurants();
                    Log.d(TAG, "getRestaurants");
                    for (Restaurant restaurant : restaurants) {
                        Location locationObj = restaurant.getLocation();
                        Log.d(TAG, "getLocaltion");
                        //String address = locationObj.getAddress1() + ", " + locationObj.getCity();
                        restaurantList.add(new Restaurant(restaurant.getId(), restaurant.getName(),
                                location, restaurant.getLocation(), restaurant.getPrice(),
                                restaurant.getRating(), restaurant.getImageUrl(), restaurant.isFavorite(),
                                true, restaurant.getPhone(), restaurant.getDisplayPhone(),
                                restaurant.getDistance()));
                        Log.d(TAG, "fatto l'add");
                    }

                   // saveDataInDatabase(restaurantList);
                    response.body().setRestaurants(restaurantList);
                    restaurantsCallback.onSuccessFromRemote(response.body());
                } else {
                    restaurantsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                }
            }
            @Override
            public void onFailure(Call<RestaurantApiResponse> call, Throwable t) {
                restaurantsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
            }
        });

    }
}
