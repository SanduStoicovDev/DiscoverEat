package hakunamatata.discovereat.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import hakunamatata.discovereat.model.Restaurant;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 */
@Dao
public interface RestaurantsDao {
    @Query("SELECT * FROM restaurant")
    List<Restaurant> getAll();

    @Query("SELECT * FROM restaurant WHERE is_synchronized = 1")
    List<Restaurant> getRestaurantsLastUpdate();

    @Query("SELECT * FROM restaurant WHERE locationSearch LIKE '%' || :location || '%'")
    List<Restaurant> getRestaurantsLastSearch(String location);

    @Query("SELECT * FROM restaurant WHERE id = :id")
    Restaurant getRestaurants(long id);

    @Query("SELECT * FROM restaurant WHERE is_favorite = 1 ORDER BY id DESC")
    List<Restaurant> getFavoriteRestaurants();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRestaurantsList(List<Restaurant> restaurantList);

    //@Insert
    //void insertAll(Restaurant... restaurants);

    @Delete
    void delete(Restaurant restaurant);

    @Query("DELETE FROM restaurant")
    int deleteAll();

    @Query("DELETE FROM restaurant WHERE is_favorite = 0")
    void deleteNotFavoriteRestaurants();

    //@Delete
    //void deleteAllWithoutQuery(Restaurant... restaurants);

    @Update
    int updateSingleFavoriteRestaurant(Restaurant restaurant);

    @Update
    int updateListFavoriteRestaurant(List<Restaurant> restaurantList);
}

