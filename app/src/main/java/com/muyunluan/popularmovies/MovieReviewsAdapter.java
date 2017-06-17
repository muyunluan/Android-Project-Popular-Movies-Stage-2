package com.muyunluan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Fei Deng on 6/14/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {

    private static final String TAG = MovieReviewsAdapter.class.getSimpleName();

    private MovieFlavor mMovieFlavor;
    private int mMovieReviewCount;

    public MovieReviewsAdapter(MovieFlavor movieFlavor) {
        mMovieFlavor = movieFlavor;
        mMovieReviewCount = 0;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);

        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(rootView);

        MovieFlavor.MovieReview movieReview = mMovieFlavor.getmReviews().get(mMovieReviewCount);
        reviewViewHolder.authorTv.setText(movieReview.getmAuthor());
        reviewViewHolder.contentTv.setText(movieReview.getmContent());
        //Log.i(TAG, "onCreateViewHolder: MovieReview - " + movieReview.toString());
        mMovieReviewCount++;

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMovieFlavor.getmReviews().size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView authorTv;
        private TextView contentTv;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            authorTv = (TextView) itemView.findViewById(R.id.tv_author);
            contentTv = (TextView) itemView.findViewById(R.id.tv_content);

        }
    }
}
