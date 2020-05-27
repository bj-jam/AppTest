package com.app.test.citypicker.Interface;

import com.app.test.citypicker.bean.CityDto;
import com.app.test.citypicker.bean.DistrictDto;
import com.app.test.citypicker.bean.ProvinceDto;

/**
 * 作者：liji on 2017/11/16 10:06
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public abstract class OnCityItemClickListener {

    /**
     * 当选择省市区三级选择器时，需要覆盖此方法
     *
     * @param province
     * @param city
     * @param district
     */
    public void onSelected(ProvinceDto province, CityDto city, DistrictDto district) {

    }

    /**
     * 取消
     */
    public void onCancel() {

    }
}
