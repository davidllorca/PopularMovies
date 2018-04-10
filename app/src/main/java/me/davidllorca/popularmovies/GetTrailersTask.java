package me.davidllorca.popularmovies;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by david on 9/4/18.
 */

public class GetTrailersTask extends AsyncTask<Integer, Void, Object> {

    private AsyncTaskListener listener;

    public GetTrailersTask(AsyncTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected Object doInBackground(Integer... params) {
        try {
            String videosResponse = NetworkUtils.getResponseFromHttpUrl(RequestFactory.buildGetVideosUrl(params[0]));
            return ResponseUtils.parseMoviesJson(videosResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
