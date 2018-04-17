package me.davidllorca.popularmovies.data.remote;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.model.Review;

/**
 * Created by david on 10/4/18.
 */

public class GetReviewsTask extends AsyncTask<Integer, Void, List<? extends Object>> {

    private AsyncTaskListener<List<Object>> listener;

    public GetReviewsTask(AsyncTaskListener<List<Object>> listener) {
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
            String videosResponse = NetworkUtils.getResponseFromHttpUrl(RequestFactory.buildGetReviewsUrl(params[0]));
            return ResponseUtils.parseReviewsJson(videosResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List reviews) {
        listener.onTaskCompleted(reviews != null ? reviews : new ArrayList<>());
    }

}
