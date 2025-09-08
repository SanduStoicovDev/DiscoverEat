package hakunamatata.discovereat.data.source.restaurants;

import static hakunamatata.discovereat.util.Constants.LAST_UPDATE;
import static hakunamatata.discovereat.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static hakunamatata.discovereat.util.Constants.UNEXPECTED_ERROR;

import java.util.List;

import hakunamatata.discovereat.data.database.RestaurantsDao;
import hakunamatata.discovereat.data.database.RestaurantsRoomDatabase;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.util.SharedPreferencesUtil;

/**
 * Class to get restaurants from local database using Room.
 */
public class RestaurantLocalDataSource extends BaseRestaurantsLocalDataSource {

    private final RestaurantsDao restaurantsDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public RestaurantLocalDataSource(RestaurantsRoomDatabase restaurantsRoomDatabase,
                               SharedPreferencesUtil sharedPreferencesUtil) {
        this.restaurantsDao = restaurantsRoomDatabase.restaurantsDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    /**
     * Gets the restaurants from the local database.
     * The method is executed with an ExecutorService defined in restaurantsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getRestaurants(String location) {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            RestaurantApiResponse restaurantApiResponse = new RestaurantApiResponse();
            restaurantApiResponse.setRestaurants(restaurantsDao.getRestaurantsLastSearch(location));
            restaurantsCallback.onSuccessFromLocal(restaurantApiResponse);
        });
    }

    @Override
    public void getFavoriteRestaurants() {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Restaurant> favoriterestaurants = restaurantsDao.getFavoriteRestaurants();
            restaurantsCallback.onRestaurantFavoriteStatusChanged(favoriterestaurants);
        });
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (restaurant != null) {
                int rowUpdatedCounter = restaurantsDao.updateSingleFavoriteRestaurant(restaurant);
                // It means that the update succeeded because only one row had to be updated
                if (rowUpdatedCounter == 1) {
                    Restaurant updatedrestaurants = restaurantsDao.getRestaurants(restaurant.getIdDb());
                    restaurantsCallback.onRestaurantFavoriteStatusChanged(updatedrestaurants, restaurantsDao.getFavoriteRestaurants());
                } else {
                    restaurantsCallback.onFailureFromLocal(new Exception(UNEXPECTED_ERROR));
                }
            } else {
                // When the user deleted all favorite restaurants from remote
                //TODO Check if it works fine and there are not drawbacks
                List<Restaurant> allRestaurants = restaurantsDao.getAll();
                for (Restaurant r : allRestaurants) {
                    r.setSynchronized(false);
                    restaurantsDao.updateSingleFavoriteRestaurant(r);
                }
            }
        });
    }

    @Override
    public void deleteFavoriteRestaurant() {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Restaurant> favoriteRestaurants = restaurantsDao.getFavoriteRestaurants();
            for (Restaurant restaurant : favoriteRestaurants) {
                restaurant.setFavorite(false);
            }
            int updatedRowsNumber = restaurantsDao.updateListFavoriteRestaurant(favoriteRestaurants);

            // It means that the update succeeded because the number of updated rows is
            // equal to the number of the original favorite restaurants
            if (updatedRowsNumber == favoriteRestaurants.size()) {
                restaurantsCallback.onDeleteFavoriteRestaurantSuccess(favoriteRestaurants);
            } else {
                restaurantsCallback.onFailureFromLocal(new Exception(UNEXPECTED_ERROR));
            }
        });
    }

    /**
     * Saves the restaurants in the local database.
     * The method is executed with an ExecutorService defined in restaurantsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param restaurantApiResponse the list of restaurants to be written in the local database.
     */
    @Override
    public void insertRestaurants(RestaurantApiResponse restaurantApiResponse) {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the restaurants from the database
            List<Restaurant> allRestaurants = restaurantsDao.getAll();
            List<Restaurant> restaurantList = restaurantApiResponse.getRestaurants();

            if (restaurantList != null) {

                // Checks if the restaurants just downloaded has already been downloaded earlier
                // in order to preserve the restaurants status (marked as favorite or not)
                for (Restaurant restaurant : allRestaurants) {
                    // This check works because restaurants and restaurantsSource classes have their own
                    // implementation of equals(Object) and hashCode() methods
                    if (restaurantList.contains(restaurant)) {
                        // The primary key and the favorite status is contained only in the restaurants objects
                        // retrieved from the database, and not in the restaurants objects downloaded from the
                        // Web Service. If the same restaurants was already downloaded earlier, the following
                        // line of code replaces the the restaurants object in restaurantsList with the corresponding
                        // restaurants object saved in the database, so that it has the primary key and the
                        // favorite status.
                        restaurantList.set(restaurantList.indexOf(restaurant), restaurant);
                    }
                }

                // Writes the restaurants in the database and gets the associated primary keys
                List<Long> insertedRestaurantsIds = restaurantsDao.insertRestaurantsList(restaurantList);
                for (int i = 0; i < restaurantList.size(); i++) {
                    // Adds the primary key to the corresponding object restaurants just downloaded so that
                    // if the user marks the restaurants as favorite (and vice-versa), we can use its id
                    // to know which restaurants in the database must be marked as favorite/not favorite
                    restaurantList.get(i).setId(String.valueOf(insertedRestaurantsIds.get(i)));
                }

                sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                        LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

                restaurantsCallback.onSuccessFromLocal(restaurantApiResponse);
            }
        });
    }

    /**
     * Saves the restaurants in the local database.
     * The method is executed with an ExecutorService defined in restaurantsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param restaurantList the list of restaurants to be written in the local database.
     */
    @Override
    public void insertRestaurant(List<Restaurant> restaurantList) {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (restaurantList != null) {

                // Reads the restaurants from the database
                List<Restaurant> allRestaurants = restaurantsDao.getAll();

                // Checks if the restaurants just downloaded has already been downloaded earlier
                // in order to preserve the restaurants status (marked as favorite or not)
                for (Restaurant restaurant : allRestaurants) {
                    // This check works because restaurants and restaurantsSource classes have their own
                    // implementation of equals(Object) and hashCode() methods
                    if (restaurantList.contains(restaurant)) {
                        // The primary key and the favorite status is contained only in the restaurants objects
                        // retrieved from the database, and not in the restaurants objects downloaded from the
                        // Web Service. If the same restaurants was already downloaded earlier, the following
                        // line of code replaces the the restaurants object in restaurantsList with the corresponding
                        // restaurants object saved in the database, so that it has the primary key and the
                        // favorite status.
                        restaurant.setSynchronized(true);
                        restaurantList.set(restaurantList.indexOf(restaurant), restaurant);
                    }
                }

                // Writes the restaurants in the database and gets the associated primary keys
                List<Long> insertedRestaurantIds = restaurantsDao.insertRestaurantsList(restaurantList);
                for (int i = 0; i < restaurantList.size(); i++) {
                    // Adds the primary key to the corresponding object restaurants just downloaded so that
                    // if the user marks the restaurants as favorite (and vice-versa), we can use its id
                    // to know which restaurants in the database must be marked as favorite/not favorite
                    restaurantList.get(i).setId(String.valueOf(insertedRestaurantIds.get(i)));
                }

                RestaurantApiResponse restaurantApiResponse = new RestaurantApiResponse();
                restaurantApiResponse.setRestaurants(restaurantList);
                restaurantsCallback.onSuccessSynchronization();
            }
        });
    }

    @Override
    public void deleteAll() {
        RestaurantsRoomDatabase.databaseWriteExecutor.execute(() -> {
            int restaurantsCounter = restaurantsDao.getAll().size();
            int restaurantsDeleted = restaurantsDao.deleteAll();

            restaurantsCallback.onSuccessDeletion();
        });
    }
}
