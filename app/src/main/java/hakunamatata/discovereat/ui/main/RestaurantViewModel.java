package hakunamatata.discovereat.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import hakunamatata.discovereat.data.repository.restaurants.IRestaurantRepositoryLiveData;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.Result;
public class RestaurantViewModel extends ViewModel {
    private static final String TAG = RestaurantViewModel.class.getSimpleName();
    private final IRestaurantRepositoryLiveData restaurantRepositoryLiveData;
    private MutableLiveData<Result> favoriteRestaurantsListLiveData;
    private MutableLiveData<Result> restaurantListLiveData;
    private int totalResults;
    private boolean isLoading;
    private int currentResults;
    private boolean firstLoading;

    public RestaurantViewModel(IRestaurantRepositoryLiveData restaurantRepositoryLiveData) {
        this.restaurantRepositoryLiveData = restaurantRepositoryLiveData;
        this.totalResults = 0;
        this.firstLoading = true;
    }

    public MutableLiveData<Result> getRestaurants(String location, boolean isRunApi) {
        /*
        if (restaurantListLiveData == null) {
            fetchRestaurants(location, isRunApi);
        }*/
        fetchRestaurants(location, isRunApi);
        return restaurantListLiveData;

    }

    public MutableLiveData<Result> getFavoriteRestaurantsListLiveData(boolean isFirstLoading) {
        if (favoriteRestaurantsListLiveData == null) {
            getFavouriteRestaurants(isFirstLoading);
        }
        return favoriteRestaurantsListLiveData;
    }

    public void fetchRestaurants(String location, boolean isRunApi) {
        restaurantListLiveData = restaurantRepositoryLiveData.fetchRestaurants(location, isRunApi);
    }

    /**
     * Updates the Restaurant status.
     * @param restaurant The restaurant to be updated.
     */
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepositoryLiveData.updateRestaurants(restaurant);
    }

    /**
     * It uses the Repository to get the list of favorite restaurants
     * and to associate it with the LiveData object.
     */
    private void getFavouriteRestaurants(boolean firstLoading) {
        favoriteRestaurantsListLiveData = restaurantRepositoryLiveData.getFavoriteRestaurant(firstLoading);
    }

    /**
     * Removes the restaurants from the list of favorite restaurants.
     * @param restaurant The restaurant to be removed from the list of favorite restaurants.
     */
    public void removeFromFavorite(Restaurant restaurant) {
        restaurantRepositoryLiveData.updateRestaurants(restaurant);
    }

    /**
     * Clears the list of favorite restaurants.
     */
    public void deleteAllFavoriteRestaurants() {
        restaurantRepositoryLiveData.deleteFavoriteRestaurant();
    }


    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isFirstLoading() {
        return firstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }
}
