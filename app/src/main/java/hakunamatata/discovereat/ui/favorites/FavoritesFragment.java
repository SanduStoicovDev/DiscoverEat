package hakunamatata.discovereat.ui.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.adapter.RestaurantFavAdapter;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.data.repository.restaurants.IRestaurantRepository;
import hakunamatata.discovereat.data.repository.restaurants.RestaurantsRepository;
import hakunamatata.discovereat.util.ResponseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements ResponseCallback{
    private static final String TAG = FavoritesFragment.class.getSimpleName();

    private ListView listViewFavRestaurants;
    private List<Restaurant> restaurantList;
    private IRestaurantRepository iRestaurantRepository;
    private RestaurantFavAdapter restaurantFavAdapter;
    private ProgressBar progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        iRestaurantRepository =
                new RestaurantsRepository(requireActivity().getApplication(), this);
        restaurantList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* In caso si volesse aggiungere la topAppBar
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                // It adds the menu item in the toolbar
                menuInflater.inflate(R.menu.top_app_bar, menu);
                //menu.findItem(R.id.top_appbar).setVisible(true); Non funziona
            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.delete) {
                    Log.d(TAG, "Delete menu item pressed");
                    //iRestaurantRepository.deleteFavoriteRestaurant();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        */

        progressBar = view.findViewById(R.id.progress_bar);

        listViewFavRestaurants = view.findViewById(R.id.listview_fav_restaurants);

        useCustomListAdapter();

        progressBar.setVisibility(View.VISIBLE);

        iRestaurantRepository.getFavoriteRestaurant();

        listViewFavRestaurants.setOnItemClickListener((parent, view1, position, id) ->
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        restaurantList.get(position).getName(), Snackbar.LENGTH_SHORT).show());
    }

    /**
     * Creates a custom ArrayAdapter to be used to populate the ListView
     * R.id.listview_fav_restaurant (listViewFavRestaurants) with an ArrayList of Restaurant.
     */
    private void useCustomListAdapter() {
        restaurantFavAdapter =
                new RestaurantFavAdapter(requireContext(), R.layout.item_fav_restaurant, restaurantList,
                        new RestaurantFavAdapter.OnFavoriteButtonClickListener() {
                            @Override
                            public void onFavoriteButtonClick(Restaurant restaurant) {
                                restaurant.setFavorite(false);
                                iRestaurantRepository.updateRestaurants(restaurant);
                                Snackbar.make(listViewFavRestaurants,
                                        restaurant.getName(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
        listViewFavRestaurants.setAdapter(restaurantFavAdapter);
    }

    @Override
    public void onSuccess(List<Restaurant> restaurantList, long lastUpdate) {
        if (restaurantList != null) {
            this.restaurantList.clear();
            this.restaurantList.addAll(restaurantList);
            requireActivity().runOnUiThread(() -> {
                restaurantFavAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRestaurantsFavoriteStatusChanged(Restaurant restaurant) {
        restaurantList.remove(restaurant);
        requireActivity().runOnUiThread(() -> restaurantFavAdapter.notifyDataSetChanged());
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                getString(R.string.restaurant_removed_from_favorite_list_message),
                Snackbar.LENGTH_LONG).show();
    }
}