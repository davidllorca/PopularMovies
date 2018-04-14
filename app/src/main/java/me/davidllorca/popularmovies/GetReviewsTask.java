package me.davidllorca.popularmovies;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.model.Review;

/**
 * Created by david on 10/4/18.
 */

public class GetReviewsTask extends AsyncTask<Integer, Void, List<Review>> {

    private AsyncTaskListener<List<Review>> listener;

    public GetReviewsTask(AsyncTaskListener<List<Review>> listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected List<Review> doInBackground(Integer... params) {
        try {
            String videosResponse = NetworkUtils.getResponseFromHttpUrl(RequestFactory.buildGetReviewsUrl(params[0]));
            return ResponseUtils.parseReviewsJson(videosResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        listener.onTaskCompleted(reviews != null ? reviews : new ArrayList<Review>());
    }

}
