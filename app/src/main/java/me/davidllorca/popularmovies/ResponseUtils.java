package me.davidllorca.popularmovies;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.model.Movie;
import me.davidllorca.popularmovies.model.Review;
import me.davidllorca.popularmovies.model.Trailer;

/**
 * Parse response from remote API.
 * <p>
 * Created by David Llorca <davidllorcabaron@gmail.com> on 7/04/18.
 */

public class ResponseUtils {

    // Keys
    private static final String RESULTS_KEY = "results";

    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String OVERVIEW_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";

    // Trailer keys
    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String SITE_KEY = "site";

    // Review keys
    private static final String AUTHOR_KEY = "author";
    private static final String COMMENT_KEY = "comment";
    private static final String URL_KEY = "url";

    static List<Movie> parseMoviesJson(String json) {
        try {
            List<Movie> list = new ArrayList<>();
            JSONObject object = new JSONObject(json);
            if (object.has(RESULTS_KEY)) {
                String results = object.optString(RESULTS_KEY);
                JSONArray jsonArray = new JSONArray(results);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Movie parsedMovie = parseMovieJson(jsonArray.getString(i));
                    if (parsedMovie != null) list.add(parsedMovie);
                }
                return list;
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<Trailer> parseTrailersJson(String json) {
        try {
            List<Trailer> list = new ArrayList<>();
            JSONObject object = new JSONObject(json);
            if (object.has(RESULTS_KEY)) {
                String results = object.optString(RESULTS_KEY);
                JSONArray jsonArray = new JSONArray(results);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Trailer parsedTrailer = parseTrailerJson(jsonArray.getString(i));
                    if (parsedTrailer != null) list.add(parsedTrailer);
                }
                return list;
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<Review> parseReviewsJson(String json) {
        try {
            List<Review> list = new ArrayList<>();
            JSONObject object = new JSONObject(json);
            if (object.has(RESULTS_KEY)) {
                String results = object.optString(RESULTS_KEY);
                JSONArray jsonArray = new JSONArray(results);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Review parsedTrailer = parseReviewJson(jsonArray.getString(i));
                    if (parsedTrailer != null) list.add(parsedTrailer);
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


    private static Trailer parseTrailerJson(String json) {
        try {
            JSONObject object = new JSONObject(json);
            Trailer trailer = new Trailer();
            if (object.has(KEY_KEY)) {
                trailer.setKey(object.optString(KEY_KEY));
            }
            if (object.has(NAME_KEY)) {
                trailer.setName(object.optString(NAME_KEY));
            }
            if (object.has(SITE_KEY)) {
                trailer.setSite(object.optString(SITE_KEY));
            }
            return trailer;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Review parseReviewJson(String json) {
        try {
            JSONObject object = new JSONObject(json);
            Review review = new Review();
            if (object.has(ID_KEY)) {
                review.setId(object.optString(ID_KEY));
            }
            if (object.has(AUTHOR_KEY)) {
                review.setAuthor(object.optString(AUTHOR_KEY));
            }
            if (object.has(COMMENT_KEY)) {
                review.setContent(object.optString(COMMENT_KEY));
            }
            if (object.has(URL_KEY)) {
                review.setUrl(object.optString(URL_KEY));
            }
            return review;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
