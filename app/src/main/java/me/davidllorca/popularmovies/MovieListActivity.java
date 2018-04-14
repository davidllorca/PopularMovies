package me.davidllorca.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import me.davidllorca.popularmovies.model.Movie;

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

    private RecyclerView mList;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mProgressBar = findViewById(R.id.pb_movie_list_loading_indicator);
        mList = findViewById(R.id.rv_movie_list);
        assert mList != null;
        setupRecyclerView();

        loadData(DEFAULT_SORT_TYPE);
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
            default:
                loadData(GetMoviesTask.GET_POPULAR_MOVIES);

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

    @Override
    public void onTaskStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (!movies.isEmpty()) {
            showMovies(movies);
        } else {
            showError();
        }
    }

    private void showMovies(List<Movie> movies) {
        ((MovieRecyclerViewAdapter) mList.getAdapter()).setData(movies);
    }

    private void showError() {
        Toast.makeText(this, R.string.msg_error_loading_movies, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickMovie(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailFragment.ARG_ITEM, movie);
        startActivity(intent);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
