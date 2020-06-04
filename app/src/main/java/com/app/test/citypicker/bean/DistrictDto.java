package com.app.test.citypicker.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class DistrictDto implements Parcelable {

    private String id; /*110101*/

    private String name; /*东城区*/

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public DistrictDto() {
    }

    protected DistrictDto(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<DistrictDto> CREATOR = new Creator<DistrictDto>() {
        @Override
        public DistrictDto createFromParcel(Parcel source) {
            return new DistrictDto(source);
        }

        @Override
        public DistrictDto[] newArray(int size) {
            return new DistrictDto[size];
        }
    };
}
