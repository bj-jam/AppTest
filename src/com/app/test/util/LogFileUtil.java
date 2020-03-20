package com.app.test.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 请求异常日志工具类
 */
public class LogFileUtil {

    /**
     * 请求异常
     */
    public final static String urlException = "URLEXCEPTION_";

    private static LogFileUtil logFileUtil;

    public LogFileUtil() {
        // TODO Auto-generated constructor stub
    }

    public static LogFileUtil init() {
        if (logFileUtil == null) {
            logFileUtil = new LogFileUtil();
        }
        return logFileUtil;
    }

    /**
     * 获取当前时间
     */
    private String getTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    /**
     * content 记录的内容<br>
     * type 日志类型<br>
     * isEncrypt 是否加密
     */
    public void writeLog(String content, String type, boolean isEncrypt) {
        FileOutputStream fos = null;
        try {
            // 文件名
            String name = getTime("yyyy-MM-dd");
            // 完整路径
//			File logFile = new File(FileUtil.getPath4Log() + type + name);
            File logFile = new File(type + name);

            // 当前时间
            String time = LogFileUtil.init().getTime("yyyy-MM-dd kk:mm:ss");
            // 写入记录的内容
            fos = new FileOutputStream(logFile, true);
            if (isEncrypt) {// 需要加密
                String log = time + "\n" + "" + "\n\n";
                fos.write(log.getBytes());
            } else {// 原文写入
                String log = time + "\n" + content + "\n\n";
                fos.write(log.getBytes());
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
