package com.muyunluan.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Fei Deng on 5/12/17.
 * Copyright (c) 2017 Muyunluan. All rights reserved.
 */

public class MovieFlavor implements Parcelable {
    private int mId;
    private String mTitle;
    private String mImageSource;
    private String mSynopsis; //overview
    private Double mRating;
    private String mReleaseDate;
    private ArrayList<MovieTrailer> mTrailers;
    private ArrayList<MovieReview> mReviews;

    public MovieFlavor(int mId, String mTitle, String mImageSource, String mSynopsis, Double mRating, String mReleaseDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mImageSource = mImageSource;
        this.mSynopsis = mSynopsis;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public String toString() {
        return "MovieFlavor{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mImageSource='" + mImageSource + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mRating=" + mRating +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mTrailers=" + mTrailers +
                ", mReviews=" + mReviews +
                '}';
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImageSource() {
        return mImageSource;
    }

    public void setmImageSource(String mImageSource) {
        this.mImageSource = mImageSource;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public void setmSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public Double getmRating() {
        return mRating;
    }

    public void setmRating(Double mRating) {
        this.mRating = mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public ArrayList<MovieTrailer> getmTrailers() {
        return mTrailers;
    }

    public void setmTrailers(ArrayList<MovieTrailer> mTrailers) {
        this.mTrailers = mTrailers;
    }

    public ArrayList<MovieReview> getmReviews() {
        return mReviews;
    }

    public void setmReviews(ArrayList<MovieReview> mReviews) {
        this.mReviews = mReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mImageSource);
        dest.writeString(this.mSynopsis);
        dest.writeDouble(this.mRating);
        dest.writeString(this.mReleaseDate);
    }

    public static final Parcelable.Creator<MovieFlavor> CREATOR = new Parcelable.Creator<MovieFlavor>() {

        @Override
        public MovieFlavor createFromParcel(Parcel source) {
            return new MovieFlavor(source);
        }

        @Override
        public MovieFlavor[] newArray(int size) {
            return new MovieFlavor[size];
        }
    };

    private MovieFlavor(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mImageSource = in.readString();
        this.mSynopsis = in.readString();
        this.mRating = in.readDouble();
        this.mReleaseDate = in.readString();
    }

    public static class MovieTrailer {
        private String mId;
        private String mIso639_1;
        private String mIso3166_1;
        private String mKey;
        private String mName;
        private String mSite;
        private int    mSize;
        private String mType;

        public MovieTrailer(String mId, String mIso639_1, String mIso3166_1, String mKey, String mName, String mSite, int mSize, String mType) {
            this.mId = mId;
            this.mIso639_1 = mIso639_1;
            this.mIso3166_1 = mIso3166_1;
            this.mKey = mKey;
            this.mName = mName;
            this.mSite = mSite;
            this.mSize = mSize;
            this.mType = mType;
        }

        @Override
        public String toString() {
            return "MovieTrailer{" +
                    "mId='" + mId + '\'' +
                    ", mIso639_1='" + mIso639_1 + '\'' +
                    ", mIso3166_1='" + mIso3166_1 + '\'' +
                    ", mKey='" + mKey + '\'' +
                    ", mName='" + mName + '\'' +
                    ", mSite='" + mSite + '\'' +
                    ", mSize=" + mSize +
                    ", mType='" + mType + '\'' +
                    '}';
        }

        public String getmId() {
            return mId;
        }

        public void setmId(String mId) {
            this.mId = mId;
        }

        public String getmIso639_1() {
            return mIso639_1;
        }

        public void setmIso639_1(String mIso639_1) {
            this.mIso639_1 = mIso639_1;
        }

        public String getmIso3166_1() {
            return mIso3166_1;
        }

        public void setmIso3166_1(String mIso3166_1) {
            this.mIso3166_1 = mIso3166_1;
        }

        public String getmKey() {
            return mKey;
        }

        public void setmKey(String mKey) {
            this.mKey = mKey;
        }

        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }

        public String getmSite() {
            return mSite;
        }

        public void setmSite(String mSite) {
            this.mSite = mSite;
        }

        public int getmSize() {
            return mSize;
        }

        public void setmSize(int mSize) {
            this.mSize = mSize;
        }

        public String getmType() {
            return mType;
        }

        public void setmType(String mType) {
            this.mType = mType;
        }
    }

    public static class MovieReview {
        private String mId;
        private String mAuthor;
        private String mContent;
        private String mUrl;

        public MovieReview(String mId, String mAuthor, String mContent, String mUrl) {
            this.mId = mId;
            this.mAuthor = mAuthor;
            this.mContent = mContent;
            this.mUrl = mUrl;
        }

        @Override
        public String toString() {
            return "MovieReview{" +
                    "mId='" + mId + '\'' +
                    ", mAuthor='" + mAuthor + '\'' +
                    ", mContent='" + mContent + '\'' +
                    ", mUrl='" + mUrl + '\'' +
                    '}';
        }

        public String getmId() {
            return mId;
        }

        public void setmId(String mId) {
            this.mId = mId;
        }

        public String getmAuthor() {
            return mAuthor;
        }

        public void setmAuthor(String mAuthor) {
            this.mAuthor = mAuthor;
        }

        public String getmContent() {
            return mContent;
        }

        public void setmContent(String mContent) {
            this.mContent = mContent;
        }

        public String getmUrl() {
            return mUrl;
        }

        public void setmUrl(String mUrl) {
            this.mUrl = mUrl;
        }
    }
}
