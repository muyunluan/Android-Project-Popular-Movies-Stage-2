package com.muyunluan.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import static com.muyunluan.popularmovies.Constants.KEY_MOVIE_OBJECT;
import static com.muyunluan.popularmovies.Constants.KEY_SAVED_MOVIES;
import static com.muyunluan.popularmovies.Constants.SORT_ORDER_FAVORITES;
import static com.muyunluan.popularmovies.Constants.SORT_ORDER_MOST_POPULAR;
import static com.muyunluan.popularmovies.Constants.SORT_ORDER_TOP_RATE;
import static com.muyunluan.popularmovies.Constants.YOUR_API_KEY;

/**
 * Created by Fei Deng on 5/12/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private GridView mGridView;
    private MovieFlavorAdapter movieFlavorAdapter;
    private ArrayList<MovieFlavor> retrievedMovieData = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            if (null != savedInstanceState.<MovieFlavor>getParcelableArrayList(KEY_SAVED_MOVIES)) {
                retrievedMovieData.clear();
                retrievedMovieData.addAll(savedInstanceState.<MovieFlavor>getParcelableArrayList(KEY_SAVED_MOVIES));
            } else {
                Log.d(TAG, "onCreateView: no saved movies list");
            }

        } else {
            Log.d(TAG, "onCreate: no savedInstanceState");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        movieFlavorAdapter = new MovieFlavorAdapter(getActivity(), new ArrayList<MovieFlavor>());

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
        mGridView.setAdapter(movieFlavorAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (retrievedMovieData.size() == 0) {
            fetchMovies(SORT_ORDER_MOST_POPULAR);
        } else {
            updateMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_SAVED_MOVIES, retrievedMovieData);
    }

    private void fetchMovies(String sortOrder) {
        if (TextUtils.isEmpty(YOUR_API_KEY)) {
            Log.e(TAG, "fetchMovies: empty API KEY");
            return;
        }

        MovieQueryTask movieQueryTasks = new MovieQueryTask(getActivity(), retrievedMovieData, movieFlavorAdapter, sortOrder);
        movieQueryTasks.execute(YOUR_API_KEY);
    }

    private void updateMovies() {
        movieFlavorAdapter.clear();
        for(MovieFlavor movieFlavor : retrievedMovieData) {
            //Log.i(TAG, "updateMovies: movie data - " + movieFlavor.toString());
            movieFlavorAdapter.add(movieFlavor);
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
                fetchMovies(SORT_ORDER_MOST_POPULAR);
                return true;
            case R.id.menu_sort_top_rated:
                fetchMovies(SORT_ORDER_TOP_RATE);
                return true;
            case R.id.menu_sort_favorites:
                fetchMovies(SORT_ORDER_FAVORITES);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
