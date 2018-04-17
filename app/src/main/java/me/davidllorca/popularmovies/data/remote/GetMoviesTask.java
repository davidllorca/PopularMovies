package me.davidllorca.popularmovies.data.remote;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.model.Movie;

/**
 * Created by david on 9/4/18.
 */

public class GetMoviesTask extends AsyncTask<Integer, Void, List<Movie>> {

    public static final int GET_POPULAR_MOVIES = 0;
    public static final int GET_TOP_RATED_MOVIES = 1;

    private AsyncTaskListener<List<Movie>> listener;

    public GetMoviesTask(AsyncTaskListener<List<Movie>> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected List<Movie> doInBackground(Integer... params) {
        URL url;
        switch (params[0]) {
            case GET_POPULAR_MOVIES:
                url = RequestFactory.buildPopularMoviesUrl();
                break;
            case GET_TOP_RATED_MOVIES:
                url = RequestFactory.buildTopRatedMoviesUrl();
                break;
            default:
                return null;
        }

        try {
            String popularMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);
            return ResponseUtils.parseMoviesJson(popularMoviesResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        listener.onTaskCompleted(movies != null ? movies : new ArrayList<Movie>());
    }
}
