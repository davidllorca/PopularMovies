package me.davidllorca.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.davidllorca.popularmovies.model.Movie;
import me.davidllorca.popularmovies.model.Review;
import me.davidllorca.popularmovies.model.Trailer;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

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

        Bundle arguments = getArguments();
        if (arguments.containsKey(ARG_ITEM)) {
            mMovie = arguments.getParcelable(ARG_ITEM);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mMovie.getTitle());

            Fragment trailerListFragment = TrailerListFragment.newInstance(mMovie.getId());
            Fragment reviewListFragment = ReviewListFragment.newInstance(mMovie.getId());
            trailerListFragment.setArguments(arguments);
            reviewListFragment.setArguments(arguments);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_trailer_list, trailerListFragment)
                    .add(R.id.fragment_review_list, reviewListFragment)
                    .commit();
        }
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
        }

        return rootView;
    }

}
