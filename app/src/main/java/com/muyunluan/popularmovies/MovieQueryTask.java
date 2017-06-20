package com.muyunluan.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.muyunluan.popularmovies.data.FavoriteContract;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.muyunluan.popularmovies.Constants.SORT_ORDER_FAVORITES;
import static com.muyunluan.popularmovies.Constants.SORT_ORDER_TOP_RATE;

/**
 * Created by Fei Deng on 6/19/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MovieQueryTask extends AsyncTask<String, Void, ArrayList<MovieFlavor>> {

    private static final String TAG = MovieQueryTask.class.getSimpleName();

    private Context mContext;
    private ArrayList<MovieFlavor> mMovieFlavors;
    private MovieFlavorAdapter mMovieFlavorAdapter;
    private String mSortOrder;

    public MovieQueryTask(Context context, ArrayList<MovieFlavor> movieFlavors, MovieFlavorAdapter movieFlavorAdapter, String sortOrder) {
        mContext = context;
        mMovieFlavors = movieFlavors;
        mMovieFlavorAdapter = movieFlavorAdapter;
        mSortOrder = sortOrder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MovieFlavor> doInBackground(String... params) {

        if (mSortOrder.equals(SORT_ORDER_FAVORITES)) {
            getFavorites();
            return mMovieFlavors;
        }

        if (0 == params.length) {
            return null;
        }

        String apiKey = params[0];
        boolean isRating = mSortOrder.equals(SORT_ORDER_TOP_RATE);
        URL url = NetworkUtils.buildBaseUrl(apiKey, isRating);
        //Log.i(TAG, "doInBackground: get URL - " + url.toString());
        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
            mMovieFlavors = OpenMovieJsonUtils.getMovieStringsFromJson(mContext, jsonMovieResponse, mMovieFlavors);
            return mMovieFlavors;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void getFavorites() {
        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(uri, null, null, null, null);
            mMovieFlavors.clear();

            if (null != cursor) {
                while (cursor.moveToNext()) {
                    MovieFlavor movieFlavor = new MovieFlavor(
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getDouble(5),
                            cursor.getString(6));
                    mMovieFlavors.add(movieFlavor);
                }
            } else {
                Log.e(TAG, "getFavorites: empty Cursor");
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieFlavor> movieFlavors) {
        if (null != movieFlavors && null != mMovieFlavorAdapter) {
            //Log.i(TAG, "onPostExecute: get MovieFlavors length - " + movieFlavors.size());
            mMovieFlavorAdapter.clear();
            for (MovieFlavor movieFlavor : movieFlavors) {
                mMovieFlavorAdapter.add(movieFlavor);
            }
        }
    }
}

