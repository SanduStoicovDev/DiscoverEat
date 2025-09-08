package hakunamatata.discovereat.util;
import android.app.Application;

import static hakunamatata.discovereat.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static hakunamatata.discovereat.util.Constants.ID_TOKEN;

import java.io.IOException;
import java.security.GeneralSecurityException;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.data.database.RestaurantsRoomDatabase;
import hakunamatata.discovereat.data.repository.restaurants.IRestaurantRepositoryLiveData;
import hakunamatata.discovereat.data.repository.restaurants.RestaurantRepositoryLiveData;
import hakunamatata.discovereat.data.repository.user.IUserRepository;
import hakunamatata.discovereat.data.repository.user.UserRepository;
import hakunamatata.discovereat.data.service.RestaurantsApiService;
import hakunamatata.discovereat.data.source.restaurants.BaseFavoriteRestaurantsDataSource;
import hakunamatata.discovereat.data.source.restaurants.BaseRestaurantRemoteDataSource;
import hakunamatata.discovereat.data.source.restaurants.BaseRestaurantsLocalDataSource;
import hakunamatata.discovereat.data.source.restaurants.FavoriteRestaurantsDataSource;
import hakunamatata.discovereat.data.source.restaurants.RestaurantLocalDataSource;
import hakunamatata.discovereat.data.source.restaurants.RestaurantRemoteDataSource;
import hakunamatata.discovereat.data.source.user.BaseUserAuthenticationRemoteDataSource;
import hakunamatata.discovereat.data.source.user.BaseUserDataRemoteDataSource;
import hakunamatata.discovereat.data.source.user.UserAuthenticationRemoteDataSource;
import hakunamatata.discovereat.data.source.user.UserDataRemoteDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;
    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public RestaurantsApiService getRestaurantsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.RESTAURANTS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(RestaurantsApiService.class);
    }
    public RestaurantsRoomDatabase getRestaurantsDao(Application application) {
        return RestaurantsRoomDatabase.getDatabase(application);
    }

    /**
     * Returns an instance of IRestaurantRepositoryLiveData.
     * @param application Param for accessing the global application state.
     * @return An instance of IRestaurantRepositoryLiveData.
     */
    public IRestaurantRepositoryLiveData getRestaurantsRepository(Application application) {
        BaseRestaurantRemoteDataSource restaurantRemoteDataSource;
        BaseRestaurantsLocalDataSource restaurantsLocalDataSource;
        BaseFavoriteRestaurantsDataSource favoriteRestaurantsDataSource;
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);
        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        restaurantRemoteDataSource =
                new RestaurantRemoteDataSource(application.getString(R.string.restaurant_api_key));

        restaurantsLocalDataSource = new RestaurantLocalDataSource(getRestaurantsDao(application),
                sharedPreferencesUtil);

        try {
            favoriteRestaurantsDataSource = new FavoriteRestaurantsDataSource(dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN
                    )
            );
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }

        return new RestaurantRepositoryLiveData(restaurantRemoteDataSource,
                restaurantsLocalDataSource, favoriteRestaurantsDataSource);
    }

    /**
     * Creates an instance of IUserRepository.
     * @return An instance of IUserRepository.
     */
    public IUserRepository getUserRepository(Application application) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource(sharedPreferencesUtil);

        BaseRestaurantsLocalDataSource restaurantsLocalDataSource =
                new RestaurantLocalDataSource(getRestaurantsDao(application), sharedPreferencesUtil);

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource, restaurantsLocalDataSource);
    }
}
