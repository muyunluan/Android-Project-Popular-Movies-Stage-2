package com.muyunluan.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fei Deng on 5/12/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MovieFlavorAdapter extends ArrayAdapter<MovieFlavor> {

    private static final String TAG = MovieFlavorAdapter.class.getSimpleName();

    public MovieFlavorAdapter(Context context, List<MovieFlavor> movieFlavor) {
        super(context, 0, movieFlavor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieFlavor movieFlavor = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flavor_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.flavor_image);
        String urlStr = NetworkUtils.buildImageUrlStr(movieFlavor.getmImageSource());
        // Add error check
        if (null != urlStr && !urlStr.isEmpty()) {
            Picasso.with(getContext()).load(urlStr).into(imageView);
        } else {
            Log.e(TAG, "getView: empty URL received");
        }

        return convertView;
    }

}
