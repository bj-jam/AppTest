package com.app.test.all.pdf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.app.test.R;
import com.app.test.util.ThreadManager;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author lcx
 * Created at 2020/12/22
 * Describe:
 */
public class PDFMainActivity extends Activity implements android.view.View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_main);
    }

    @Override
    public void onClick(android.view.View view) {
        switch (view.getId()) {
            case R.id.tv_list:
                startActivity(new Intent(this, RecyclerViewActivity.class));

                ThreadManager.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            downloadBigActivity("第1页\n正品大牌假一赔十\n11百亿补贴\n" +
                                    "100\n" +
                                    "立即抢购\n\n" +
                                    "第2页\n感恩月福利\n送好友3期免息神券\n我赚100元\n领85\n" + new Random().nextInt(100));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


//                fileOutput("test.doc", "测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word测试生成word");
                break;
            case R.id.tv_viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
        }
    }

    private void createWord() throws Exception {
        //创建一个空的操作word docx版本的对象
//        XWPFDocument document = new XWPFDocument();
//        //创建段落
//        XWPFParagraph paragraph = document.createParagraph();
        //创建一行
//        XWPFRun run = paragraph.createRun();
////        //行的内容
//        run.setText("标题");
//        //字体大小
//        run.setFontSize(14);
//        //是否加粗
//        run.setBold(true);
        //ParagraphAlignment里面有很多选项分别对应不同的排列方式，CENTER:居中
//        paragraph.setAlignment(ParagraphAlignment.CENTER);
//        OutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/test.doc");
//        //把doc输出到输出流中
//        document.write(os);
//        os.close();
    }

    private void fileOutput(String name, String info) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), name);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(info.getBytes());
            fos.close();
            System.out.println("写入成功：");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadBigActivity(String value) throws Exception {
        // 创建Word文件
        XWPFDocument doc = new XWPFDocument();
        // 新建段落
        XWPFParagraph p = doc.createParagraph();
        // 设置段落的对齐方式
        p.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = p.createRun();//创建标题
        if (value.indexOf("\n") > 0) {
            //设置换行
            String[] text = value.split("\n");
            for (int f = 0; f < text.length; f++) {
                if (f != 0) {
                    run.addCarriageReturn();//硬回车
                }
                run.setText(text[f]);
            }
        } else {
            run.setText(value);
        }
        OutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/test.docx");
        doc.write(os);
        os.close();
    }


}
