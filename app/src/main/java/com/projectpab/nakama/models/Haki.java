package com.projectpab.nakama.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Haki implements Parcelable {
    private int haki_id;
    private String haki_name;
    private String haki_describe;
    private String created_date;
    private String modified_date;

    public Haki() {
    }

    protected Haki(Parcel in) {
        haki_id = in.readInt();
        haki_name = in.readString();
        haki_describe = in.readString();
        created_date = in.readString();
        modified_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(haki_id);
        dest.writeString(haki_name);
        dest.writeString(haki_describe);
        dest.writeString(created_date);
        dest.writeString(modified_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Haki> CREATOR = new Creator<Haki>() {
        @Override
        public Haki createFromParcel(Parcel in) {
            return new Haki(in);
        }

        @Override
        public Haki[] newArray(int size) {
            return new Haki[size];
        }
    };

    public int getHaki_id() {
        return haki_id;
    }

    public void setHaki_id(int haki_id) {
        this.haki_id = haki_id;
    }

    public String getHaki_name() {
        return haki_name;
    }

    public void setHaki_name(String haki_name) {
        this.haki_name = haki_name;
    }

    public String getHaki_describe() {
        return haki_describe;
    }

    public void setHaki_describe(String haki_describe) {
        this.haki_describe = haki_describe;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }
}
