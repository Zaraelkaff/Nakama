package com.projectpab.nakama.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Crew implements Parcelable {
    private int crew_id;
    private int pirates_id;
    private String crew_name;
    private String crew_photo;
    private String crew_bounty;
    private String created_date;
    private String modified_date;

    public Crew() {
    }

    protected Crew(Parcel in) {
        crew_id = in.readInt();
        pirates_id = in.readInt();
        crew_name = in.readString();
        crew_photo = in.readString();
        crew_bounty = in.readString();
        created_date = in.readString();
        modified_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(crew_id);
        dest.writeInt(pirates_id);
        dest.writeString(crew_name);
        dest.writeString(crew_photo);
        dest.writeString(crew_bounty);
        dest.writeString(created_date);
        dest.writeString(modified_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    public int getCrew_id() {
        return crew_id;
    }

    public void setCrew_id(int crew_id) {
        this.crew_id = crew_id;
    }

    public int getPirates_id() {
        return pirates_id;
    }

    public void setPirates_id(int pirates_id) {
        this.pirates_id = pirates_id;
    }

    public String getCrew_name() {
        return crew_name;
    }

    public void setCrew_name(String crew_name) {
        this.crew_name = crew_name;
    }

    public String getCrew_photo() {
        return crew_photo;
    }

    public void setCrew_photo(String crew_photo) {
        this.crew_photo = crew_photo;
    }

    public String getCrew_bounty() {
        return crew_bounty;
    }

    public void setCrew_bounty(String crew_bounty) {
        this.crew_bounty = crew_bounty;
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
