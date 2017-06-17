package com.muyunluan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Fei Deng on 6/14/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {

    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();

    private MovieFlavor mMovieFlavor;
    private int mMovieTrailerCount;
    private String mMovieTrailerKey;

    final private ListItemClickListener itemClickListener;

    public MovieTrailersAdapter(MovieFlavor movieFlavor, ListItemClickListener listItemClickListener) {
        mMovieFlavor = movieFlavor;
        mMovieTrailerCount = 0;
        itemClickListener = listItemClickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, false);

        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(rootView);

        MovieFlavor.MovieTrailer movieTrailer = mMovieFlavor.getmTrailers().get(mMovieTrailerCount);
        trailerViewHolder.titleTv.setText(movieTrailer.getmName());

        mMovieTrailerKey = movieTrailer.getmKey();
        //Log.i(TAG, "onCreateViewHolder: Trailer ID - " + mMovieTrailerKey);
        mMovieTrailerCount++;

        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMovieFlavor.getmTrailers().size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView playImg;
        private TextView titleTv;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            playImg = (ImageView) itemView.findViewById(R.id.img_play);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onListItemClick(mMovieTrailerKey);
        }
    }
}
