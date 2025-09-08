package hakunamatata.discovereat.data.database;

import static hakunamatata.discovereat.util.Constants.RESTAURANTS_DATABASE_NAME;
import static hakunamatata.discovereat.util.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hakunamatata.discovereat.model.LocationConverter;
import hakunamatata.discovereat.model.Restaurant;

/**
 * Main access point for the underlying connection to the local database.
 */
@Database(entities = {Restaurant.class}, version = DATABASE_VERSION)
@TypeConverters({LocationConverter.class})
public abstract class RestaurantsRoomDatabase extends RoomDatabase{
    public abstract RestaurantsDao restaurantsDao();
    private static volatile RestaurantsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RestaurantsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RestaurantsRoomDatabase.class, RESTAURANTS_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}


