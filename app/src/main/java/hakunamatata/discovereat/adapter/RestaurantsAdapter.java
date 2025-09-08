package hakunamatata.discovereat.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.model.Restaurant;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder>{

    public interface OnItemClickListener {
        void onRestaurantItemClick(Restaurant restaurant);
        void onFavoriteButtonPressed(int position);
    }

    private List<Restaurant> restaurantList;
    private final Application application;
    private final RestaurantsAdapter.OnItemClickListener onItemClickListener;

    public RestaurantsAdapter(List<Restaurant> restaurantList, Application application, RestaurantsAdapter.OnItemClickListener onItemClickListener) {
        this.restaurantList = restaurantList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsAdapter.MyViewHolder holder, int position) {
        final Restaurant restaurant = restaurantList.get(position);
        holder.title.setText(restaurant.getName());
        holder.rating.setText(restaurant.getRating() + " ★");

        //imposta cuore rosso quando torni su Restaurants da Favorites
        if(restaurant.isFavorite()){
            holder.favoriteView.setImageDrawable(
                    AppCompatResources.getDrawable(application,
                            R.drawable.iconfavoritefull));
            holder.favoriteView.setColorFilter(
                    ContextCompat.getColor(
                            holder.favoriteView.getContext(),
                            R.color.red)
            );
        } else {
            holder.favoriteView.setImageDrawable(
                    AppCompatResources.getDrawable(application,
                            R.drawable.icon_favorite));
            holder.favoriteView.setColorFilter(
                    ContextCompat.getColor(
                            holder.favoriteView.getContext(),
                            R.color.black)
            );
        }

        // Verifica se il percorso dell'immagine non è vuoto o nullo
        if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().isEmpty()) {
            Picasso.get().load(restaurant.getImageUrl()).into(holder.image);
        } else {
            // Nel caso in cui il percorso dell'immagine sia vuoto o nullo, imposta l'immagine di fallback
            holder.image.setImageResource(R.drawable.image_not_found_black); // R.drawable.default_image è l'immagine di fallback
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private ImageView image;
        private TextView rating;
        private CardView cardView;
        private ImageView favoriteView;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.carView);
            rating = itemView.findViewById(R.id.rating_rest);
            favoriteView = itemView.findViewById(R.id.favorite_restaurant);
            image.setOnClickListener(this);
            title.setOnClickListener(this);
            favoriteView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.favorite_restaurant) {
                Log.d(TAG, "favo9");
                setImageViewFavoriteRestaurants(!restaurantList.get(getAdapterPosition()).isFavorite());
                onItemClickListener.onFavoriteButtonPressed(getAdapterPosition());
            } else {
                Log.d(TAG, "favo10");
                onItemClickListener.onRestaurantItemClick(restaurantList.get(getAdapterPosition()));
            }
        }

        private void setImageViewFavoriteRestaurants(boolean isFavorite) {
            if (isFavorite) {
                favoriteView.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.iconfavoritefull));
                favoriteView.setColorFilter(
                        ContextCompat.getColor(
                                favoriteView.getContext(),
                                R.color.red)
                );
            } else {
                favoriteView.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.icon_favorite));
                favoriteView.setColorFilter(
                        ContextCompat.getColor(
                                favoriteView.getContext(),
                                R.color.black)
                );
            }
        }
    }
}
