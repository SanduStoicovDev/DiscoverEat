package hakunamatata.discovereat.util;

/**
 * Utility class to save constants used by the app or for the api
 */

public class Constants {

    // Constants for SharedPreferences
    public static final String SHARED_PREFERENCES_FILE_NAME = "hakunamatata.discovereat.preferences";
    public static final String LAST_UPDATE = "last_update";

    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "hakunamatata.discovereat.encrypted_preferences";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";
    public static final String ID_TOKEN = "google_token";

    public static final String SHARED_PREFERENCES_FIRST_LOADING = "first_loading";

    // Constants for encrypted files
    public static final String ENCRYPTED_DATA_FILE_NAME = "hakunamatata.discovereat.encrypted_file.txt";

    // Constants for Room database
    public static final String RESTAURANTS_DATABASE_NAME = "hakunamatata_db";
    public static final int DATABASE_VERSION = 1;

    // Constants for YelpApiService
    public static final String RESTAURANTS_API_BASE_URL = "https://api.yelp.com/v3/";
    public static final String RESTAURANTS_API_TERM = "restaurants";
    public static final String SEARCH_LOCATION_BAR = "Search Location";
    public static final int FRESH_TIMEOUT = 3600000; // 1 hour in milliseconds
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";
    public static final int MINIMUM_PASSWORD_LENGTH = 6;

    // Constants for Firebase Realtime Database
    public static final String FIREBASE_REALTIME_DATABASE = "https://discovereat-a89c5-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_FAVORITE_RESTAURANTS_COLLECTION = "favorite_restaurants";
}
