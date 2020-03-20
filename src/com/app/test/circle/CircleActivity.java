package com.app.test.circle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.app.test.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CircleActivity extends Activity implements SeekBarView.ResponseOnTouch {
    private CirclePieView cp;
    private PieGraph mPieGraph;
    private CustomPieView pv;
    private SeekBarView mCustomSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        mCustomSeekbar = findViewById(R.id.mCustomSeekbar);
        cp = (CirclePieView) findViewById(R.id.cp);
        cp.setNumbers(new int[]{4, 5, 7});
        cp.setTextColors(new int[]{Color.parseColor("#FFFCCB4A"),
                Color.parseColor("#FFF75D60"), Color.parseColor("#FF01D3F6")});
        initView();
        initData();


        pv = (CustomPieView) findViewById(R.id.pv);
        List<CustomPieView.Data> list = new ArrayList<>();
        list.add(new CustomPieView.Data("第一块", R.drawable.code6, R.drawable.code6));
        list.add(new CustomPieView.Data("第二块", R.drawable.code6, R.drawable.code6));
        list.add(new CustomPieView.Data("第三块", R.drawable.code6, R.drawable.code6));
//        list.add(new PieView.Data("第四块", R.drawable.code6, R.drawable.code6));
//        list.add(new PieView.Data("第五块", R.drawable.code6, R.drawable.code6));
//        list.add(new PieView.Data("第六块", R.mipmap.icon_normal,R.mipmap.icon_select));
//        list.add(new PieView.Data("第七块", R.mipmap.icon_normal,R.mipmap.icon_select));
//        list.add(new PieView.Data("第八块", R.mipmap.icon_normal,R.mipmap.icon_select));
        pv.setList(list);
        pv.setOnItemSelectListener(new CustomPieView.OnItemSelectListener() {
            @Override
            public void selectPosition(int position, CustomPieView pieView) {
                if (pieView.getList() != null) {
                }
            }
        });
    }

    private void initView() {
        mPieGraph = (PieGraph) findViewById(R.id.pie_graph);
    }

    private void initData() {
        List<PieGraph.PieDataHolder> pieceDataHolders = new ArrayList<>();
        pieceDataHolders.add(new PieGraph.PieDataHolder(20, 0xFFBDEEF5, 0x73BDEEF5, "232132", 5, -1));
        pieceDataHolders.add(new PieGraph.PieDataHolder(20, 0xFFFFE1A2, 0x73FFE1A2, "312312", 5, -1));
        pieceDataHolders.add(new PieGraph.PieDataHolder(12, 0xFFFF8C83, 0x73FF8C83, "213123", 5, -1));
        pieceDataHolders.add(new PieGraph.PieDataHolder(26, 0xFF46BBA8, 0x7346BBA8, "12312312", 4, -1));
        mPieGraph.setPieData(pieceDataHolders);

//        mCustomSeekbar.setMaxValue(5);
        mCustomSeekbar.setProgress(1);
        mCustomSeekbar.setResponseOnTouch(this);//activity实现了下面的接口ResponseOnTouch，每次touch会回调onTouchResponse

    }


    public void onClick() {
        // TODO Auto-generated method stub
        // Intent intent = new Intent(this, LineActivity.class);
        // 异步更新UI的四中方式
        // new MyThread().start();
        // new myAsyncTask().execute();
        // new myThread().start();
        // Handler handler = new Handler();
        // handler.post(new Runnable() {
        // public void run() {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // // 该干什么
        // }
        // });
    }

    @Override
    public void onTouchResponse(float volume) {

    }

    // private class MyThread extends Thread {
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // try {
    // Thread.sleep(2000);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // // 做什么的操作
    // }
    // }

    // private class myAsyncTask extends AsyncTask {
    //
    // @Override
    // protected Object doInBackground(Object... params) {
    // // TODO Auto-generated method stub
    // try {
    // Thread.sleep(3000);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
    // @Override
    // protected void onPostExecute(Object result) {
    // // TODO Auto-generated method stub
    // // 做什么的操作
    // }
    // }

    public class myThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */

    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 随机读取文件内容
     */
    public void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInputMessage() throws IOException {
        System.out.println("请输入您的命令∶");
        byte buffer[] = new byte[1024];
        int count = System.in.read(buffer);
        char[] ch = new char[count - 2];// 最后两位为结束符，删去不要
        for (int i = 0; i < count - 2; i++)
            ch[i] = (char) buffer[i];
        String str = new String(ch);
        return str;
    }

    public void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }
}
