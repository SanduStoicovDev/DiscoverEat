package hakunamatata.discovereat.ui.main;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import hakunamatata.discovereat.R;
import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.Result;
import hakunamatata.discovereat.model.User;

import com.squareup.picasso.Picasso;

public class RestaurantSpec extends AppCompatActivity {


    Restaurant restSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_spec);
        Log.d(TAG, "Rcidd");
        TextView nameRestaurant = (TextView)findViewById(R.id.name_restaurant);
        TextView text_price = (TextView)findViewById(R.id.text_price_rest_spec);
        TextView text_rating = (TextView)findViewById(R.id.text_rating_rest_spec);
        TextView text_state = (TextView)findViewById(R.id.city_state_restaurant);
        Button searchOnMapsBtn = (Button) findViewById(R.id.button_search_on_maps);
        Button callRestaurantBtn = (Button) findViewById(R.id.button_call_restaurant);
        TextView text_telephone = (TextView)findViewById(R.id.telephone_restaurant);
        TextView locationRestaurant = (TextView)findViewById(R.id.location_restaurant);
        ImageView image = (ImageView)findViewById(R.id.imageViewSpec);
        final FloatingActionButton buttonBack = (FloatingActionButton)findViewById(R.id.button_back);
        Gson gson = new Gson();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intentBack = new Intent(view.getContext(), HomeActivity.class);
                intentBack.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0,0);
                view.getContext().startActivity(intentBack);*/
                finish();
            }
        });
        callRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assicurati che restSpec.getPhone() restituisca il numero in un formato utilizzabile,
                // ad esempio "+391234567890" o "0123456789"
                String phoneNumber = restSpec.getPhone();

                // Crea un Intent per aprire l'app di composizione telefonica
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // Assicurati che "tel:" sia seguito direttamente dal numero di telefono senza spazi
                intent.setData(Uri.parse("tel:" + phoneNumber));

                // intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });

        searchOnMapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("google.navigation:q= " + restSpec.getLocation().getAddress1()
                + ", " + restSpec.getLocation().getCity());

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        });

        //Restaurant restaurant = RestaurantSpecArgs.fromBundle(getIntent().getExtras().get("Restaurant"));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Restaurant restaurant = gson.fromJson(getIntent().getStringExtra("key"), Restaurant.class);
            restSpec = restaurant;
            //Log.d(TAG, "risposta " + value);
            //The key argument here must match that used in the other activity
            nameRestaurant.setText(restaurant.getName());
            locationRestaurant.setText(restaurant.getLocation().getAddress1());
            text_state.setText(restaurant.getLocation().getCity() + ", " + restaurant.getLocation().getCountry());
            text_rating.setText("Rating: " + restaurant.getRating() + "★");
            text_telephone.setText("Phone: " + restaurant.getDisplayPhone());
            if(restaurant.getPrice() != null){
                text_price.setText("Price: " + restaurant.getPrice());
            } else {
                text_price.setText(" ");
            }
            if(!restaurant.getImageUrl().isEmpty()){
                Log.d(TAG, "picasso: " + restaurant.getImageUrl().isEmpty());
                Picasso.get().load(restaurant.getImageUrl()).into(image);
            }

        }
    }
}