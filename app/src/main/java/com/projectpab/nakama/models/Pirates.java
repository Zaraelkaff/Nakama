package com.projectpab.nakama.models;
import android.os.Parcel;
import android.os.Parcelable;

public class Pirates implements Parcelable{
    private int pirates_id;
    private String pirates_name;
    private String pirates_photo;
    private String created_date;
    private String modified_date;

    public Pirates() {
    }

    protected Pirates(Parcel in) {
        pirates_id = in.readInt();
        pirates_name = in.readString();
        pirates_photo = in.readString();
        created_date = in.readString();
        modified_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pirates_id);
        dest.writeString(pirates_name);
        dest.writeString(pirates_photo);
        dest.writeString(created_date);
        dest.writeString(modified_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pirates> CREATOR = new Creator<Pirates>() {
        @Override
        public Pirates createFromParcel(Parcel in) {
            return new Pirates(in);
        }

        @Override
        public Pirates[] newArray(int size) {
            return new Pirates[size];
        }
    };

    public int getPirates_id() {
        return pirates_id;
    }

    public void setPirates_id(int pirates_id) {
        this.pirates_id = pirates_id;
    }

    public String getPirates_name() {
        return pirates_name;
    }

    public void setPirates_name(String pirates_name) {
        this.pirates_name = pirates_name;
    }

    public String getPirates_photo() {
        return pirates_photo;
    }

    public void setPirates_photo(String pirates_photo) {
        this.pirates_photo = pirates_photo;
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
