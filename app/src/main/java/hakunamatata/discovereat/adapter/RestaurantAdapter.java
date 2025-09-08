package hakunamatata.discovereat.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.model.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onRestaurantItemClick(Restaurant restaurant);
        void onFavoriteButtonPressed(int position);
    }
    private List<Restaurant> restaurantList;
    private Context context;
    private final Application application;

    private final OnItemClickListener onItemClickListener;

    public RestaurantAdapter(List<Restaurant> restaurantList, Application application,
                             OnItemClickListener onItemClickListener) {
        this.restaurantList = restaurantList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.textViewRestaurantName.setText(restaurant.getName());
        holder.textViewRestaurantName.setTextColor(Color.parseColor("white"));

        // Verifica se il percorso dell'immagine non è vuoto o nullo
        if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().isEmpty()) {
            Picasso.get().load(restaurant.getImageUrl()).into(holder.imageViewRestaurant);
        } else {
            // Nel caso in cui il percorso dell'immagine sia vuoto o nullo, imposta l'immagine di fallback
            holder.imageViewRestaurant.setImageResource(R.drawable.image_not_found); // R.drawable.default_image è l'immagine di fallback
        }
        holder.bind(restaurantList.get(position));
    }

    @Override
    public int getItemCount() {
        if (restaurantList != null) {
            return restaurantList.size();
        }
        return 0;
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewRestaurantName;
        private final ImageView imageViewRestaurant;
        //private final ImageView imageViewFavoriteRestaurants;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRestaurantName = itemView.findViewById(R.id.textViewRestaurantName);
            imageViewRestaurant = itemView.findViewById(R.id.imageViewRestaurant);
            //imageViewFavoriteRestaurants = itemView.findViewById(R.id.imageview_favorite_restaurants);
            itemView.setOnClickListener(this);
            //imageViewFavoriteRestaurants.setOnClickListener(this);
        }

        public void bind(Restaurant restaurant) {
            textViewRestaurantName.setText(restaurant.getName());
            setImageViewFavoriteRestaurants(restaurantList.get(getAdapterPosition()).isFavorite());
        }
        @Override
        public void onClick(View v) {
            /*if (v.getId() == R.id.imageview_favorite_restaurants) {
                setImageViewFavoriteRestaurants(!restaurantList.get(getAdapterPosition()).isFavorite());
                onItemClickListener.onFavoriteButtonPressed(getAdapterPosition());
            } else {
                onItemClickListener.onRestaurantItemClick(restaurantList.get(getAdapterPosition()));
            }*/
        }
        private void setImageViewFavoriteRestaurants(boolean isFavorite) {
            /*if (isFavorite) {
                imageViewFavoriteRestaurants.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_baseline_favorite_24));
                imageViewFavoriteRestaurants.setColorFilter(
                        ContextCompat.getColor(
                                imageViewFavoriteRestaurants.getContext(),
                                R.color.red)
                );
            } else {
                imageViewFavoriteRestaurants.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_baseline_favorite_border_24));
                imageViewFavoriteRestaurants.setColorFilter(
                        ContextCompat.getColor(
                                imageViewFavoriteRestaurants.getContext(),
                                R.color.white)
                );
            }*/
        }

    }
}
