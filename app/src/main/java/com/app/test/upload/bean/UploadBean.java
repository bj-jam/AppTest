package com.app.test.upload.bean;

import java.io.Serializable;

/**
 * description:
 */
public class UploadBean implements Serializable {
    public UploadDto rt;

    public static class UploadDto {
        public String id;//id
        public String type;//图片、视频、文件
        public String status;//状态
        public String msg;//信息
    }
}
