package com.app.test.network;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadPicture {

	public void loadPicture(String picUrl) {
		Bitmap bitmapFact;
		try {
			bitmapFact = BitmapFactory.decodeStream(getImageStream(picUrl));
			saveFile(bitmapFact, "", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从网络上获取图片,并返回输入流
	 * 
	 * @param path
	 *            图片的完整地址
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10 * 1000);
		conn.setConnectTimeout(10 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 *            位图
	 * @param fileName
	 *            文件名
	 * @param modifyTime
	 *            修改时间
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName, String modifyTime)
			throws IOException {
		File myCaptureFile = new File(fileName);// 创建文件
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();

	}

	public Bitmap getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10 * 1000);
		conn.setConnectTimeout(10 * 1000);
		conn.setRequestMethod("GET");
		InputStream is = null;
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			is = conn.getInputStream();
		} else {
			is = null;
		}
		if (is == null) {
			throw new RuntimeException("stream is null");
		} else {
			try {
				byte[] data = readStream(is);
				if (data != null) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					return bitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			is.close();
			return null;
		}
	}

	/*
	 * 得到图片字节流 数组大小
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

}
