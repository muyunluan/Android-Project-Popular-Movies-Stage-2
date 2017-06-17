package com.muyunluan.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Fei Deng on 5/12/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class NetworkUtils {

    private final static String TAG = NetworkUtils.class.getSimpleName();

    private final static String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_SORT = "sort_by";
    private final static String SORT_BY_POPULAR = "popularity.desc";
    private final static String SORT_BY_RATING = "vote_average.desc";
    private final static String PARAM_LANGUAGE = "language";

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String PATH_TRAILERS = "videos";
    private static final String PATH_REVIEWS = "reviews";

    private final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String IMAGE_SIZE = "w185/";

    public static URL buildBaseUrl(String searchQuery, boolean isRating) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, searchQuery)
                .appendQueryParameter(PARAM_SORT, isRating ? SORT_BY_RATING : SORT_BY_POPULAR)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .build();

        return buildUrl(builtUri);
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildImageUrlStr(String posterPath) {
        if (null != posterPath || !posterPath.isEmpty()) {
            StringBuffer imageStr = new StringBuffer(IMAGE_BASE_URL);
            imageStr.append(IMAGE_SIZE);
            imageStr.append(posterPath);
            return imageStr.toString();
        } else {
            Log.e(TAG, "buildImageUrlStr: Empty input for image poster path");
            return null;
        }
    }

    public static URL buildTrailerUrl(String searchQuery, int id) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(PATH_TRAILERS)
                .appendQueryParameter(PARAM_API_KEY, searchQuery)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .build();

        return buildUrl(builtUri);
    }

    public static URL buildReviewsUrl(String searchQuery, int id) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, searchQuery)
                .appendQueryParameter(PARAM_LANGUAGE, "en-US")
                .build();

        return buildUrl(builtUri);
    }

    private static URL buildUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

}
