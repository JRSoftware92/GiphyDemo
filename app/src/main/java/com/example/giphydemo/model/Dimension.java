package com.example.giphydemo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jriley on 11/5/16.
 * Simple Tuple class encapsulating width and height for an image
 */

public class Dimension implements Parcelable{

    public final int width, height;

    public Dimension(int width, int height){
        this.width = width;
        this.height = height;
    }

    protected Dimension(Parcel in) {
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dimension> CREATOR = new Creator<Dimension>() {
        @Override
        public Dimension createFromParcel(Parcel in) {
            return new Dimension(in);
        }

        @Override
        public Dimension[] newArray(int size) {
            return new Dimension[size];
        }
    };
}
