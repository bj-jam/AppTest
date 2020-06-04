package com.app.test.upload;

/**
 * 更新UI
 */
public interface UICallback {
    /**
     * 成功
     *
     * @param fileUrl
     */
    void onSuccess(String fileUrl);

    /**
     * 失败
     *
     * @param fileUrl
     */

    void onFailure(String fileUrl);

    /**
     * 进度
     *
     * @param fileUrl
     */

    void onProgress(String fileUrl, long currentSize, long totalSize);

    /**
     * 文件是空的
     *
     * @param fileUrl
     */
    void fileEmpty(String fileUrl);

}
