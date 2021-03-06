package me.davidllorca.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.model.Movie;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter
        .ViewHolder> {

    private final Context mContext;
    private List<Movie> mDataSet;
    private MovieListener mListener;

    MovieRecyclerViewAdapter(Context context, MovieListener listener) {
        super();
        mContext = context;
        mDataSet = new ArrayList<>();
        mListener = listener;
    }

    public List<Movie> getData() {
        return mDataSet;
    }

    public void setData(List<Movie> movies) {
        mDataSet.clear();
        mDataSet.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        final Movie movie = mDataSet.get(position);

        Picasso.with(mContext)
                .load(BuildConfig.BASE_IMAGE_URL + "/" + movie.getPosterPath())
                .into(holder.mPosterView);

        holder.itemView.setTag(movie);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickMovie(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    interface MovieListener {
        void onClickMovie(Movie movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mPosterView;

        ViewHolder(View view) {
            super(view);
            mPosterView = view.findViewById(R.id.iv_poster);
        }
    }
}
