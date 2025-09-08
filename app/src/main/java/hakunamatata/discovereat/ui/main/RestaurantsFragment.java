package hakunamatata.discovereat.ui.main;

import static hakunamatata.discovereat.util.Constants.LAST_UPDATE;
import static hakunamatata.discovereat.util.Constants.SHARED_PREFERENCES_FILE_NAME;
import static hakunamatata.discovereat.util.Constants.SEARCH_LOCATION_BAR;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.navigation.Navigation;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.adapter.RestaurantFavAdapter;
import hakunamatata.discovereat.adapter.RestaurantsAdapter;
import hakunamatata.discovereat.data.repository.restaurants.IRestaurantRepositoryLiveData;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.adapter.RestaurantAdapter;
import hakunamatata.discovereat.data.repository.restaurants.IRestaurantRepository;
import hakunamatata.discovereat.data.repository.restaurants.RestaurantsRepository;
import hakunamatata.discovereat.ui.login.LoginActivity;
import hakunamatata.discovereat.util.ResponseCallback;
import hakunamatata.discovereat.util.SharedPreferencesUtil;
import hakunamatata.discovereat.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsFragment extends Fragment implements ResponseCallback {

    private static final String TAG = RestaurantsFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Restaurant> restaurantList;
    private IRestaurantRepository iRestaurantRepository;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RestaurantAdapter restaurantAdapter;
    private RestaurantsAdapter restaurantsAdapter;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private String input_location;
    private TextInputLayout searchTextInput;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantsFragment newInstance(String param1, String param2) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IRestaurantRepositoryLiveData restaurantRepositoryLiveData =
                ServiceLocator.getInstance().getRestaurantsRepository(
                        requireActivity().getApplication());

        iRestaurantRepository =
                new RestaurantsRepository(requireActivity().getApplication(), this);

        restaurantList = new ArrayList<>();

        String lastUpdate = "0";
        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());
        sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE, lastUpdate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton searchButton = (FloatingActionButton)view.findViewById(R.id.button_search);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);

        RecyclerView recyclerViewRestaurants = view.findViewById(R.id.recyclerViewRestaurants);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        /*restaurantAdapter = new RestaurantAdapter(restaurantList,
                requireActivity().getApplication(),
                new RestaurantAdapter.OnItemClickListener() {

                    @Override
                    public void onRestaurantItemClick(Restaurant restaurant) {
                        Snackbar.make(view, restaurant.getName(), Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(RestaurantsFragment.this.getContext(), RestaurantSpec.class);
                        Gson gson = new Gson();
                        String myRestaurant = gson.toJson(restaurant);
                        intent.putExtra("key",myRestaurant);
                        Log.d(TAG, "Restaurant Name: " + restaurant.getName() + " Restaurant idDB: " + restaurant.getIdDb());
                        getContext().startActivity(intent);
                    }

                    @Override
                    public void onFavoriteButtonPressed(int position) {
                        restaurantList.get(position).setFavorite(!restaurantList.get(position).isFavorite());
                        iRestaurantRepository.updateRestaurants(restaurantList.get(position));
                    }
                });
        recyclerViewRestaurants.setLayoutManager(layoutManager);
        recyclerViewRestaurants.setAdapter(restaurantAdapter);*/

        restaurantsAdapter = new RestaurantsAdapter(restaurantList,
                requireActivity().getApplication(),
                new RestaurantsAdapter.OnItemClickListener() {

                    @Override
                    public void onRestaurantItemClick(Restaurant restaurant) {
                        Snackbar.make(view, restaurant.getName(), Snackbar.LENGTH_SHORT).show();
                      //  Navigation.findNavController(view).navigate(R.id.action_restaurantsFragment_to_restaurantSpecFragment);

                        Intent intent = new Intent(RestaurantsFragment.this.getContext(), RestaurantSpec.class);
                        Gson gson = new Gson();
                        String myRestaurant = gson.toJson(restaurant);
                        intent.putExtra("key",myRestaurant);
                        Log.d(TAG, "Restaurant Name: " + restaurant.getName() + " Restaurant idDB: " + restaurant.getIdDb());
                        getContext().startActivity(intent);
                    }

                    @Override
                    public void onFavoriteButtonPressed(int position) {
                        restaurantList.get(position).setFavorite(!restaurantList.get(position).isFavorite());
                        iRestaurantRepository.updateRestaurants(restaurantList.get(position));
                    }
                });
        recyclerViewRestaurants.setLayoutManager(gridLayoutManager);
        recyclerViewRestaurants.setAdapter(restaurantsAdapter);

        //progressBar.setVisibility(View.VISIBLE);

        String resultReadPreferences = sharedPreferencesUtil.readStringData(
                SHARED_PREFERENCES_FILE_NAME, SEARCH_LOCATION_BAR);

        String lastLocation = "";
        if (resultReadPreferences != null) {
            lastLocation = sharedPreferencesUtil.readStringData(
                    SHARED_PREFERENCES_FILE_NAME, SEARCH_LOCATION_BAR);
        }

        searchTextInput = view.findViewById(R.id.input_location_restaurant);
        //get input of inputText
        searchButton.setOnClickListener(v -> {
            String location_search = searchTextInput.getEditText().getText().toString();
            //nascondi tastiera dopo che cerchi il ristorante
            View view2 = requireActivity().getCurrentFocus();
            if (view2 != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, SEARCH_LOCATION_BAR, location_search);
            iRestaurantRepository.fetchRestaurants(location_search, true);
        });
        searchTextInput.getEditText().setText(lastLocation);
        iRestaurantRepository.fetchRestaurants(lastLocation,false);
    }

    @Override
    public void onSuccess(List<Restaurant> restaurantList, long lastUpdate) {
        if (restaurantList != null) {
            this.restaurantList.clear();
            this.restaurantList.addAll(restaurantList);
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE,
                    String.valueOf(lastUpdate));
        }
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restaurantsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRestaurantsFavoriteStatusChanged(Restaurant restaurant) {
        /*if (restaurant.isFavorite()) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.restaurant_added_to_favorite_list_message),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.restaurant_removed_from_favorite_list_message),
                    Snackbar.LENGTH_LONG).show();
        }*/
    }
}