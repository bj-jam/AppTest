package com.app.test.base;

import com.google.gson.Gson;

public class BaseUtil {

	/** 连接超时 */
	public final static int timeout = -1;
	/** 请求正常，返回值不是200 */
	public final static int not200 = timeout - 1;
	/** 无网络状态 */
	public final static int noNet = not200 - 1;
	/** 数据解析异常 */
	public final static int jsonException = noNet - 1;
	/** 网络连接异常 */
	public final static int connectException = jsonException - 1;
	/** 其他请求异常 */
	public final static int other = connectException - 1;

	/** json数据解析工具 */
	public static Gson gson;

	/** 初始化json数据解析工具 */
	public static Gson initGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

}
