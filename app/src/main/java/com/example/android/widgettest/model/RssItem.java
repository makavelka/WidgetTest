package com.example.android.widgettest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RssItem implements Parcelable {

    private String title;
    private String description;

    public RssItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    protected RssItem(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<RssItem> CREATOR = new Parcelable.Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel source) {
            return new RssItem(source);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };
}
