package me.davidllorca.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import me.davidllorca.popularmovies.model.Trailer;

/**
 * Created by david on 14/4/18.
 */

public class TrailerListFragment extends BaseFragment implements AsyncTaskListener<List<Trailer>>, TrailerRecyclerViewAdapter.TrailerListener {


    private static final String MOVIE_ID_KEY = "movie_id";

    private RecyclerView mList;
    private ProgressBar mProgressBar;

    public TrailerListFragment() {

    }

    public static TrailerListFragment newInstance(int movieId) {
        TrailerListFragment fragment = new TrailerListFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(MOVIE_ID_KEY, movieId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trailer_list, container, false);

        mList = rootView.findViewById(R.id.rv_trailer_list);
        mList.setAdapter(new TrailerRecyclerViewAdapter(this));
        mProgressBar = rootView.findViewById(R.id.pb_trailer_list_loading_indicator);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().containsKey(MOVIE_ID_KEY)) {
            loadData(getArguments().getInt(MOVIE_ID_KEY));
        }

    }

    private void loadData(int movieId) {
        if (hasNetworkConnection()) {
            new GetTrailersTask(this).execute(movieId);
        }
    }

    @Override
    public void onTaskStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskCompleted(List<Trailer> result) {
        mProgressBar.setVisibility(View.GONE);
        if (!result.isEmpty()) {
            ((TrailerRecyclerViewAdapter) mList.getAdapter()).setData(result);
        }

    }

    @Override
    public void onClickTrailer(Trailer trailer) {
        //TODO launch intent to play video
    }
}
