package me.davidllorca.popularmovies;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import me.davidllorca.popularmovies.model.Trailer;

/**
 * Created by david on 9/4/18.
 */

public class GetTrailersTask extends AsyncTask<Integer, Void, List<Trailer>> {

    private AsyncTaskListener<List<Trailer>> listener;

    public GetTrailersTask(AsyncTaskListener<List<Trailer>> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected List<Trailer> doInBackground(Integer... params) {
        try {
            String videosResponse = NetworkUtils.getResponseFromHttpUrl(RequestFactory.buildGetVideosUrl(params[0]));
            return ResponseUtils.parseTrailersJson(videosResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        listener.onTaskCompleted(trailers);
    }

}
