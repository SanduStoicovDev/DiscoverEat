package hakunamatata.discovereat.data.repository.restaurants;

import static hakunamatata.discovereat.util.Constants.RESTAURANTS_API_TERM;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import hakunamatata.discovereat.R;

import hakunamatata.discovereat.data.database.RestaurantsDao;
import hakunamatata.discovereat.data.database.RestaurantsRoomDatabase;
import hakunamatata.discovereat.model.Location;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.data.service.RestaurantsApiService;
import hakunamatata.discovereat.util.ResponseCallback;
import hakunamatata.discovereat.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsRepository implements IRestaurantRepository{
    private static final String TAG = RestaurantsRepository.class.getSimpleName();
    private final Application application;
    private final RestaurantsApiService restaurantsApiService;
    private final RestaurantsDao restaurantsDao;
    private final ResponseCallback responseCallback;
    private List<Restaurant> restaurantList;


    public RestaurantsRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.restaurantsApiService = ServiceLocator.getInstance().getRestaurantsApiService();
        RestaurantsRoomDatabase restaurantsRoomDatabase = ServiceLocator.getInstance().getRestaurantsDao(application);
        this.restaurantsDao = restaurantsRoomDatabase.restaurantsDao();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchRestaurants(String location, boolean isRunApi) {

        String auth = application.getString(R.string.restaurant_api_key);
        restaurantList = new ArrayList<>();

        /* Gestione chiamata a tempo non necessaria
        long currentTime = System.currentTimeMillis();
        boolean isRunApi = currentTime - lastUpdate > FRESH_TIMEOUT;
        Log.d(TAG, "Current Time is: " + currentTime + " and last Update was : " + lastUpdate);
        Log.d(TAG, "Difference Time is : " + (currentTime - lastUpdate));
        Log.d(TAG, "fresh : " + FRESH_TIMEOUT);
        isRunApi= true;
         */

        if (isRunApi)  {
            Log.d(TAG, "Running Restaurants Api");
            Call<RestaurantApiResponse> restaurantsResponseCall = restaurantsApiService.searchRestaurants(auth, RESTAURANTS_API_TERM, location, "distance", 50);
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
                            Location location = restaurant.getLocation();
                            Log.d(TAG, "getLOcaltion");
                            String address = location.getAddress1() + ", " + location.getCity();
                            restaurantList.add(new Restaurant(restaurant.getId(), restaurant.getName(),
                                    restaurant.getLocationString(), restaurant.getLocation(), restaurant.getPrice(),
                                    restaurant.getRating(), restaurant.getImageUrl(), restaurant.isFavorite(),
                                    true, restaurant.getPhone(), restaurant.getDisplayPhone(),
                                    restaurant.getDistance()));
                            Log.d(TAG, "fatto l'add");
                        }
                        saveDataInDatabase(restaurantList);
                        Log.d(TAG, "saved");
                    } else {
                        //responseCallback.onFailure(application.getString(R.string.error_retrieving_restaurants));
                    }
                }
                @Override
                public void onFailure(Call<RestaurantApiResponse> call, Throwable t) {
                    Log.d(TAG, "Failure Api");
                }
            });
        } else {
            Log.d(TAG, "Reading Data from Database");
            readDataFromDatabase();
        }
    }

    /**
     * Update the restaurants changing the status of "favorite"
     * in the local database.
     * @param restaurant The restaurant to be updated.
     */
    @Override
    public void updateRestaurants(Restaurant restaurant) {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            restaurantsDao.updateSingleFavoriteRestaurant(restaurant);
            responseCallback.onRestaurantsFavoriteStatusChanged(restaurant);
        });
    }

    /**
     * Gets the list of favorite restaurants from the local database.
     */
    @Override
    public void getFavoriteRestaurant() {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(restaurantsDao.getFavoriteRestaurants(), System.currentTimeMillis());
        });
    }

    @Override
    public void deleteFavoriteRestaurant() {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Restaurant> favoriteRestaurants = restaurantsDao.getFavoriteRestaurants();
            for (Restaurant restaurant : favoriteRestaurants) {
                restaurant.setFavorite(false);
            }
            restaurantsDao.updateListFavoriteRestaurant(favoriteRestaurants);
            responseCallback.onSuccess(restaurantsDao.getFavoriteRestaurants(), System.currentTimeMillis());
        });
    }

    /**
     * Gets the restaurants from the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     */
    private void readDataFromDatabase() {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(restaurantsDao.getRestaurantsLastUpdate(), System.currentTimeMillis());
        });
    }

    /**
     * Saves the restaurants in the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     * @param restaurantList the list of restaurants to be written in the local database.
     */
    private void saveDataInDatabase(List<Restaurant> restaurantList) {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            restaurantsDao.deleteNotFavoriteRestaurants();
            // Reads the restaurants from the database
            List<Restaurant> restaurantsDBlist = restaurantsDao.getAll();
            Log.d(TAG, "Dimensione DB get All" + restaurantsDBlist.size());
            for (Restaurant restaurant : restaurantsDBlist) {
                //Log.d(TAG, "E' entrato nel FOR con restaurant " + restaurant + " confronto con" +
                //        "DBlist : " + restaurantsDBlist);
                if (restaurantList.contains(restaurant)) {
                    //Log.d(TAG, "E' entrato nel IF CONTAINS");
                    restaurantList.set(restaurantList.indexOf(restaurant), restaurant);
                }
            }

            // Writes the restaurants in the database and gets the associated primary keys
            List<Long> insertedRestaurantIds = restaurantsDao.insertRestaurantsList(restaurantList);
            for (int i = 0; i < restaurantList.size(); i++) {
                restaurantList.get(i).setIdDb(insertedRestaurantIds.get(i));
            }

            responseCallback.onSuccess(restaurantList, System.currentTimeMillis());
        });
    }
}
