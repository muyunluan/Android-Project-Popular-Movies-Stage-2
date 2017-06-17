package com.muyunluan.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fei Deng on 5/16/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public final class OpenMovieJsonUtils {

    private static final String TAG = OpenMovieJsonUtils.class.getSimpleName();

    public static ArrayList<MovieFlavor> getMovieStringsFromJson(Context context, String moiveJsonStr) throws JSONException {
        final String KEY_RESULTS = "results";

        final String KEY_ID = "id";
        final String KEY_ORIGINAL_TITLE = "original_title";
        final String KEY_POSTER_IMAGE_THUMBNAIL = "poster_path";
        final String KEY_SYNOPSIS = "overview";
        final String KEY_RATING = "vote_average";
        final String KEY_RELEASE_DATE = "release_date";

        JSONObject movieJson = new JSONObject(moiveJsonStr);

        JSONArray resultsArray = movieJson.getJSONArray(KEY_RESULTS);
        int arrLen = resultsArray.length();
        ArrayList<MovieFlavor> parsedMovieData = new ArrayList<>();

        for (int i = 0; i < arrLen; i++) {
            JSONObject movieObject = resultsArray.getJSONObject(i);
            int idInt = movieObject.getInt(KEY_ID);
            String originalTitleStr = movieObject.getString(KEY_ORIGINAL_TITLE);
            String posterImagStr = movieObject.getString(KEY_POSTER_IMAGE_THUMBNAIL);
            String synopsisStr = movieObject.getString(KEY_SYNOPSIS);
            Double ratingStr = movieObject.getDouble(KEY_RATING);
            String releaseDateStr = movieObject.getString(KEY_RELEASE_DATE);
            MovieFlavor movieFlavor = new MovieFlavor(idInt, originalTitleStr, posterImagStr, synopsisStr, ratingStr, releaseDateStr);
            //Log.i(TAG, "getMovieStringsFromJson: MovieFlavor object - " + movieFlavor.toString());
            parsedMovieData.add(i, movieFlavor);
        }
        return parsedMovieData;
    }

    public static MovieFlavor getMovieReviewsFromJson(Context context, String movieReviewsJsonStr, MovieFlavor movieFlavor) throws JSONException {
        final String KEY_RESULTS = "results";

        final String KEY_ID = "id";
        final String KEY_AUTHOR = "author";
        final String KEY_CONTENT = "content";
        final String KEY_URL = "url";

        JSONObject reviewJson = new JSONObject(movieReviewsJsonStr);

        JSONArray resultsArray = reviewJson.getJSONArray(KEY_RESULTS);
        int arrLen = resultsArray.length();
        ArrayList<MovieFlavor.MovieReview> parsedReviewData = new ArrayList<>();
        for (int i = 0; i < arrLen; i++) {
            JSONObject reviewObject = resultsArray.getJSONObject(i);
            String idStr = reviewObject.getString(KEY_ID);
            String authorStr = reviewObject.getString(KEY_AUTHOR);
            String contentStr = reviewObject.getString(KEY_CONTENT);
            String urlStr = reviewObject.getString(KEY_URL);

            MovieFlavor.MovieReview movieReview = new MovieFlavor.MovieReview(idStr, authorStr, contentStr, urlStr);
            //Log.i(TAG, "getMovieReviewsFromJson: MovieReview object - " + movieReview.toString());
            parsedReviewData.add(i, movieReview);
        }

        movieFlavor.setmReviews(parsedReviewData);
        //Log.i(TAG, "getMovieReviewsFromJson: MovieFlavor object - " + movieFlavor.toString());
        return movieFlavor;
    }

    public static MovieFlavor getMovieVideosFromJson(Context context, String movieVideosJsonStr, MovieFlavor movieFlavor) throws JSONException {
        final String KEY_RESULTS = "results";

        final String KEY_ID = "id";
        final String KEY_ISO_639_1 = "iso_639_1";
        final String KEY_ISO_3166_1 = "iso_3166_1";
        final String KEY_KEY = "key";
        final String KEY_NAME = "name";
        final String KEY_SITE = "site";
        final String KEY_SIZE = "size";
        final String KEY_TYPE = "type";

        JSONObject trailerJson = new JSONObject(movieVideosJsonStr);

        JSONArray resultsArray = trailerJson.getJSONArray(KEY_RESULTS);
        int arrLen = resultsArray.length();
        ArrayList<MovieFlavor.MovieTrailer> parsedReviewData = new ArrayList<>();
        for (int i = 0; i < arrLen; i++) {
            JSONObject trailerObject = resultsArray.getJSONObject(i);
            String idStr = trailerObject.getString(KEY_ID);
            String iso639_1Str = trailerObject.getString(KEY_ISO_639_1);
            String iso3166_1Str = trailerObject.getString(KEY_ISO_3166_1);
            String keyStr = trailerObject.getString(KEY_KEY);
            String nameStr = trailerObject.getString(KEY_NAME);
            String siteStr = trailerObject.getString(KEY_SITE);
            int sizeStr = trailerObject.getInt(KEY_SIZE);
            String typeStr = trailerObject.getString(KEY_TYPE);

            MovieFlavor.MovieTrailer movieTrailer = new MovieFlavor.MovieTrailer(idStr, iso639_1Str, iso3166_1Str, keyStr, nameStr, siteStr, sizeStr, typeStr);
            parsedReviewData.add(i, movieTrailer);
        }

        movieFlavor.setmTrailers(parsedReviewData);

        return movieFlavor;
    }
}
