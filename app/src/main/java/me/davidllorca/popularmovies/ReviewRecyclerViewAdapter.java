package me.davidllorca.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.davidllorca.popularmovies.model.Review;

/**
 * Created by david on 14/4/18.
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

    private List<Review> mDataSet;

    public void setData(List<Review> reviews) {
        mDataSet.clear();
        mDataSet.addAll(reviews);
        notifyDataSetChanged();
    }

    @Override
    public ReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent,
                false);
        return new ReviewRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewRecyclerViewAdapter.ViewHolder holder, int position) {
        final Review review = mDataSet.get(position);
        holder.itemView.setTag(review);
        holder.mAuthorView.setText(review.getAuthor());
        holder.mContentView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mAuthorView;
        final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mAuthorView = view.findViewById(R.id.tv_item_review_author);
            mContentView = view.findViewById(R.id.tv_item_review_content);
        }
    }
}
