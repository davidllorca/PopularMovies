package me.davidllorca.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.davidllorca.popularmovies.data.model.Trailer;

/**
 * Created by david on 14/4/18.
 */

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder> {

    private List mDataSet;
    private TrailerListener mListener;

    interface TrailerListener {
        void onClickTrailer(Trailer trailer);
    }

    TrailerRecyclerViewAdapter(TrailerListener listener) {
        super();
        mDataSet = new ArrayList<>();
        mListener = listener;
    }

    public void setData(List<Object> trailers) {
        mDataSet.clear();
        mDataSet.addAll(trailers);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent,
                false);
        return new TrailerRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer = ((Trailer) mDataSet.get(position));
        holder.itemView.setTag(trailer);
        holder.mNameView.setText(trailer.getName());
        holder.mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickTrailer(trailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageButton mPlayButton;
        final TextView mNameView;

        public ViewHolder(View view) {
            super(view);
            mPlayButton = view.findViewById(R.id.bt_item_trailer_play);
            mNameView = view.findViewById(R.id.tv_item_trailer_name);
        }
    }
}
