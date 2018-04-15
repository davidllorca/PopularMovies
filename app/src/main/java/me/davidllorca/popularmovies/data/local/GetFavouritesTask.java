package me.davidllorca.popularmovies.data.local;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.model.Movie;
import me.davidllorca.popularmovies.data.remote.AsyncTaskListener;

/**
 * Created by David Llorca <davidllorcabaron@gmail.com> on 15/04/18.
 */

public class GetFavouritesTask extends AsyncTask<Void, Void, List<Movie>> {

    private final Context context;
    private final AsyncTaskListener<List<Movie>> listener;

    public GetFavouritesTask(Context context, AsyncTaskListener<List<Movie>>
            listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    protected List doInBackground(Void... voids) {
        Cursor cursor = context.getContentResolver()
                .query(MoviesContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null) {
            List list = new ArrayList();
            int idIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE);
            int posterPathIndex = cursor.getColumnIndex(MoviesContract.MovieEntry
                    .COLUMN_POSTER_PATH);
            int voteAverageIndex = cursor.getColumnIndex(MoviesContract.MovieEntry
                    .COLUMN_VOTE_AVERAGE);
            int overviewIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW);
            int releaseDateIndex = cursor.getColumnIndex(MoviesContract.MovieEntry
                    .COLUMN_RELEASE_DATE);
            int favouriteIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_FAVOURITE);
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(idIndex));
                movie.setTitle(cursor.getString(titleIndex));
                movie.setPosterPath(cursor.getString(posterPathIndex));
                movie.setVoteAverage(cursor.getDouble(voteAverageIndex));
                movie.setOverview(cursor.getString(overviewIndex));
                movie.setReleaseDate(cursor.getString(releaseDateIndex));
                movie.setFavourite(cursor.getInt(favouriteIndex) == 1);
                list.add(movie);
            }
            cursor.close();
            return list;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        listener.onTaskCompleted(movies != null ? movies : new ArrayList());
    }
}
