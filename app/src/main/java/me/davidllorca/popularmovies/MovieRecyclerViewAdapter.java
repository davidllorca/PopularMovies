package me.davidllorca.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter
        .ViewHolder> {

    private final Context mContext;
    private final MovieListActivity mParentActivity;
    private List<Movie> mDataSet;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Movie item = (Movie) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(MovieDetailFragment.ARG_ITEM, item);

                context.startActivity(intent);
        }
    };

    MovieRecyclerViewAdapter(Context context, MovieListActivity parent) {
        mContext = context;
        mParentActivity = parent;
        mDataSet = new ArrayList<>();
    }

    public void setData(List<Movie> movies) {
        mDataSet.clear();
        mDataSet.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new MovieRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        Movie movie = mDataSet.get(position);

        Picasso.with(mContext)
                .load(BuildConfig.BASE_IMAGE_URL + "/" + movie.getPosterPath())
                .into(holder.mPosterView);

        holder.itemView.setTag(movie);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mPosterView;

        ViewHolder(View view) {
            super(view);
            mPosterView = view.findViewById(R.id.iv_poster);
        }
    }
}
