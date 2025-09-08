package hakunamatata.discovereat.data.service;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.model.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RestaurantsApiService {

        @GET("businesses/search")
        Call<RestaurantApiResponse> searchRestaurants(
                @Header("Authorization") String authHeader,
                @Query("term") String term,
                @Query("location") String location,
                @Query("sort_by") String sortBy, // Aggiungi questo
                @Query("limit") int limit // E questo
        );


}
