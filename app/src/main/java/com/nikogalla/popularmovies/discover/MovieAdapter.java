package com.nikogalla.popularmovies.discover;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nikogalla.popularmovies.R;
import com.nikogalla.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nicola on 2016-03-01.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> items;
    private int itemLayout;
    private Context mContext;
    OnMovieSelectedListener mListener;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
        this.mListener = (OnMovieSelectedListener) mContext;
        items = new ArrayList<>();
    }

    public MovieAdapter(Context mContext, Cursor c) {
        this.mContext = mContext;
        this.mListener = (OnMovieSelectedListener) mContext;
        items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)  {
        final Movie item = items.get(position);
        Picasso.with(mContext).load(item.getPosterPath()).into(holder.ivMovie);
        holder.ivMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMovieSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items!=null){
            return items.size();
        }else{
            return 0;
        }
    }

    public void setItems(ArrayList<Movie> items) {
        this.items.clear();
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String TAG = ViewHolder.class.getSimpleName();
        ImageView ivMovie;
        public ViewHolder(View itemView) {
            super(itemView);
            ivMovie = (ImageView) itemView.findViewById(R.id.ivMovie);
        }
    }

    public interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }

}
