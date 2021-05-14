package com.uodreams.tmgutils.model;

import android.os.Parcel;
import android.os.Parcelable;

public class requestUser implements Parcelable {
    public int userId;
    public String alias;
    public int rank;
    public String roles;

    protected requestUser(Parcel in) {
        userId = in.readInt();
        alias = in.readString();
        rank = in.readInt();
        roles = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(alias);
        dest.writeInt(rank);
        dest.writeString(roles);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<requestUser> CREATOR = new Creator<requestUser>() {
        @Override
        public requestUser createFromParcel(Parcel in) {
            return new requestUser(in);
        }

        @Override
        public requestUser[] newArray(int size) {
            return new requestUser[size];
        }
    };
}
