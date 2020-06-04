package com.app.test.upload.service;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.app.test.upload.Config;
import com.app.test.upload.FileUtils;
import com.app.test.upload.UICallback;
import com.app.test.upload.UploadCallback;

import java.io.File;

/**
 * Created by jam on 2019/6/13.
 * description:
 */
public class UploadService {

    private final long minSize = 1024 * 1024 * 1024;
    //OSS的上传下载
    private OssService mService;
    public static UploadService uploadService;

    public static UploadService getInstance(Context context) {
        if (uploadService == null) {
            synchronized (UploadService.class) {
                if (uploadService == null) {
                    uploadService = new UploadService(context);
                }
            }
        }
        return uploadService;
    }

    private UploadService(Context context) {
        mService = initOSS(context);
    }

    /**
     * 上传文件
     *
     * @param localFile
     * @param uiCallback
     * @param fileType   0 视频 1 图片 2 文件
     */
    public void asyncPutFile(final String localFile, final UICallback uiCallback, final int fileType) {
        File file = new File(localFile);
        if (!file.exists()) {
            if (uiCallback != null) {
                uiCallback.fileEmpty(localFile);
            }
            return;
        }
        String mBucket;
        if (fileType == 0) {
            mBucket = Config.VIDEO_NAME;
        } else if (fileType == 1)
            mBucket = Config.IMAGE_NAME;
        else
            mBucket = Config.FILE_NAME;
        UploadCallback uploadCallback = new UploadCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                if (TextUtils.equals(localFile, fileUrl)) {
                    //上传成功
                }
            }

            @Override
            public void onFailure(String fileUrl) {
                if (TextUtils.equals(localFile, fileUrl)) {
                    if (uiCallback != null) {
                        uiCallback.onFailure(fileUrl);
                    }
                }
            }

            @Override
            public void fileExist(String fileUrl) {

            }

            @Override
            public void onProgress(String fileUrl, long currentSize, long totalSize) {
                if (TextUtils.equals(localFile, fileUrl)) {
                    if (uiCallback != null) {
                        uiCallback.onProgress(fileUrl, currentSize, totalSize);
                    }
                }
            }
        };
        if (mService != null) {
            //大文件走断点上传 小文件不需要
            if (FileUtils.getFileLength(file) <= minSize) {
                mService.asyncPutImage(FileUtils.getFileMD5ToString(localFile), localFile, mBucket, uploadCallback);
            } else
                mService.asyncPutFile(FileUtils.getFileMD5ToString(localFile), localFile, mBucket, uploadCallback);
        }
    }

    private OssService initOSS(Context context) {

//        移动端是不安全环境，不建议直接使用阿里云主账号ak，sk的方式。建议使用STS方式。具体参
//        https://help.aliyun.com/document_detail/31920.html
//        注意：SDK 提供的 PlainTextAKSKCredentialProvider 只建议在测试环境或者用户可以保证阿里云主账号AK，SK安全的前提下使用。具体使用如下
//        主账户使用方式
//        String AK = "******";
//        String SK = "******";
//        credentialProvider = new PlainTextAKSKCredentialProvider(AK,SK)
//        以下是使用STS Sever方式。
//        如果用STS鉴权模式，推荐使用OSSAuthCredentialProvider方式直接访问鉴权应用服务器，token过期后可以自动更新。
//        详见：https://help.aliyun.com/document_detail/31920.html
//        OSSClient的生命周期和应用程序的生命周期保持一致即可。在应用程序启动时创建一个ossClient，在应用程序结束时销毁即可。

        //使用自己的获取STSToken的类
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(Config.STS_SERVER_URL);
        //本地签名
        OSSCustomSignerCredentialProvider provider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {

                // 此处本应该是客户端将contentString发送到自己的业务服务器,然后由业务服务器返回签名后的content。关于在业务服务器实现签名算法
                // 详情请查看http://help.aliyun.com/document_detail/oss/api-reference/access-control/signature-header.html。客户端
                // 的签名算法实现请参考OSSUtils.sign(accessKey,screctKey,content)

                String signedString = OSSUtils.sign(Config.OSS_ACCESS_KEY_ID, Config.OSS_ACCESS_KEY_SECRET, content);
                return signedString;
            }
        };


        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(context.getApplicationContext(), Config.OSS_ENDPOINT, provider, conf);
        OSSLog.enableLog();
        return new OssService(oss);
    }
}
