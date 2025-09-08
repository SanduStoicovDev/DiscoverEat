package hakunamatata.discovereat.ui.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import hakunamatata.discovereat.data.repository.restaurants.IRestaurantRepositoryLiveData;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory{
    private final IRestaurantRepositoryLiveData iRestaurantRepositoryLiveData;

    public RestaurantViewModelFactory(IRestaurantRepositoryLiveData iRestaurantRepositoryLiveData) {
        this.iRestaurantRepositoryLiveData = iRestaurantRepositoryLiveData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RestaurantViewModel(iRestaurantRepositoryLiveData);
    }
}
