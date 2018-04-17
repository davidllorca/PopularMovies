package me.davidllorca.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.davidllorca.popularmovies.data.local.MoviesContract;
import me.davidllorca.popularmovies.data.model.Movie;
import me.davidllorca.popularmovies.data.model.Review;
import me.davidllorca.popularmovies.data.model.Trailer;
import me.davidllorca.popularmovies.data.remote.AsyncTaskListener;
import me.davidllorca.popularmovies.data.remote.GetReviewsTask;
import me.davidllorca.popularmovies.data.remote.GetTrailersTask;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends BaseFragment implements AsyncTaskListener<List<Object>>,
        TrailerRecyclerViewAdapter.TrailerListener {
    /**
     * The fragment argument representing the item COLUMN_ID that this fragment
     * represents.
     */
    public static final String ITEM_KEY = "item";

    /**
     * VIEWS
     */
    private RecyclerView mTrailerList;
    private RecyclerView mReviewList;
    private CheckBox  mFavouriteCheckbox;

    /**
     * Movie to be presented.
     */
    private Movie mMovie;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments.containsKey(ITEM_KEY)) {
                mMovie = arguments.getParcelable(ITEM_KEY);
            } else {
                getActivity().finish();
            }
        } else {
            mMovie = savedInstanceState.getParcelable(ITEM_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ITEM_KEY, mMovie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (mMovie != null) {
            ((TextView) rootView.findViewById(R.id.tv_title)).setText(mMovie.getTitle());
            ((TextView) rootView.findViewById(R.id.tv_overview)).setText(mMovie.getOverview());
            ((TextView) rootView.findViewById(R.id.tv_vote_average)).setText(String.valueOf
                    (mMovie.getVoteAverage()));
            ((TextView) rootView.findViewById(R.id.tv_release_date)).setText(mMovie
                    .getReleaseDate());

            ImageView posterImageView = rootView.findViewById(R.id.iv_poster);
            Picasso.with(getContext())
                    .load(BuildConfig.BASE_IMAGE_URL + "/" + mMovie.getPosterPath())
                    .into(posterImageView);

            mTrailerList = rootView.findViewById(R.id.rv_trailers);
            mTrailerList.setAdapter(new TrailerRecyclerViewAdapter(this));
            mReviewList = rootView.findViewById(R.id.rv_reviews);
            mReviewList.setAdapter(new ReviewRecyclerViewAdapter());

            mFavouriteCheckbox = rootView.findViewById(R.id.cb_favorite);

            loadData(mMovie.getId());
        }

        return rootView;
    }

    private void insetMovie() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MoviesContract.MovieEntry.COLUMN_ID, mMovie.getId());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        movieValues.put(MoviesContract.MovieEntry.COLUMN_FAVOURITE, true);

        getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, movieValues);
    }


    private void removeMovie() {
        getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,
                MoviesContract.MovieEntry.COLUMN_ID + "=" + mMovie.getId(), null);
    }

    private void loadData(int movieId) {
        if(hasNetworkConnection()) {
            new GetTrailersTask(this).execute(movieId);
            new GetReviewsTask(this).execute(movieId);
        }
        checkIsFavourite(movieId);
    }

    private void checkIsFavourite(int movieId) {
        Cursor cursor = getActivity().getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI,
                null,
                MoviesContract.MovieEntry.COLUMN_ID + "= ?",
                new String[]{String.valueOf(movieId)},
                null);

        if (cursor != null && cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_FAVOURITE);
                mMovie.setFavourite(cursor.getInt(index) == 1);
            }
        }
        mFavouriteCheckbox.setChecked(mMovie.isFavourite());
        mFavouriteCheckbox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                insetMovie();
            } else {
                removeMovie();
            }
        });
    }

    @Override
    public void onTaskStarted() {
    }

    @Override
    public void onTaskCompleted(List<Object> result) {
        if(!result.isEmpty()){
            if(result.get(0) instanceof Trailer){
                ((TrailerRecyclerViewAdapter) mTrailerList.getAdapter()).setData(result);
            }

            if(result.get(0) instanceof Review) {
                ((ReviewRecyclerViewAdapter) mReviewList.getAdapter()).setData(result);
            }
        }
    }

    @Override
    public void onClickTrailer(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}
