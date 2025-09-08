package hakunamatata.discovereat.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import hakunamatata.discovereat.model.Result;
import hakunamatata.discovereat.model.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> getUserFavoriteRestaurants(String idToken);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);
}
