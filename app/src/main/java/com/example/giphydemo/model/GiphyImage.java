package com.example.giphydemo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jriley on 11/3/16.
 * Simple Data class for storing an image url
 */

public class GiphyImage implements Parcelable {

    private String url = null, thumbnailUrl = null, source = null;

    private Dimension sizeFull = null, sizeThumbnail = null;

    public GiphyImage(){}

    protected GiphyImage(Parcel in) {
        url = in.readString();
        thumbnailUrl = in.readString();
        source = in.readString();
        sizeFull = in.readParcelable(Dimension.class.getClassLoader());
        sizeThumbnail = in.readParcelable(Dimension.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(thumbnailUrl);
        dest.writeString(source);
        dest.writeParcelable(sizeFull, flags);
        dest.writeParcelable(sizeThumbnail, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        if(thumbnailUrl == null)
            return url;
        return thumbnailUrl;
    }

    public String getSource() {
        return source;
    }

    public Dimension getSizeFull() {
        return sizeFull;
    }

    public Dimension getSizeThumbnail() {
        if(sizeThumbnail == null)
            return sizeFull;
        return sizeThumbnail;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSizeFull(Dimension sizeFull) {
        this.sizeFull = sizeFull;
    }

    public void setSizeThumbnail(Dimension sizeThumbnail) {
        this.sizeThumbnail = sizeThumbnail;
    }

    public static final Creator<GiphyImage> CREATOR = new Creator<GiphyImage>() {
        @Override
        public GiphyImage createFromParcel(Parcel in) {
            return new GiphyImage(in);
        }

        @Override
        public GiphyImage[] newArray(int size) {
            return new GiphyImage[size];
        }
    };
}
