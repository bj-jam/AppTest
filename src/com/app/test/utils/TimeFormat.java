package com.app.test.utils;

public class TimeFormat {
	// 通过时间秒数获得格式为时：分：秒的字符串
	public static String getTime(int sec) {
		long hour = sec / (60 * 60);
		long minute = (sec - hour * 60 * 60) / 60;
		long second = sec - hour * 60 * 60 - minute * 60;
		String sh = "";
		String sm = "";
		String ss = "";
		if (hour < 10) {
			sh = "0" + String.valueOf(hour);
		} else {
			sh = String.valueOf(hour);
		}
		if (minute < 10) {
			sm = "0" + String.valueOf(minute);
		} else {
			sm = String.valueOf(minute);
		}
		if (second < 10) {
			ss = "0" + String.valueOf(second);
		} else {
			ss = String.valueOf(second);
		}
		return sh + ":" + sm + ":" + ss;
	}
}
