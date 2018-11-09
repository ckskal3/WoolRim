package org.woolrim.woolrim.DataItems;

import android.os.Parcel;
import android.os.Parcelable;

public class MyFavoritesItem implements Parcelable {
    public String poemName, userName, error;

    public MyFavoritesItem(String poemName, String userName){
        this.poemName = poemName;
        this.userName = userName;
        this.error = "SUCCESS";
    }
    public MyFavoritesItem(String error){
        this.error = error;
    }

    protected MyFavoritesItem(Parcel in) {
        poemName = in.readString();
        userName = in.readString();
    }

    public static final Creator<MyFavoritesItem> CREATOR = new Creator<MyFavoritesItem>() {
        @Override
        public MyFavoritesItem createFromParcel(Parcel in) {
            return new MyFavoritesItem(in);
        }

        @Override
        public MyFavoritesItem[] newArray(int size) {
            return new MyFavoritesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poemName);
        parcel.writeString(userName);
    }
}
