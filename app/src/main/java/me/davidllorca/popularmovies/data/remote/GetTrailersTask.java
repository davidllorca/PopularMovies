package me.davidllorca.popularmovies.data.remote;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.model.Trailer;

/**
 * Created by david on 9/4/18.
 */

public class GetTrailersTask extends AsyncTask<Integer, Void, List<? extends Object>> {

    private AsyncTaskListener<List<Object>> listener;

    public GetTrailersTask(AsyncTaskListener<List<Object>> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected List<? extends Object> doInBackground(Integer... params) {
        try {
            String videosResponse = NetworkUtils.getResponseFromHttpUrl(RequestFactory.buildGetVideosUrl(params[0]));
            return ResponseUtils.parseTrailersJson(videosResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List trailers) {
        listener.onTaskCompleted(trailers != null ? trailers : new ArrayList());
    }

}
