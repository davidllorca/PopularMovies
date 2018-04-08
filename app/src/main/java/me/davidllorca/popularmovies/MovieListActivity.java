package me.davidllorca.popularmovies;

import android.os.AsyncTask;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    private static final int DEFAULT_SORT_TYPE = GetMoviesTask.GET_POPULAR_MOVIES;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.movie_list);
        assert mRecyclerView != null;
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
        mRecyclerView.setAdapter(new MovieRecyclerViewAdapter(this, this));
    }

    private void loadData(int sortType) {
        new GetMoviesTask().execute(sortType);
    }

    private void showMovies(List<Movie> movies) {
        ((MovieRecyclerViewAdapter) mRecyclerView.getAdapter()).setData(movies);
    }

    private void showError() {
        Toast.makeText(this, R.string.msg_error_loading_movies, Toast.LENGTH_SHORT).show();
    }

    class GetMoviesTask extends AsyncTask<Integer, Void, List<Movie>> {

        static final int GET_POPULAR_MOVIES = 0;
        static final int GET_TOP_RATED_MOVIES = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
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
                String popularMoviesResponse = NetworkUtils.getResponseFromHttpUrl
                        (url);
                return ResponseUtils.parseMoviesJson(popularMoviesResponse);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (!movies.isEmpty()) {
                showMovies(movies);
            } else {
                showError();
            }
        }
    }
}
