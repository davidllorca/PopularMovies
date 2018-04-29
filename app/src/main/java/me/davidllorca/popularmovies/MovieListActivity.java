package me.davidllorca.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.local.GetFavouritesTask;
import me.davidllorca.popularmovies.data.model.Movie;
import me.davidllorca.popularmovies.data.remote.AsyncTaskListener;
import me.davidllorca.popularmovies.data.remote.GetMoviesTask;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity
        implements AsyncTaskListener<List<Movie>>, MovieRecyclerViewAdapter.MovieListener {

    private static final int DEFAULT_SORT_TYPE = GetMoviesTask.GET_POPULAR_MOVIES;

    private static final String MOVIES_KEY = "movies";

    private RecyclerView mList;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mProgressBar = findViewById(R.id.pb_movie_list_loading_indicator);
        mList = findViewById(R.id.rv_movie_list);
        assert mList != null;
        setupRecyclerView();

        if (savedInstanceState == null) {
            loadData(DEFAULT_SORT_TYPE);
        } else {
            ArrayList<Movie> savedData = savedInstanceState.getParcelableArrayList
                    (MOVIES_KEY);
            ((MovieRecyclerViewAdapter) mList.getAdapter()).setData(savedData);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY,
                (ArrayList<? extends Parcelable>) ((MovieRecyclerViewAdapter) mList.getAdapter
                        ()).getData());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_top_rated:
                loadData(GetMoviesTask.GET_TOP_RATED_MOVIES);
                break;
            case R.id.action_sort_by_popular:
                loadData(GetMoviesTask.GET_POPULAR_MOVIES);
                break;
            default:
                getFavourites();

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        mList.setAdapter(new MovieRecyclerViewAdapter(this, this));
    }

    private void loadData(int sortType) {
        if (hasNetworkConnection()) {
            new GetMoviesTask(this).execute(sortType);
        }
    }

    private void getFavourites() {
        new GetFavouritesTask(this, this).execute();
    }

    @Override
    public void onTaskStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (movies != null && !movies.isEmpty()) {
            showMovies(movies);
        } else {
            String infoMessage = getString(R.string.msg_no_movies_found);
            if(!hasNetworkConnection()) {
                infoMessage += getString(R.string.msg_check_network_connection);
            }
            Toast.makeText(this, infoMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void showMovies(List<Movie> movies) {
        ((MovieRecyclerViewAdapter) mList.getAdapter()).setData(movies);
    }

    @Override
    public void onClickMovie(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailFragment.ITEM_KEY, movie);
        startActivity(intent);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
