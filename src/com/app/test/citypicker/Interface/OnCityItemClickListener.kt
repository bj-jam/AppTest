package com.app.test.citypicker.Interface

import com.app.test.citypicker.bean.CityDto
import com.app.test.citypicker.bean.DistrictDto
import com.app.test.citypicker.bean.ProvinceDto

/**
 */
abstract class OnCityItemClickListener {
    /**
     * 当选择省市区三级选择器时，需要覆盖此方法
     *
     * @param province
     * @param city
     * @param district
     */
    open fun onSelected(province: ProvinceDto?, city: CityDto?, district: DistrictDto?) {}

    /**
     * 取消
     */
    open fun onCancel() {}
}