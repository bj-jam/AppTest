package com.app.test.upload;

/**
 * Created by jam on 2019/6/4.
 */

public class Config {
    // 访问的endpoint地址
    public static final String OSS_ENDPOINT = "";
    // STS 鉴权服务器地址，使用前请参照文档 https://help.aliyun.com/document_detail/31920.html 介绍配置STS 鉴权服务器地址。
    // 或者根据工程sts_local_server目录中本地鉴权服务脚本代码启动本地STS 鉴权服务器。详情参见sts_local_server 中的脚本内容。
    public static final String STS_SERVER_URL = "http://*.*.*.*:*/sts/getsts";//STS 地址

    public static final String VIDEO_NAME = "video-test";
    public static final String IMAGE_NAME = "image-test";
    public static final String FILE_NAME = "file-test";
    public static final String OSS_ACCESS_KEY_ID = "test";
    public static final String OSS_ACCESS_KEY_SECRET = "test";

}
