package com.uodreams.tmgutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RequestViewModel implements Parcelable {
    public int id;
    public int value;
    public String skill;
    public ArrayList<requestUser> characters;

    protected RequestViewModel(Parcel in) {
        id = in.readInt();
        value = in.readInt();
        skill = in.readString();
        characters = in.createTypedArrayList(requestUser.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(value);
        dest.writeString(skill);
        dest.writeTypedList(characters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestViewModel> CREATOR = new Creator<RequestViewModel>() {
        @Override
        public RequestViewModel createFromParcel(Parcel in) {
            return new RequestViewModel(in);
        }

        @Override
        public RequestViewModel[] newArray(int size) {
            return new RequestViewModel[size];
        }
    };
}
