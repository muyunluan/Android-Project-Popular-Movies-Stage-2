package com.muyunluan.popularmovies;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.muyunluan.popularmovies.data.FavoriteContract;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Fei Deng on 5/12/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private GridView mGridView;
    private MovieFlavorAdapter movieFlavorAdapter;
    private ArrayList<MovieFlavor> retrievedMovieData;

    //TODO: Please update your own API Key
    public static final String YOUR_API_KEY = "";
    private static final String KEY_SAVED_MOVIES = "saved_movies";
    private static final String KEY_MOVIE_OBJECT = "movie_object";

    private static final String SORT_ORDER_TOP_RATE = "top_rates";
    private static final String SORT_ORDER_MOST_POPULAR = "most_popular";
    private static final String SORT_ORDER_FAVORITES = "favorites";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);



        mGridView = (GridView) view.findViewById(R.id.discovery_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra(KEY_MOVIE_OBJECT, movieFlavorAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_SAVED_MOVIES, retrievedMovieData);
        super.onSaveInstanceState(outState);
    }

    private void getMovies() {
        if (null != movieFlavorAdapter) {
            movieFlavorAdapter.clear();
            movieFlavorAdapter = null;
        }
        new MovieQueryTask(SORT_ORDER_MOST_POPULAR).execute(YOUR_API_KEY);
    }

    private void updateMovies() {
        movieFlavorAdapter.clear();
        for(MovieFlavor movieFlavor : retrievedMovieData) {
            movieFlavorAdapter.add(movieFlavor);
        }
    }

    public class MovieQueryTask extends AsyncTask<String, Void, ArrayList<MovieFlavor>> {

        private String mSortOrder;

        public MovieQueryTask(String sortOrder) {
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
                return retrievedMovieData;
            }

            if (0 == params.length) {
                return null;
            }

            String movieUrlStr = params[0];
            boolean isRating = mSortOrder.equals(SORT_ORDER_TOP_RATE);
            URL url = NetworkUtils.buildBaseUrl(movieUrlStr, isRating);
            //Log.i(TAG, "doInBackground: get URL - " + url.toString());
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                 retrievedMovieData = OpenMovieJsonUtils.getMovieStringsFromJson(getActivity(),jsonMovieResponse);
                return retrievedMovieData;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void getFavorites() {
            Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
            ContentResolver contentResolver = getContext().getContentResolver();
            Cursor cursor = null;

            try {
                cursor = contentResolver.query(uri, null, null, null, null);
                retrievedMovieData.clear();

                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        MovieFlavor movieFlavor = new MovieFlavor(
                                cursor.getInt(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getDouble(5),
                                cursor.getString(6));
                        retrievedMovieData.add(movieFlavor);
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
            if (!movieFlavors.isEmpty()) {
                //Log.i(TAG, "onPostExecute: get MovieFlavors length - " + movieFlavors.size());
                movieFlavorAdapter = new MovieFlavorAdapter(getActivity(), movieFlavors);
                mGridView.setAdapter(movieFlavorAdapter);
                movieFlavorAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_sort_most_popular:
                new MovieQueryTask(SORT_ORDER_MOST_POPULAR).execute(YOUR_API_KEY);
                return true;
            case R.id.menu_sort_top_rated:
                new MovieQueryTask(SORT_ORDER_TOP_RATE).execute(YOUR_API_KEY);
                return true;
            case R.id.menu_sort_favorites:
                new MovieQueryTask(SORT_ORDER_FAVORITES).execute(YOUR_API_KEY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
