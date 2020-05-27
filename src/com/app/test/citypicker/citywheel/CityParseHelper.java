package com.app.test.citypicker.citywheel;

import android.content.Context;

import com.app.test.citypicker.bean.CityDto;
import com.app.test.citypicker.bean.DistrictDto;
import com.app.test.citypicker.bean.ProvinceDto;
import com.app.test.citypicker.utils.PickerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区解析辅助类
 */

public class CityParseHelper {
    private static final String CITY_DATA = "china_city_data.json";

    /**
     * 省份数据
     */
    private ArrayList<ProvinceDto> mProvinceDtoArrayList = new ArrayList<>();

    /**
     * 城市数据
     */
    private ArrayList<ArrayList<CityDto>> mCityBeanArrayList;

    /**
     * 地区数据
     */
    private ArrayList<ArrayList<ArrayList<DistrictDto>>> mDistrictBeanArrayList;

    private List<ProvinceDto> mProvinceBeenArray;

    private ProvinceDto mProvinceDto;

    private CityDto mCityBean;

    private DistrictDto mDistrictDto;
    /**
     * key - 省 value - 市
     */
    private Map<String, List<CityDto>> mPro_CityMap = new HashMap<String, List<CityDto>>();

    /**
     * key - 市 values - 区
     */
    private Map<String, List<DistrictDto>> mCity_DisMap = new HashMap<String, List<DistrictDto>>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, DistrictDto> mDisMap = new HashMap<String, DistrictDto>();

    public ArrayList<ProvinceDto> getProvinceBeanArrayList() {
        return mProvinceDtoArrayList;
    }

    public void setProvinceBeanArrayList(ArrayList<ProvinceDto> provinceDtoArrayList) {
        mProvinceDtoArrayList = provinceDtoArrayList;
    }

    public ArrayList<ArrayList<CityDto>> getCityBeanArrayList() {
        return mCityBeanArrayList;
    }

    public void setCityBeanArrayList(ArrayList<ArrayList<CityDto>> cityBeanArrayList) {
        mCityBeanArrayList = cityBeanArrayList;
    }

    public ArrayList<ArrayList<ArrayList<DistrictDto>>> getDistrictBeanArrayList() {
        return mDistrictBeanArrayList;
    }

    public void setDistrictBeanArrayList(ArrayList<ArrayList<ArrayList<DistrictDto>>> districtBeanArrayList) {
        mDistrictBeanArrayList = districtBeanArrayList;
    }

    public List<ProvinceDto> getProvinceBeenArray() {
        return mProvinceBeenArray;
    }

    public void setProvinceBeenArray(List<ProvinceDto> provinceBeenArray) {
        mProvinceBeenArray = provinceBeenArray;
    }

    public ProvinceDto getProvinceBean() {
        return mProvinceDto;
    }

    public void setProvinceBean(ProvinceDto provinceDto) {
        mProvinceDto = provinceDto;
    }

    public CityDto getCityBean() {
        return mCityBean;
    }

    public void setCityBean(CityDto cityBean) {
        mCityBean = cityBean;
    }

    public DistrictDto getDistrictBean() {
        return mDistrictDto;
    }

    public void setDistrictBean(DistrictDto districtDto) {
        mDistrictDto = districtDto;
    }

    public Map<String, List<CityDto>> getPro_CityMap() {
        return mPro_CityMap;
    }

    public void setPro_CityMap(Map<String, List<CityDto>> pro_CityMap) {
        mPro_CityMap = pro_CityMap;
    }

    public Map<String, List<DistrictDto>> getCity_DisMap() {
        return mCity_DisMap;
    }

    public void setCity_DisMap(Map<String, List<DistrictDto>> city_DisMap) {
        mCity_DisMap = city_DisMap;
    }

    public Map<String, DistrictDto> getDisMap() {
        return mDisMap;
    }

    public void setDisMap(Map<String, DistrictDto> disMap) {
        mDisMap = disMap;
    }

    public CityParseHelper() {

    }

    /**
     * 初始化数据，解析json数据
     */
    public void initData(Context context) {

        String cityJson = PickerUtils.getJson(context, CITY_DATA);
        Type type = new TypeToken<ArrayList<ProvinceDto>>() {
        }.getType();

        mProvinceDtoArrayList = new Gson().fromJson(cityJson, type);

        if (mProvinceDtoArrayList == null || mProvinceDtoArrayList.isEmpty()) {
            return;
        }

        mCityBeanArrayList = new ArrayList<>(mProvinceDtoArrayList.size());
        mDistrictBeanArrayList = new ArrayList<>(mProvinceDtoArrayList.size());

        //*/ 初始化默认选中的省、市、区，默认选中第一个省份的第一个市区中的第一个区县
        if (mProvinceDtoArrayList != null && !mProvinceDtoArrayList.isEmpty()) {
            mProvinceDto = mProvinceDtoArrayList.get(0);
            List<CityDto> cityList = mProvinceDto.getCityList();
            if (cityList != null && !cityList.isEmpty() && cityList.size() > 0) {
                mCityBean = cityList.get(0);
                List<DistrictDto> districtList = mCityBean.getCityList();
                if (districtList != null && !districtList.isEmpty() && districtList.size() > 0) {
                    mDistrictDto = districtList.get(0);
                }
            }
        }

        //省份数据
        mProvinceBeenArray = new ArrayList<ProvinceDto>();

        for (int p = 0; p < mProvinceDtoArrayList.size(); p++) {

            //遍历每个省份
            ProvinceDto itemProvince = mProvinceDtoArrayList.get(p);

            //每个省份对应下面的市
            ArrayList<CityDto> cityList = itemProvince.getCityList();

            //遍历当前省份下面城市的所有数据
            for (int j = 0; j < cityList.size(); j++) {
                //当前省份下面每个城市下面再次对应的区或者县
                List<DistrictDto> districtList = cityList.get(j).getCityList();
                if (districtList == null) {
                    break;
                }
                for (int k = 0; k < districtList.size(); k++) {

                    // 遍历市下面所有区/县的数据
                    DistrictDto districtModel = districtList.get(k);

                    //存放 省市区-区 数据
                    mDisMap.put(itemProvince.getName() + cityList.get(j).getName() + districtList.get(k).getName(),
                            districtModel);

//                    districtList.add(districtModel);

                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mCity_DisMap.put(itemProvince.getName() + cityList.get(j).getName(), districtList);

            }

            // 省-市的数据，保存到mCitisDatasMap
            mPro_CityMap.put(itemProvince.getName(), cityList);

            mCityBeanArrayList.add(cityList);

            //只有显示三级联动，才会执行
            ArrayList<ArrayList<DistrictDto>> array2DistrictLists = new ArrayList<>(cityList.size());

            for (int c = 0; c < cityList.size(); c++) {
                CityDto cityBean = cityList.get(c);
                array2DistrictLists.add(cityBean.getCityList());
            }
            mDistrictBeanArrayList.add(array2DistrictLists);
            mProvinceBeenArray.add(p, itemProvince);

        }

    }

}
