package com.app.test.citypicker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class ProvinceDto implements Parcelable {

    private String id; /*110101*/

    private String name; /*东城区*/


    private ArrayList<CityDto> cityList;


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

    public ArrayList<CityDto> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityDto> cityList) {
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

    public ProvinceDto() {
    }

    protected ProvinceDto(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.cityList = in.createTypedArrayList(CityDto.CREATOR);
    }

    public static final Creator<ProvinceDto> CREATOR = new Creator<ProvinceDto>() {
        @Override
        public ProvinceDto createFromParcel(Parcel source) {
            return new ProvinceDto(source);
        }

        @Override
        public ProvinceDto[] newArray(int size) {
            return new ProvinceDto[size];
        }
    };
}
