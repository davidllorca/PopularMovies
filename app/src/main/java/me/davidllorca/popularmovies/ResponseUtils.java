package me.davidllorca.popularmovies;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse response from remote API.
 *
 * Created by David Llorca <davidllorcabaron@gmail.com> on 7/04/18.
 */

public class ResponseUtils {

    // KEYS    
    private static final String RESULTS_KEY = "results";

    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String OVERVIEW_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";

    static List<Movie> parseMoviesJson(String json) {
        try {
            List<Movie> list = new ArrayList<>();
            JSONObject object = new JSONObject(json);
            if(object.has(RESULTS_KEY)){
                String results = object.optString(RESULTS_KEY);
                JSONArray jsonArray = new JSONArray(results);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Movie parsedMovie = parseMovieJson(jsonArray.getString(i));
                    if(parsedMovie != null) list.add(parsedMovie);
                }
                return list;
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Movie parseMovieJson(String json) {
        try {
            JSONObject object = new JSONObject(json);
            Movie movie = new Movie();
            if (object.has(ID_KEY)) {
                movie.setId(object.optInt(ID_KEY));
            }
            if (object.has(TITLE_KEY)) {
                movie.setTitle(object.optString(TITLE_KEY));
            }
            if (object.has(POSTER_PATH_KEY)) {
                movie.setPosterPath(object.optString(POSTER_PATH_KEY));
            }
            if (object.has(VOTE_AVERAGE_KEY)) {
                movie.setVoteAverage(object.optDouble(VOTE_AVERAGE_KEY));
            }
            if (object.has(OVERVIEW_KEY)) {
                movie.setOverview(object.optString(OVERVIEW_KEY));
            }
            if (object.has(RELEASE_DATE_KEY)) {
                movie.setReleaseDate(object.optString(RELEASE_DATE_KEY));
            }
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
