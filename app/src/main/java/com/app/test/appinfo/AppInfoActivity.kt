package com.app.test.appinfo

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.test.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AppInfoActivity : AppCompatActivity() {
    var listView: ListView? = null
    var adapter: BaseAdapter? = null
    var list: MutableList<AppInfo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)
        //获取应用列表
        val packages = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES)
        for (info in packages) {
            if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) { //只获取非系统自带应用
                val app = AppInfo()
                app.name = info.applicationInfo.loadLabel(packageManager).toString()
                app.packageName = info.packageName
                app.versionName = info.versionName
                app.versionCode = info.versionCode.toString() + ""
                app.icon = info.applicationInfo.loadIcon(packageManager)
                app.packageInfo = info
                list.add(app)
            }
        }
        listView = findViewById(R.id.list_view)
        if (adapter == null) {
            adapter = object : BaseAdapter() {
                override fun getCount(): Int {
                    return list.size
                }

                override fun getItem(position: Int): Any? {
                    return null
                }

                override fun getItemId(position: Int): Long {
                    return 0
                }

                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val inflater = this@AppInfoActivity.layoutInflater
                    //因为getView()返回的对象，adapter会自动赋给ListView
                    val view: View = convertView ?: inflater.inflate(R.layout.item_app_info, null)
                    val img = view.findViewById<ImageView>(R.id.img)
                    img?.setImageDrawable(list[position].icon)
                    val name = view.findViewById<TextView>(R.id.name)
                    name?.text = list[position].name
                    return view
                }
            }
        } else {
            adapter?.notifyDataSetChanged()
        }
        listView?.adapter = adapter
        listView?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val v = View.inflate(this@AppInfoActivity, R.layout.dialog_app_info, null)
            val img = v.findViewById<ImageView>(R.id.img)
            img.setImageDrawable(list[position].icon)
            val name = v.findViewById<TextView>(R.id.name)
            name.text = list[position].name
            val packageName = v.findViewById<TextView>(R.id.package_name)
            packageName.text = list[position].packageName
            packageName.setTextIsSelectable(true)
            val md5 = v.findViewById<TextView>(R.id.md5)
            md5.setTextIsSelectable(true)
            md5.text = MD5(list[position].packageInfo)
            val sha1 = v.findViewById<TextView>(R.id.sha1)
            sha1.text = SHA1(list[position].packageInfo)
            sha1.setTextIsSelectable(true)
            val version = v.findViewById<TextView>(R.id.version)
            version.text = "版本名:" + list[position].versionName + ";版本号:" + list[position].versionCode
            customAlertDialog(this@AppInfoActivity, v, true)
        }
    }

    private fun customAlertDialog(ctx: Context?, view: View?, cancelable: Boolean): AlertDialog {
        return AlertDialog.Builder(ctx!!)
                .setView(view)
                .setCancelable(cancelable)
                .show()
    }

    private fun SHA1(info: PackageInfo): String? {
        try {
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance("SHA1")
            val publicKey = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                        .toUpperCase(Locale.US)
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            val result = hexString.toString()
            return result.substring(0, result.length - 1)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    private fun MD5(info: PackageInfo): String? {
        val paramArrayOfByte = info.signatures[0].toByteArray()
        var localMessageDigest: MessageDigest? = null
        try {
            localMessageDigest = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        localMessageDigest?.update(paramArrayOfByte)
        return toHexString(localMessageDigest?.digest())
    }

    private fun toHexString(paramArrayOfByte: ByteArray?): String? {
        if (paramArrayOfByte == null) {
            return null
        }
        val localStringBuilder = StringBuilder(2 * paramArrayOfByte.size)
        var i = 0
        while (true) {
            if (i >= paramArrayOfByte.size) {
                return localStringBuilder.toString()
            }
            var str = (0xFF and paramArrayOfByte[i].toInt()).toString(16)
            if (str.length == 1) {
                str = "0$str"
            }
            localStringBuilder.append(str)
            i++
        }
    }
}