package com.app.test.citypicker

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app.test.R
import com.app.test.citypicker.Interface.OnCityItemClickListener
import com.app.test.citypicker.bean.CityDto
import com.app.test.citypicker.bean.DistrictDto
import com.app.test.citypicker.bean.ProvinceDto
import com.app.test.citypicker.citypickerview.CityPickerHelper
import com.app.test.citypicker.citywheel.CityConfig

/**
 * @author lcx
 * Created at 2020.5.27
 * Describe:
 */
class CityPickerActivity : Activity() {
    var mCityPickerHelper: CityPickerHelper = CityPickerHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
        mCityPickerHelper.init(this)
        findViewById<View>(R.id.iv_anim).setOnClickListener { wheel(); }
    }

    /**
     * 弹出选择器
     */
    private fun wheel() {
        val cityConfig: CityConfig = CityConfig.Builder()
                .title("选择城市")
                .visibleItemsCount(7)
                .province("北京市")
                .city("北京市")
                .district("朝阳区")
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(true)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                //.setCustomItemLayout(R.layout.)//自定义item的布局
                //.setCustomItemTextViewId(R.id.)
                .setShowGAT(true)
                .build()
        mCityPickerHelper.setConfig(cityConfig)
        mCityPickerHelper.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(province: ProvinceDto?, city: CityDto?, district: DistrictDto?) {
                val sb = StringBuilder()
                sb.append("选择的结果：\n")
                if (province != null) {
                    sb.append(province.name.toString() + "\n")
                }
                if (city != null) {
                    sb.append(city.name.toString() + "\n")
                }
                if (district != null) {
                    sb.append(district.name.toString() + "\n")
                }
                Toast.makeText(this@CityPickerActivity, sb.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onCancel() {}
        })
        mCityPickerHelper.showCityPicker()
    }
}