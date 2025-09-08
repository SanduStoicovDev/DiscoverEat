package hakunamatata.discovereat.util;

import static hakunamatata.discovereat.util.Constants.API_KEY_ERROR;
import static hakunamatata.discovereat.util.Constants.RETROFIT_ERROR;

import android.app.Application;

import hakunamatata.discovereat.R;

/**
 * Utility class to get the proper message to be show
 * to the user when an error occurs.
 */
public class ErrorMessagesUtil {

    private Application application;

    public ErrorMessagesUtil(Application application) {
        this.application = application;
    }

    /**
     * Returns a message to inform the user about the error.
     * @param errorType The type of error.
     * @return The message to be shown to the user.
     */
    public String getErrorMessage(String errorType) {
        switch(errorType) {
            case RETROFIT_ERROR:
                return application.getString(R.string.error_retrieving_restaurants);
            case API_KEY_ERROR:
                return application.getString(R.string.api_key_error);
            default:
                return application.getString(R.string.unexpected_error);
        }
    }
}
