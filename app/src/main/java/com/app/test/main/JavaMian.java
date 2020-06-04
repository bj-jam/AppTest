package com.app.test.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class JavaMian {

    public static void main(String[] args) {
        sdfd();
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//
//            }
//        });
//        Observable.just(1, 2, 3, 4, 5)
//                .scan(new BiFunction<Integer, Integer, Integer>() {
//                    @Override
//                    public Integer apply(Integer integer, Integer integer2) throws Exception {
//                        System.out.println("====================apply ");
//                        System.out.println("====================integer " + integer);
//                        System.out.println("====================integer2 " + integer2);
//                        return integer + integer2;
//                    }
//                })
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        System.out.println("====================integer2 " + integer);
//                    }
//                });

    }

    private static void sdfd() {
        List<String> data = new ArrayList<>();
        data.add("我哎");
        data.add("我");
        data.add("我哎");
        data.add("我哎哎");
        System.out.println(data.indexOf("我哎"));
    }

    public static void djfal(final OnFileReaderCompleteCallBack callBack, final String name) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int cdf = 30;
                try {
                    while (cdf > 0) {
                        cdf--;
                        System.out.println(cdf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != callBack) {
                        callBack.onFileReadComplete(cdf + "!!!!");
                        System.out.println("finally" + cdf + "!!!!" + name);
                    }
                    try {
                        if (executorService != null && !executorService.isShutdown()) {
                            executorService.shutdown();
                            System.out.println("finally  shutdown" + name);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void enenga() {
        start(new OnLoadContentCompleteListener() {
            @Override
            public void onLoadJsContentComplete(String content, String localContent, String netContent) {
                System.out.println("========");
            }

            @Override
            public void onCallBackDownloadTag(String tagDownload) {

            }
        });
    }

    public static synchronized void start(final OnLoadContentCompleteListener listener) {
        djfal(new OnFileReaderCompleteCallBack() {
            @Override
            public void onFileReadComplete(String content) {
                startScend(listener);
            }
        }, "start");
    }

    public static synchronized void startScend(final OnLoadContentCompleteListener listener) {
        djfal(new OnFileReaderCompleteCallBack() {
            @Override
            public void onFileReadComplete(String content) {
                listener.onLoadJsContentComplete(content, null, null);
            }
        }, "startScend");
    }

    public interface OnFileReaderCompleteCallBack {
        void onFileReadComplete(String content);
    }

    public interface OnLoadContentCompleteListener {
        void onLoadJsContentComplete(String content, String localContent, String netContent);

        void onCallBackDownloadTag(String tagDownload);
    }

    public static int upZipFile(File zipFile, String folderPath) throws Exception {
//        try {
//            ZipFile zfile = new ZipFile(zipFile);
//            Enumeration zList = zfile.entries();
//            ZipEntry ze = null;
//            byte[] buf = new byte[1024];
//            while (zList.hasMoreElements()) {
//                ze = (ZipEntry) zList.nextElement();
//                if (ze.isDirectory()) {
//                    //Logcat.d("upZipFile", "ze.getName() = " + ze.getName());
//                    String dirstr = folderPath + ze.getName();
//                    //dirstr.trim();
//                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
//                    //Logcat.d("upZipFile", "str = " + dirstr);
//                    File f = new File(dirstr);
//                    f.mkdir();
//                    continue;
//                }
//                //Logcat.d("upZipFile", "ze.getName() = " + ze.getName());
//                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
//                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
//                int readLen = 0;
//                while ((readLen = is.read(buf, 0, 1024)) != -1) {
//                    os.write(buf, 0, readLen);
//                }
//                is.close();
//                os.close();
//            }
//            zfile.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return 0;
    }
}
