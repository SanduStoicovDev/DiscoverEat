package hakunamatata.discovereat.data.repository.restaurants;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import hakunamatata.discovereat.data.source.restaurants.BaseFavoriteRestaurantsDataSource;
import hakunamatata.discovereat.data.source.restaurants.BaseRestaurantRemoteDataSource;
import hakunamatata.discovereat.data.source.restaurants.BaseRestaurantsLocalDataSource;
import hakunamatata.discovereat.data.source.restaurants.RestaurantsCallback;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.model.RestaurantResponse;
import hakunamatata.discovereat.model.Result;

public class RestaurantRepositoryLiveData implements IRestaurantRepositoryLiveData, RestaurantsCallback {
    private static final String TAG = RestaurantRepositoryLiveData.class.getSimpleName();
    private final MutableLiveData<Result> allRestaurantsMutableLiveData;
    private final MutableLiveData<Result> favoriteRestaurantsMutableLiveData;
    private final BaseRestaurantRemoteDataSource restaurantsRemoteDataSource;
    private final BaseRestaurantsLocalDataSource restaurantsLocalDataSource;
    private final BaseFavoriteRestaurantsDataSource favoriteRestaurantsDataSource;

    public RestaurantRepositoryLiveData(BaseRestaurantRemoteDataSource restaurantRemoteDataSource,
                                        BaseRestaurantsLocalDataSource restaurantLocalDataSource,
                                        BaseFavoriteRestaurantsDataSource favoriteRestaurantsDataSource) {
        allRestaurantsMutableLiveData = new MutableLiveData<>();
        favoriteRestaurantsMutableLiveData = new MutableLiveData<>();
        this.restaurantsRemoteDataSource = restaurantRemoteDataSource;
        this.restaurantsLocalDataSource = restaurantLocalDataSource;
        this.favoriteRestaurantsDataSource = favoriteRestaurantsDataSource;
        this.restaurantsRemoteDataSource.setRestaurantCallback(this);
        this.restaurantsLocalDataSource.setRestaurantsCallback(this);
        this.favoriteRestaurantsDataSource.setRestaurantsCallback(this);
    }

    @Override
    public MutableLiveData<Result> fetchRestaurants(String location, boolean isRunApi) {
        //restaurantsRemoteDataSource.getRestaurant(location);
        if (isRunApi) {
            restaurantsRemoteDataSource.getRestaurant(location);
        } else {
            restaurantsLocalDataSource.getRestaurants(location);
        }
        return allRestaurantsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result>  getFavoriteRestaurant(boolean isFirstLoading) {
        if (isFirstLoading) {
            favoriteRestaurantsDataSource.getFavoriteRestaurants();
        } else {
            restaurantsLocalDataSource.getFavoriteRestaurants();
        }
        return favoriteRestaurantsMutableLiveData;
    }

    @Override
    public void updateRestaurants(Restaurant restaurant) {
        restaurantsLocalDataSource.updateRestaurant(restaurant);
        if (restaurant.isFavorite()) {
            favoriteRestaurantsDataSource.addFavoriteRestaurant(restaurant);
        } else {
            favoriteRestaurantsDataSource.deleteFavoriteRestaurant(restaurant);
        }
    }

    @Override
    public void deleteFavoriteRestaurant() {
       restaurantsLocalDataSource.deleteFavoriteRestaurant();
    }

    @Override
    public void onSuccessFromRemote(RestaurantApiResponse restaurantApiResponse) {
        restaurantsLocalDataSource.insertRestaurants(restaurantApiResponse);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        allRestaurantsMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocal(RestaurantApiResponse restaurantApiResponse) {
        if (allRestaurantsMutableLiveData.getValue() != null && allRestaurantsMutableLiveData.getValue().isSuccess()) {
            List<Restaurant> restaurantsList = ((Result.RestaurantResponseSuccess)allRestaurantsMutableLiveData.getValue()).getData().getRestaurants();
            restaurantsList.addAll(restaurantApiResponse.getRestaurants());
            restaurantApiResponse.setRestaurants(restaurantsList);
            Result.RestaurantResponseSuccess result = new Result.RestaurantResponseSuccess(restaurantApiResponse);
            allRestaurantsMutableLiveData.postValue(result);
        } else {
            Result.RestaurantResponseSuccess result = new Result.RestaurantResponseSuccess(restaurantApiResponse);
            allRestaurantsMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allRestaurantsMutableLiveData.postValue(resultError);
        favoriteRestaurantsMutableLiveData.postValue(resultError);
    }

    @Override
    public void onRestaurantFavoriteStatusChanged(Restaurant restaurant, List<Restaurant> restaurantList) {

        Result allRestaurantResult =  allRestaurantsMutableLiveData.getValue();

        if (allRestaurantResult != null && allRestaurantResult.isSuccess()) {
            List<Restaurant> oldAllRestaurants = ((Result.RestaurantResponseSuccess)allRestaurantResult).getData().getRestaurants();
            if (oldAllRestaurants.contains(restaurant)) {
                oldAllRestaurants.set(oldAllRestaurants.indexOf(restaurant), restaurant);
                allRestaurantsMutableLiveData.postValue(allRestaurantResult);
            }
        }
        favoriteRestaurantsMutableLiveData.postValue(new Result.RestaurantResponseSuccess(new RestaurantResponse(restaurantList)));
        //favoriteRestaurantsMutableLiveData.postValue(new Result.SearchResponseSuccess(new SearchResponse(restaurantList)))
    }

    @Override
    public void onRestaurantFavoriteStatusChanged(List<Restaurant> restaurantList) {
        List<Restaurant> notSynchronizedRestaurantList = new ArrayList<>();

        for (Restaurant restaurant : restaurantList) {
            if (!restaurant.isSynchronized()) {
                notSynchronizedRestaurantList.add(restaurant);
            }
        }

        if (!notSynchronizedRestaurantList.isEmpty()) {
            favoriteRestaurantsDataSource.synchronizeFavoriteRestaurants(notSynchronizedRestaurantList);
        }

        favoriteRestaurantsMutableLiveData.postValue(new Result.RestaurantResponseSuccess(new RestaurantResponse(restaurantList)));
    }

    @Override
    public void onDeleteFavoriteRestaurantSuccess(List<Restaurant> favoriteRestaurants) {
        Result allRestaurantResult = allRestaurantsMutableLiveData.getValue();

        if (allRestaurantResult != null && allRestaurantResult.isSuccess()) {
            List<Restaurant> oldAllRestaurants = ((Result.RestaurantResponseSuccess)allRestaurantResult).getData().getRestaurants();
            for (Restaurant restaurant : favoriteRestaurants) {
                if (oldAllRestaurants.contains(restaurant)) {
                    oldAllRestaurants.set(oldAllRestaurants.indexOf(restaurant), restaurant);
                }
            }
            allRestaurantsMutableLiveData.postValue(allRestaurantResult);
        }

        if (favoriteRestaurantsMutableLiveData.getValue() != null &&
                favoriteRestaurantsMutableLiveData.getValue().isSuccess()) {
            favoriteRestaurants.clear();
            Result.RestaurantResponseSuccess result = new Result.RestaurantResponseSuccess(new RestaurantResponse(favoriteRestaurants));
            favoriteRestaurantsMutableLiveData.postValue(result);
        }

        favoriteRestaurantsDataSource.deleteAllFavoriteRestaurants();
    }

    @Override
    public void onSuccessFromCloudReading(List<Restaurant> restaurantList) {
        // Favorite restaurants got from Realtime Database the first time
        if (restaurantList != null) {
            for (Restaurant restaurant : restaurantList) {
                restaurant.setSynchronized(true);
            }
            restaurantsLocalDataSource.insertRestaurant(restaurantList);
            favoriteRestaurantsMutableLiveData.postValue(new Result.RestaurantResponseSuccess(new RestaurantResponse(restaurantList)));
        }
    }

    @Override
    public void onSuccessFromCloudWriting(Restaurant restaurant) {
        if (restaurant != null && !restaurant.isFavorite()) {
            restaurant.setSynchronized(false);
        }
        restaurantsLocalDataSource.updateRestaurant(restaurant);
        favoriteRestaurantsDataSource.getFavoriteRestaurants();
    }

    @Override
    public void onFailureFromCloud(Exception exception) {
        //  errorMessages.postValue(exception.getMessage()); TO DO
    }

    @Override
    public void onSuccessSynchronization() {
        ///successMessageLiveData.postValue(true); TO DO
    }

    @Override
    public void onSuccessDeletion() {
        //successMessageLiveData.postValue(true); TO DO
    }

}
