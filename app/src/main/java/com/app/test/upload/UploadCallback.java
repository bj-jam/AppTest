package com.app.test.upload;

/**
 * Created by jam on 2019/6/4.
 */

public interface UploadCallback {

    void onSuccess(String fileUrl);

    void onFailure(String fileUrl);

    void fileExist(String fileUrl);

    void onProgress(String fileUrl, long currentSize, long totalSize);
}
