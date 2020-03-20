package com.app.test.utils;

import java.io.File;

import com.app.test.util.FileUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

/** 打开网络文件的帮助类需要API2.3支持 **/
@SuppressLint("DefaultLocale")
public class OpenNetFileTool {

	private Activity ativity;
	private String dataName;

	// 文档本地缓存目录
	private String path;

	public OpenNetFileTool(Activity ativity) {
		this.ativity = ativity;
		// 初始化文件夹
//		path = FileUtil.getPath4DownDocument(false);

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private void showToast(String text) {
		Toast.makeText(ativity, text, Toast.LENGTH_SHORT).show();
	}

	/** 判断是否开启了下载管理器 */
	private boolean checkDownload() {
		int state = ativity.getPackageManager().getApplicationEnabledSetting(
				"com.android.providers.downloads");
		if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
				|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER) {
			return false;
		}
		return true;
	}

	/** 开启下载管理器 */
	private void openDownloadSet() {
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			intent.setData(Uri.parse("package:com.android.providers.downloads"));
			ativity.startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException e) {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
			ativity.startActivityForResult(intent, 0);
		}
	}

	// 调用系统下载工具下载
	@SuppressWarnings("deprecation")
	public void downLoad(String url) {
		/** 判断是否开启了下载管理器，如果下载管理器被关闭，会报错 */
		if (!checkDownload()) {
			showToast("请先开启下载管理器");
			openDownloadSet();
			return;
		}
		DownloadManager downloadManager = (DownloadManager) ativity
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		// 设置允许使用的网络类型，这里是移动网络和WIFI都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

		// 显示下载界面
		request.setVisibleInDownloadsUi(true);
		// 发出通知
		request.setShowRunningNotification(true);
		try {
		} catch (Exception e) {
			showToast("下载失败");
		}

		// 返回值为一个id，需要时可用来作判断
		if (request != null) {
			long id = downloadManager.enqueue(request);
//			DownLoadCompleteReceiver.addDownLoadId(ativity, id);
		}
	}

	private void openFile(String path) {
		// 调用第三方工具打开
		String extension = getExtension(path.toLowerCase().trim(), "");
		// 获得MimeTypeMap对象
		MimeTypeMap mtm = MimeTypeMap.getSingleton();
		// 根据文件扩展名判断
		if (mtm.hasExtension(extension)) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 设置intent的Action属性
			intent.setAction(Intent.ACTION_VIEW);
			// 获取文件file的MIME类型
			String type = mtm.getMimeTypeFromExtension(extension);
			// 设置intent的data和Type属性。
			intent.setDataAndType(Uri.parse(path), type);
			// 判断是否系统是否有打开文件的软件
			ResolveInfo ri = ativity.getPackageManager().resolveActivity(
					intent, PackageManager.MATCH_DEFAULT_ONLY);
			if (ri == null) {
				showToast("没有找到应用打开该类型的文件！");
			} else {
				ativity.startActivity(intent);
			}
		} else {
			showToast("系统不支持的文件类型！");
		}
	}

	// 通过网址获得文件扩展
	public static String getExtension(String url, String defExt) {
		if ((url != null) && (url.length() > 0)) {
			int i = url.lastIndexOf('.');
			if ((i > -1) && (i < (url.length() - 1))) {
				return url.substring(i + 1);
			}
		}
		return defExt;
	}

	// 通过网址获取文件名
	private String getFileName(String url) {
		if (!TextUtils.isEmpty(dataName)) {
			return dataName;
		}
		String[] s = url.split("/");
		return s[s.length - 1];
	}

	// 打开文档
	public void openNetDocumentFile(String url) {
		// 判断SD是否存在
		if (!FileUtil.isSDCard()) {
			showToast("找不到SD卡，无法打开！");
			return;
		}

		// 判断是否存在缓存文件
		String path = this.path + getFileName(url);
		File file = new File(path);
		if (file.exists()) {
			// 如果存在缓存文件则直接打开
			openFile("file://" + path);
		} else {
			// 如果不存在则下载
			downLoad(url);
		}
	}

	// 打开文档
	public void openNetDocumentFile(String url, String dataName) {
		this.dataName = dataName;
		openNetDocumentFile(url);
	}
}
