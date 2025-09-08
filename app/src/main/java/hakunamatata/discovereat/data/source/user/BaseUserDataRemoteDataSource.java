package hakunamatata.discovereat.data.source.user;

import hakunamatata.discovereat.data.repository.user.UserResponseCallback;
import hakunamatata.discovereat.model.User;

/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;
    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract void saveUserData(User user);
    public abstract void getUserFavoriteRestaurants(String idToken);
}
