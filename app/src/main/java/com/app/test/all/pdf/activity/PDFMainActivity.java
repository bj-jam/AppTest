package com.app.test.all.pdf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.app.test.R;

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
                try {
                    downloadBigActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void downloadBigActivity() throws Exception {
        XWPFDocument doc = new XWPFDocument();// 创建Word文件
        XWPFParagraph p = doc.createParagraph();// 新建段落
        p.setAlignment(ParagraphAlignment.LEFT);// 设置段落的对齐方式
        XWPFRun r = p.createRun();//创建标题

        r.setText("dfasdfasdfasdfasdfasdfas\ndjfladjflkasdjfkajds\tkdjflajdfklajsdf\n" + new Random().nextInt(100));
        r.setFontSize(21); //设置字体大小


        OutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/test.docx");
        //workbook将Excel写入到response的输出流中，供页面下载该Excel文件
        doc.write(os);
        os.close();
    }


}
