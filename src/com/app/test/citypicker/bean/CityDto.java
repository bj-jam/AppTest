package com.app.test.citypicker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class CityDto implements Parcelable {


    private String id; /*110101*/

    private String name; /*东城区*/

    public CityDto() {
        super();
    }

    private ArrayList<DistrictDto> cityList;

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

    public ArrayList<DistrictDto> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<DistrictDto> cityList) {
        this.cityList = cityList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.cityList);
    }


    protected CityDto(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.cityList = in.createTypedArrayList(DistrictDto.CREATOR);
    }

    public static final Creator<CityDto> CREATOR = new Creator<CityDto>() {
        @Override
        public CityDto createFromParcel(Parcel source) {
            return new CityDto(source);
        }

        @Override
        public CityDto[] newArray(int size) {
            return new CityDto[size];
        }
    };
}
