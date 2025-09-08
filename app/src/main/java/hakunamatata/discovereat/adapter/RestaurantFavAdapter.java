package hakunamatata.discovereat.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.util.List;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.ui.login.LoginActivity;

/**
 * Custom adapter that extends ArrayAdapter to show an ArrayList of Restaurants.
 */
public class RestaurantFavAdapter extends ArrayAdapter<Restaurant> {
    private final List<Restaurant> restaurantList;
    private final int layout;
    private final OnFavoriteButtonClickListener onFavoriteButtonClickListener;

    /**
     * Interface to associate a listener to other elements defined in the layout
     * chosen for the ListView item (e.g., a Button).
     */
    public interface OnFavoriteButtonClickListener {
        void onFavoriteButtonClick(Restaurant restaurant);
    }

    public RestaurantFavAdapter(@NonNull Context context, int layout, @NonNull List<Restaurant> restaurantList,
                           OnFavoriteButtonClickListener onDeleteButtonClickListener) {
        super(context, layout, restaurantList);
        this.layout = layout;
        this.restaurantList = restaurantList;
        this.onFavoriteButtonClickListener = onDeleteButtonClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(layout, parent, false);
        }

        Restaurant restaurant = restaurantList.get(position);
        TextView textViewRestaurantName = convertView.findViewById(R.id.textViewFavRestaurantName);
        ImageView imageViewFavoriteRestaurantsPhoto = convertView.findViewById(R.id.imageViewFavRestaurant);
        ImageView imageViewFavoriteRestaurants = convertView.findViewById(R.id.imageview_favorite_restaurants_fav);

        imageViewFavoriteRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteButtonClickListener.onFavoriteButtonClick(restaurantList.get(position));
            }
        });

        imageViewFavoriteRestaurants.setImageDrawable(AppCompatResources.getDrawable(this.getContext(),
                        R.drawable.ic_baseline_favorite_24));
        imageViewFavoriteRestaurants.setColorFilter(ContextCompat.getColor(
                        imageViewFavoriteRestaurants.getContext(),
                        R.color.red));


        // Verifica se il percorso dell'immagine non è vuoto o nullo
        if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().isEmpty()) {
            Picasso.get().load(restaurant.getImageUrl()).into(imageViewFavoriteRestaurantsPhoto);

        } else {
            // Nel caso in cui il percorso dell'immagine sia vuoto o nullo, imposta l'immagine di fallback
            imageViewFavoriteRestaurantsPhoto.setImageResource(R.drawable.image_not_found_black); // R.drawable.default_image è l'immagine di fallback
        }
        textViewRestaurantName.setText(restaurantList.get(position).getName());

        return convertView;
    }

}
