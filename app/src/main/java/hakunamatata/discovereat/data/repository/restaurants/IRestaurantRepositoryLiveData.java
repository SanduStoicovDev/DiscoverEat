package hakunamatata.discovereat.data.repository.restaurants;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.Result;

public interface IRestaurantRepositoryLiveData {
    MutableLiveData<Result>  fetchRestaurants(String location, boolean isRunApi);
    MutableLiveData<Result>  getFavoriteRestaurant(boolean firstLoading);
    void updateRestaurants(Restaurant restaurant);

    void deleteFavoriteRestaurant();
}

