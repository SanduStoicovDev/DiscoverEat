package hakunamatata.discovereat.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.model.SearchResponse;
import hakunamatata.discovereat.model.Result;
import hakunamatata.discovereat.model.User;

import hakunamatata.discovereat.data.source.restaurants.BaseRestaurantsLocalDataSource;
import hakunamatata.discovereat.data.source.restaurants.RestaurantsCallback;

import hakunamatata.discovereat.data.source.user.BaseUserAuthenticationRemoteDataSource;
import hakunamatata.discovereat.data.source.user.BaseUserDataRemoteDataSource;


/**
 * Repository class to get the user information.
 */
public class UserRepository implements IUserRepository, UserResponseCallback, RestaurantsCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BaseRestaurantsLocalDataSource restaurantsLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userFavoriteRestaurantsMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BaseRestaurantsLocalDataSource restaurantsLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.restaurantsLocalDataSource = restaurantsLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userFavoriteRestaurantsMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.restaurantsLocalDataSource.setRestaurantsCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserFavoriteRestaurants(String idToken) {
        userDataRemoteDataSource.getUserFavoriteRestaurants(idToken);
        return userFavoriteRestaurantsMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Restaurant> restaurantList) {
        restaurantsLocalDataSource.insertRestaurant(restaurantList);
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        restaurantsLocalDataSource.deleteAll();
    }

    @Override
    public void onSuccessFromLocal(RestaurantApiResponse restaurantApiResponse) {
        //Result.SearchResponseSuccess result = new Result.SearchResponseSuccess(searchResponse);
        //userFavoriteRestaurantsMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessDeletion() {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(null);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromCloudReading(List<Restaurant> restaurantList) {

    }

    @Override
    public void onSuccessFromCloudWriting(Restaurant restaurant) {

    }

    @Override
    public void onFailureFromCloud(Exception exception) {

    }


    @Override
    public void onSuccessSynchronization() {
       // userFavoriteRestaurantsMutableLiveData.postValue(new Result.SearchResponseSuccess(null));
    }

    @Override
    public void onSuccessFromRemote(RestaurantApiResponse restaurantApiResponse) {
        restaurantsLocalDataSource.insertRestaurants(restaurantApiResponse);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }

    @Override
    public void onFailureFromLocal(Exception exception) {

    }

    @Override
    public void onRestaurantFavoriteStatusChanged(Restaurant restaurant, List<Restaurant> favoriteRestaurant) {
    }

    @Override
    public void onRestaurantFavoriteStatusChanged(List<Restaurant> restaurantList) {

    }

    @Override
    public void onDeleteFavoriteRestaurantSuccess(List<Restaurant> favoriteRestaurant) {

    }

}
