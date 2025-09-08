package hakunamatata.discovereat.data.repository.user;

import java.util.List;

import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.User;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
    void onSuccessFromRemoteDatabase(List<Restaurant> restaurantList);
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();
}
