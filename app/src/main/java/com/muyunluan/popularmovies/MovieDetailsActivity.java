package com.muyunluan.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.muyunluan.popularmovies.data.FavoriteContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity implements ListItemClickListener {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private MovieFlavor mMovieFlavor;

    private int movieId;

    private MovieReviewsAdapter movieReviewsAdapter;
    private RecyclerView mReviewListView;

    private MovieTrailersAdapter movieTrailersAdapter;
    private RecyclerView mTrailerListView;

    private ImageButton favoriteBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (null != getIntent() && getIntent().hasExtra("movie_object")) {
            mMovieFlavor = getIntent().getParcelableExtra("movie_object");
        }

        movieId = mMovieFlavor.getmId();

        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText(mMovieFlavor.getmTitle());

        ImageView posterImg = (ImageView) findViewById(R.id.img_poster);
        Picasso.with(getApplicationContext()).load(NetworkUtils.buildImageUrlStr(mMovieFlavor.getmImageSource())).into(posterImg);

        TextView ratingTv = (TextView) findViewById(R.id.tv_rating);
        ratingTv.setText(String.valueOf(mMovieFlavor.getmRating()));

        TextView releaseTv = (TextView) findViewById(R.id.tv_release_date);
        releaseTv.setText(mMovieFlavor.getmReleaseDate());

        favoriteBt = (ImageButton) findViewById(R.id.imgbt_favorite);
        toggleFavorite();
        favoriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite()) {
                    addToFavorites();
                } else {
                    removeFromFavorites();
                }
                toggleFavorite();
            }
        });

        TextView overviewTv = (TextView) findViewById(R.id.tv_overview);
        overviewTv.setText(mMovieFlavor.getmSynopsis());

        mReviewListView = (RecyclerView) findViewById(R.id.rv_reviews);
        mReviewListView.setLayoutManager(new LinearLayoutManager((this)));
        new MovieReviewQuery().execute();

        mTrailerListView = (RecyclerView) findViewById(R.id.rv_trailers);
        mTrailerListView.setLayoutManager(new LinearLayoutManager(this));
        new MovieTrailerQuery().execute();

    }

    private void toggleFavorite() {
        if (isFavorite()) {
            favoriteBt.setImageResource(R.mipmap.ic_star);
        } else {
            favoriteBt.setImageResource(R.mipmap.ic_unstar);
        }
    }

    private boolean isFavorite() {
        Uri uri = ContentUris.withAppendedId(FavoriteContract.FavoriteEntry.CONTENT_URI, (long)mMovieFlavor.getmId());
        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                return true;
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return false;
    }

    private void addToFavorites() {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_ID, mMovieFlavor.getmId());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, mMovieFlavor.getmTitle());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_IMAGE_SOURCE, mMovieFlavor.getmImageSource());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_SYNOPSIS, mMovieFlavor.getmSynopsis());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RATING, mMovieFlavor.getmRating());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, mMovieFlavor.getmReleaseDate());

        Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, contentValues);
        //Log.i(TAG, "addToFavorites: new URI - " + uri.toString());
    }

    private void removeFromFavorites() {
        String mSelectionClause = FavoriteContract.FavoriteEntry.COLUMN_ID + " = ? ";
        String[] mSelectionArgs = {mMovieFlavor.getmId() + ""};
        long mRowsDeleted = getContentResolver().delete(FavoriteContract.FavoriteEntry.CONTENT_URI, mSelectionClause, mSelectionArgs);
    }

    @Override
    public void onListItemClick(String itemID) {
        Uri uri = Uri.parse("vnd.youtube:" + itemID);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra("VIDEO_ID", itemID);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "onListItemClick: Youtube app not installed");
        }
    }

    public class MovieReviewQuery extends AsyncTask<Void, Void, MovieFlavor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MovieFlavor doInBackground(Void... params) {
            URL url = NetworkUtils.buildReviewsUrl(MainActivityFragment.YOUR_API_KEY, movieId);
            //Log.i(TAG, "doInBackground: get URL - " + url.toString());
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                mMovieFlavor = OpenMovieJsonUtils.getMovieReviewsFromJson(getApplicationContext(), jsonMovieResponse, mMovieFlavor);
                //Log.i(TAG, "doInBackground: Movie Flavor - " + mMovieFlavor.toString());
                return mMovieFlavor;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieFlavor movieFlavor) {
            if (null != movieFlavor && 0 < movieFlavor.getmReviews().size()) {
                movieReviewsAdapter = new MovieReviewsAdapter(movieFlavor);
                mReviewListView.setAdapter(movieReviewsAdapter);
                movieReviewsAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "onPostExecute: empty review" );
            }
        }
    }

    public class MovieTrailerQuery extends AsyncTask<Void, Void, MovieFlavor> {

        @Override
        protected MovieFlavor doInBackground(Void... params) {
            URL url = NetworkUtils.buildTrailerUrl(MainActivityFragment.YOUR_API_KEY, movieId);
            //Log.i(TAG, "doInBackground: get URL - " + url.toString());
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                mMovieFlavor = OpenMovieJsonUtils.getMovieVideosFromJson(getApplicationContext(), jsonMovieResponse, mMovieFlavor);
                //Log.i(TAG, "doInBackground: Movie Flavor - " + mMovieFlavor.toString());
                return mMovieFlavor;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieFlavor movieFlavor) {
            if (null != movieFlavor && 0 < movieFlavor.getmTrailers().size()) {
                movieTrailersAdapter = new MovieTrailersAdapter(movieFlavor, MovieDetailsActivity.this);
                mTrailerListView.setAdapter(movieTrailersAdapter);
                movieTrailersAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "onPostExecute: empty trailer " );
            }
        }
    }


}
