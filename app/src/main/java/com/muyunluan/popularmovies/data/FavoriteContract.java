package com.muyunluan.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Fei Deng on 6/15/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class FavoriteContract {

    public static final String AUTHORITY = "com.muyunluan.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + PATH_FAVORITES;

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_SOURCE = "image_src";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
