package hakunamatata.discovereat.data.source.restaurants;

import static hakunamatata.discovereat.util.Constants.FIREBASE_FAVORITE_RESTAURANTS_COLLECTION;
import static hakunamatata.discovereat.util.Constants.FIREBASE_REALTIME_DATABASE;
import static hakunamatata.discovereat.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hakunamatata.discovereat.model.Restaurant;

/**
 * Class to get the user favorite Restaurants using Firebase Realtime Database.
 */
public class FavoriteRestaurantsDataSource extends BaseFavoriteRestaurantsDataSource {

    private static final String TAG = FavoriteRestaurantsDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;
    private final String idToken;

    public FavoriteRestaurantsDataSource(String idToken) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
        this.idToken = idToken;
    }

    @Override
    public void getFavoriteRestaurants() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RESTAURANTS_COLLECTION).get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error getting data", task.getException());
                    }
                    else {
                        Log.d(TAG, "Successful read: " + task.getResult().getValue());

                        List<Restaurant> restaurantList = new ArrayList<>();
                        for(DataSnapshot ds : task.getResult().getChildren()) {
                            Restaurant restaurant = ds.getValue(Restaurant.class);
                            restaurant.setSynchronized(true);
                            restaurantList.add(restaurant);
                        }

                        restaurantsCallback.onSuccessFromCloudReading(restaurantList);
                    }
                });
    }

    @Override
    public void addFavoriteRestaurant(Restaurant restaurant) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RESTAURANTS_COLLECTION).child(String.valueOf(restaurant.hashCode())).setValue(restaurant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        restaurant.setSynchronized(true);
                        restaurantsCallback.onSuccessFromCloudWriting(restaurant);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        restaurantsCallback.onFailureFromCloud(e);
                    }
                });
    }

    @Override
    public void synchronizeFavoriteRestaurants(List<Restaurant> notSynchronizedRestaurantsList) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RESTAURANTS_COLLECTION).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            Restaurant restaurant = ds.getValue(Restaurant.class);
                            restaurant.setSynchronized(true);
                            restaurantList.add(restaurant);
                        }

                        restaurantList.addAll(notSynchronizedRestaurantsList);

                        for (Restaurant restaurant : restaurantList) {
                            databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                                    child(FIREBASE_FAVORITE_RESTAURANTS_COLLECTION).
                                    child(String.valueOf(restaurant.hashCode())).setValue(restaurant).addOnSuccessListener(
                                            new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    restaurant.setSynchronized(true);
                                                }
                                            }
                                    );
                        }
                    }
                });
    }

    @Override
    public void deleteFavoriteRestaurant(Restaurant restaurant) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RESTAURANTS_COLLECTION).child(String.valueOf(restaurant.hashCode())).
                removeValue().addOnSuccessListener(aVoid -> {
                    restaurant.setSynchronized(false);
                    restaurantsCallback.onSuccessFromCloudWriting(restaurant);
                }).addOnFailureListener(e -> {
                    restaurantsCallback.onFailureFromCloud(e);
                });
    }

    @Override
    public void deleteAllFavoriteRestaurants() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RESTAURANTS_COLLECTION).removeValue().addOnSuccessListener(aVoid -> {
                    restaurantsCallback.onSuccessFromCloudWriting(null);
                }).addOnFailureListener(e -> {
                    restaurantsCallback.onFailureFromCloud(e);
                });
    }
}
