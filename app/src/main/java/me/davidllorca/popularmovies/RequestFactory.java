package me.davidllorca.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Factory responsible for generating API's requests.
 *
 * Created by David Llorca <davidllorcabaron@gmail.com> on 4/04/18.
 */

public final class RequestFactory {

    private static final String TAG = RequestFactory.class.getSimpleName();

    private static final String POPULAR_ENDPOINT = "movie/popular";
    private static final String TOP_RATED_ENDPOINT = "movie/top_rated";

    private static final String API_KEY_PARAM = "api_key";

    private RequestFactory(){

    }

    static URL buildPopularMoviesUrl() {
        Uri builtUri = Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendEncodedPath(POPULAR_ENDPOINT)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    static URL buildTopRatedMoviesUrl() {
        Uri builtUri = Uri.parse(BuildConfig.BASE_URL).buildUpon()
                .appendEncodedPath(TOP_RATED_ENDPOINT)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}
