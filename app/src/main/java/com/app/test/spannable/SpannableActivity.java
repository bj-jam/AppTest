package com.app.test.spannable;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TabStopSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.test.R;

/**
 * @author lcx
 * Created at 2020.1.18
 * Describe:  在构建除了Spannable 对象以后，就可以使用spannable.setSpan(Obj what, int start, int end, int flags)方法来进行样式的设置了，其中参数what是具体样式的实现对象，start则是该样式开始的位置，end对应的是样式结束的位置，参数 flags，定义在Spannable中的常量，常用的有：
 * <p>
 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含两端start和end所在的端点              (a,b)
 * panned.SPAN_EXCLUSIVE_INCLUSIVE --- 不包含端start，但包含end所在的端点       (a,b]
 * Spanned.SPAN_INCLUSIVE_EXCLUSIVE --- 包含两端start，但不包含end所在的端点   [a,b)
 * Spanned.SPAN_INCLUSIVE_INCLUSIVE--- 包含两端start和end所在的端点                     [a,b]
 * <p>
 * 但实际测试这其中似乎并未有差别，而在start和end相同的情况下，则只对start所在字符的当前行起作用。
 */
public class SpannableActivity extends Activity {
    private TextView tvInfo1;
    private TextView tvInfo2;
    private TextView tvInfo3;
    private TextView tvInfo4;
    private TextView tvInfo5;
    private TextView tvInfo6;
    private TextView tvInfo7;
    private TextView tvInfo8;
    private TextView tvInfo9;
    private TextView tvInfo10;
    private TextView tvInfo11;
    private TextView tvInfo12;
    private TextView tvInfo13;
    private TextView tvInfo14;
    private TextView tvInfo15;
    private TextView tvInfo16;
    private TextView tvInfo17;
    private TextView tvInfo18;
    private TextView tvInfo19;
    private TextView tvInfo20;
    private TextView tvInfo21;
    private TextView tvInfo22;
    private TextView tvInfo23;
    private TextView tvInfo24;
    private TextView tvInfo25;
    private TextView tvInfo26;
    private TextView tvInfo27;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable);
        initView();
        setTvInfo1();
        setTvInfo2();
        setTvInfo3();
        setTvInfo4();
        setTvInfo5();
        setTvInfo6();
        setTvInfo7();
        setTvInfo8();
        setTvInfo9();
        setTvInfo10();
        setTvInfo11();
        setTvInfo12();
        setTvInfo13();
        setTvInfo14();
        setTvInfo15();
        setTvInfo16();
        setTvInfo17();
        setTvInfo18();
        setTvInfo19();
        setTvInfo20();
        setTvInfo21();
        setTvInfo22();
        setTvInfo23();
    }

    private void initView() {
        tvInfo1 = (TextView) findViewById(R.id.tv_info1);
        tvInfo2 = (TextView) findViewById(R.id.tv_info2);
        tvInfo3 = (TextView) findViewById(R.id.tv_info3);
        tvInfo4 = (TextView) findViewById(R.id.tv_info4);
        tvInfo5 = (TextView) findViewById(R.id.tv_info5);
        tvInfo6 = (TextView) findViewById(R.id.tv_info6);
        tvInfo7 = (TextView) findViewById(R.id.tv_info7);
        tvInfo8 = (TextView) findViewById(R.id.tv_info8);
        tvInfo9 = (TextView) findViewById(R.id.tv_info9);
        tvInfo10 = (TextView) findViewById(R.id.tv_info10);
        tvInfo11 = (TextView) findViewById(R.id.tv_info11);
        tvInfo12 = (TextView) findViewById(R.id.tv_info12);
        tvInfo13 = (TextView) findViewById(R.id.tv_info13);
        tvInfo14 = (TextView) findViewById(R.id.tv_info14);
        tvInfo15 = (TextView) findViewById(R.id.tv_info15);
        tvInfo16 = (TextView) findViewById(R.id.tv_info16);
        tvInfo17 = (TextView) findViewById(R.id.tv_info17);
        tvInfo18 = (TextView) findViewById(R.id.tv_info18);
        tvInfo19 = (TextView) findViewById(R.id.tv_info19);
        tvInfo20 = (TextView) findViewById(R.id.tv_info20);
        tvInfo21 = (TextView) findViewById(R.id.tv_info21);
        tvInfo22 = (TextView) findViewById(R.id.tv_info22);
        tvInfo23 = (TextView) findViewById(R.id.tv_info23);
        tvInfo24 = (TextView) findViewById(R.id.tv_info24);
        tvInfo25 = (TextView) findViewById(R.id.tv_info25);
        tvInfo26 = (TextView) findViewById(R.id.tv_info26);
        tvInfo27 = (TextView) findViewById(R.id.tv_info27);
    }

    private void setTvInfo1() {


        //顾名思义，AbsoluteSizeSpan是指绝对尺寸，通过指定绝对尺寸来改变文本的字体大小。该类有三个构造函数：
        //AbsoluteSizeSpan(int size)、AbsoluteSizeSpan(int size, boolean dip)、AbsoluteSizeSpan(Parcel src)。
        //AbsoluteSizeSpan(int size)：参数size， 以size的指定的像素值来设定文本大小。
        //AbsoluteSizeSpan(int size, boolean dip)：参数size，以size的指定像素值来设定文本大小，如果参数dip为true则以size指定的dip为值来设定文本大小。
        //AbsoluteSizeSpan(Parcel src)：参数src，包含有size和dip值的包装类。在该构造中
        //public AbsoluteSizeSpan(Parcel src) {
        //mSize = src.readInt();
        //mDip = src.readInt() != 0;
        //}
        Parcel p = Parcel.obtain();
        p.writeInt(29);//字体大小
        p.writeInt(1);//是否是dip单位
        p.setDataPosition(0);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(p);

        StringBuilder builder = new StringBuilder();
        builder.append("我是来测试AbsoluteSizeSpan的用法的");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(builder.toString());
        spannableStringBuilder.setSpan(ass, 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvInfo1.setText(spannableStringBuilder);
    }

    private void setTvInfo2() {
        //AlignmentSpan.Standard， 标准文本对齐样式，该类有两个构造函数，
        // AlignmentSpan.Standard(Layout.Alignment align)和AlignmentSpan.Standard(Parcel src)。AlignmentSpan.Standard(Layout.Alignment align)
        // 参数align，Layout.Alignment类型的枚举值。包括居中、正常和相反三种情况。
        //AlignmentSpan.Standard(Parcel src)：参数src，包含有标准字符串的Parcel类，
        // 其值应为"ALIGN_CENTER"、"ALIGN_NORMAL" 或"ALIGN_OPPOSITE"中的之一，对应Layout.Alignment枚举中的三个类型。使用示例：
        Parcel p = Parcel.obtain();
        p.writeString(Layout.Alignment.ALIGN_OPPOSITE.toString());
        p.setDataPosition(0);
        AlignmentSpan.Standard standard = new AlignmentSpan.Standard(p);

        StringBuilder builder = new StringBuilder();
        builder.append("我是来测试AlignmentSpan.Standard的用法的");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(builder.toString());
        spannableStringBuilder.setSpan(standard, 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvInfo2.setText(spannableStringBuilder);
    }

    private void setTvInfo3() {
        //BackgroundColorSpan，背景色样式，显然可以用来设定文本的背景色。该类有两个构造函数，BackgroundColorSpan(int color)和BackgroundColorSpan(Parcel src)。
        //BackgroundColorSpan(int color)：参数color，颜色值。
        //BackgroundColorSpan(Parcel src)：参数src，包含颜色值信息的包装类，使用方法如下:
        Parcel p = Parcel.obtain();
        p.writeInt(Color.GREEN);
        p.setDataPosition(0);
        BackgroundColorSpan bcs = new BackgroundColorSpan(p);

        StringBuilder builder = new StringBuilder();
        builder.append("我是来测试BackgroundColorSpan的用法的");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(builder.toString());
        spannableStringBuilder.setSpan(bcs, 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvInfo3.setText(spannableStringBuilder);
    }

    private void setTvInfo4() {
        //BulletSpan， 着重样式，类似于HTML中的<li>标签的圆点效果。该类有4个构造函数BulletSpan()、BulletSpan(int gapWidth)、BulletSpan(int gapWidth,int color)、BulletSpan(Parcel src)。
        //BulletSpan()：仅提供一个与文本颜色一致的符号。
        //BulletSpan(int gapWidth)： 提供一个与文本颜色一致的符号，并指定符号与后面文字之间的空白长度。
        //BulletSpan(int gapWidth,int color)：提供一个指定颜色的符号，并指定符号与后面文字之间的宽度。
        //BulletSpan(Parcel src)：参数src，包含宽度、颜色信息的包装类，在以此构造时，构造函数的调用如下：
        //mGapWidth = src.readInt();d
        //mWantColor = src.readInt() != 0;\nmColor = src.readInt();
        Parcel p = Parcel.obtain();
        p.writeInt(30);//设置gapWidth
        p.writeInt(1);//设置是否使用颜色
        p.writeInt(Color.RED);//设置颜色
        p.setDataPosition(0);
        BulletSpan bs3 = new BulletSpan(p);

        SpannableString spanString = new SpannableString("我是来测试BulletSpan的用法的");
        spanString.setSpan(bs3, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo4.setText(spanString);
    }

    private void setTvInfo5() {
        //DrawableMarginSpan，图片+Margin样式，该类有两个构造函数：DrawableMarginSpan(Drawable b)、DrawableMarginSpan(Drawable b,int pad)。
        //DrawableMarginSpan(Drawable b)：参数b，用于显示的图片。
        //DrawableMarginSpan(Drawable b,int pad)：参数b，用于显示的图片，参数pad，图片和文字的距离。
        DrawableMarginSpan bs3 = new DrawableMarginSpan(getResources().getDrawable(R.drawable.ads_video_close2), 30);


        SpannableString spanString = new SpannableString("我是来测试DrawableMarginSpan的用法的");
        spanString.setSpan(bs3, 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo5.setText(spanString);

//        StringBuilder builder = new StringBuilder();
//        builder.append("5我是来测试Spannable的用法的");
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(builder.toString());
//        spannableStringBuilder.setSpan(bs3, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvInfo5.setText(spannableStringBuilder);
    }

    private void setTvInfo6() {
        //ForegroundColorSpan，字体颜色样式，用于改变字体颜色。该类有两个构造函数：ForegroundColorSpan(int color)、ForegroundColorSpan(Parcel src)。
        //ForegroundColorSpan(int color)：参数color，字体颜色。
        //ForegroundColorSpan(Parcel src)：参数src，包含字体颜色信息的包装类，
        Parcel p = Parcel.obtain();
        p.writeInt(Color.RED);
        p.setDataPosition(0);
        ForegroundColorSpan fcs = new ForegroundColorSpan(p);

        SpannableString spanString = new SpannableString("我是来测试ForegroundColorSpan的用法的");
        spanString.setSpan(fcs, 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo6.setText(spanString);
    }

    private void setTvInfo7() {
        //IconMarginSpan，图标+Margin样式，该类与DrawableMarginSpan使用上很相似。本类有两个构造函数：
        //IconMarginSpan(Bitmap b)：参数b，用于显示图像的bitmap。
        //IconMarginSpan(Bitmap b,int pad)：参数b，用于显示图像的bitmap，参数pad，Bitmap和文本之间的间距
        IconMarginSpan bs3 = new IconMarginSpan(BitmapFactory.decodeResource(getResources(), R.drawable.ads_video_close2), 30);

        SpannableString spanString = new SpannableString("我是来测试IconMarginSpan的用法的");
        spanString.setSpan(bs3, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo7.setText(spanString);
    }

    private void setTvInfo8() {
        //ImageSpan，图片样式，主要用于在文本中插入图片。本类构造函数较多，但主要是针对Bitmap和Drawable的，也可以通过资源Id直接加载图片。如下：
        //ImageSpan(Bitmap b)：.参数b，用于显示的Bitmap。该方法已过时，改用Use ImageSpan(Context, Bitmap)代替。
        //ImageSpan(Bitmap b, int verticalAlignment)：参数b，用于显示的Bitmap，参数verticalAlignment，对齐方式，对应ImageSpan中的常量值。该方法已过时，改用ImageSpan(Context, Bitmap, int)代替。
        //ImageSpan(Context context, Bitmap b)：参数context，传入的上下文，参数b，用于显示的Bitmap。
        //ImageSpan(Context context, Bitmap b, int verticalAlignment)：参数context，传入的上下文，参数b，用于显示的Bitmap，参数verticalAlignment，对齐方式。
        //ImageSpan(Drawable d)：参数d，用于显示的Drawable，此Drawable须设置大小。
        //ImageSpan(Drawable d, int verticalAlignment)：参数d，用于显示的Drawable，参数verticalAlignment，对齐方式。
        //ImageSpan(Drawable d, String source)：参数d，用于显示的Drawable，参数source，资源字符串。
        //ImageSpan(Drawable d, String source, int verticalAlignment)：参数d，用于显示的Drawable，参数source，资源字符串，参数verticalAlignment，对齐方式。
        //ImageSpan(Context context, Uri uri)：参数context，传入的上下文，参数uri，图片的uri。
        //ImageSpan(Context context, Uri uri, int verticalAlignment)：参数context，传入的上下文，参数uri，图片的uri，参数verticalAlignment，对齐方式。
        //ImageSpan(Context context, int resourceId)：参数context，传入的上下文，参数resourceId，图片的资源id。
        //ImageSpan(Context context, int resourceId, int verticalAlignment)参数context，传入的上下文，参数resourceId，图片的资源id，参数verticalAlignment，对齐方式。
        //————————————————
        ImageSpan bs3 = new ImageSpan(this, R.drawable.ads_video_close2, DynamicDrawableSpan.ALIGN_BASELINE);

        SpannableString spanString = new SpannableString("我是来测试ImageSpan的用法的");
        spanString.setSpan(bs3, 3, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo8.setText(spanString);
    }

    private void setTvInfo9() {
        //LeadingMarginSpan.Standard，文本缩进的样式。有3个构造函数，分别为：
        //Standard(int arg0)：参数arg0，缩进的像素。
        //Standard(int arg0, int arg1)：参数arg0，首行缩进的像素，arg1，剩余行缩进的像素。
        //Standard(Parcel p)： 参数p，包含缩进信息的包装类。在构造时，
        //public Standard(Parcel src) {
        //mFirst = src.readInt();\n"+
        //mRest = src.readInt();\n"+
        //}
        Parcel p = Parcel.obtain();
        p.writeInt(20);
        p.writeInt(100);
        p.setDataPosition(0);
        LeadingMarginSpan.Standard lms = new LeadingMarginSpan.Standard(p);

        SpannableString spanString = new SpannableString("我是来测试LeadingMarginSpan.Standard的用法的9我是来测试LeadingMarginSpan.Standard的用法的9我是来测试LeadingMarginSpan.Standard的用法的");
        spanString.setSpan(lms, 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo9.setText(spanString);
    }

    private void setTvInfo10() {
        //MaskFilterSpan，滤镜样式，只有一个构造函数：
        //MaskFilterSpan(MaskFilter filter)：参数filter，滤镜样式。
        //在android系统里，MaskFilter提供了两个子类，BlurMaskFilter和EmbossMaskFilter，分别用来制作模糊效果和浮雕效果。
        //android:hardwareAccelerated="false"
        String content = "我是you来测试MaskFilterSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        MaskFilterSpan embossMaskFilterSpan = new MaskFilterSpan(new EmbossMaskFilter(new float[]{3, 3, 9}, 3.0f, 12, 16));
        ssb.setSpan(embossMaskFilterSpan, 5, 5 + 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo10.setText(ssb);
        String you = "you";
        int indexYou = content.indexOf(you);
        MaskFilterSpan blurMaskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(3, BlurMaskFilter.Blur.OUTER));
        ssb.setSpan(blurMaskFilterSpan, indexYou, indexYou + you.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo10.setText(ssb);
    }

    private void setTvInfo11() {
        //QuoteSpan，引用样式，在文本左侧添加一条表示引用的竖线，该类有3个构造函数：
        //QuoteSpan()：无参构造，默认颜色为蓝色。
        //QuoteSpan(int color)：参数color，颜色值。
        //QuoteSpan(Parcel src)：包含颜色值信息的包装类

        SpannableStringBuilder ssb = new SpannableStringBuilder("我是来测试QuoteSpan的用法的");
        ssb.setSpan(new QuoteSpan(0xffff00ff), 0, 0, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo11.setText(ssb);
    }

    private void setTvInfo12() {
        //ScaleXSpan，横向缩放样式，将字体按比例进行横向缩放。构造函数：
        //ScaleXSpan(float proportion)：参数proportion，缩放比例。如果字体设置的大小为A，则实际显示为A×proportion。
        //ScaleXSpan(Parcel src)：参数src，包含了缩放比例信息的包装类。使用：
        //Parcel p = Parcel.obtain();
        //p.writeFloat(2.5f);
        //p.setDataPosition(0);
        //ScaleXSpan rss = new ScaleXSpan(p);
        String content = "我是you来测试ScaleXSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new ScaleXSpan(2.0f), 0, content.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo12.setText(ssb);
    }

    private void setTvInfo13() {
        //StrikethroughSpan，删除线样式。该类有两个构造函数：
        //StrikethroughSpan()和SrickkethroughSapn(Parcel src)。但有参数的构造函数并未对src参数做处理，
        //public StrikethroughSpan(Parcel src) {
        //}
        //因此这两个构造函数完全是同样的效果。
        String content = "我是you来测试StrikethroughSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new StrikethroughSpan(), 0, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo13.setText(ssb);
    }

    private void setTvInfo14() {
        //StyleSpan，主要由正常、粗体、斜体和同时加粗倾斜四种样式，常量值定义在Typeface类中。构造函数：
        //StyleSpan(int style)：参数style，定义在Typeface中的常量
        //
        //    Typeface.NORMAL
        //
        //    Typeface.BOLD
        //
        //    Typeface.ITALIC
        //
        //    Typeface.BOLD_ITALIC。
        //StyleSpan(Parcel src)：参数src，包含字体信息的包装类，用法：
        //Parcel p = Parcel.obtain();
        //p.writeInt(Typeface.BOLD_ITALIC);
        //p.setDataPosition(0);
        //StyleSpan ss = new StyleSpan(p);
        String content = "我是you来测试StyleSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 1, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo14.setText(ssb);
    }

    private void setTvInfo15() {
        //SuperscriptSpan，上标样式，比如数学上的次方运算，当然，还可以对上标文字进行缩放。构造函数：
        //SuperscriptSpan()：无参构造。
        //SuperscriptSpan(Parcel src)：一参构造，参数src并未起任何作用，源码中为：
        //public SuperscriptSpan(Parcel src) {
        //}
        String content = "我是来测试SubscriptSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.replace(5, 5 + 9, "SubscriptSpan");
        Parcel parcel = Parcel.obtain();
        parcel.writeInt(6);
        int sixPosition = ssb.toString().indexOf("n");
        ssb.setSpan(new SubscriptSpan(parcel), sixPosition, sixPosition + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        parcel.recycle();
        tvInfo15.setText(ssb);
    }

    private void setTvInfo16() {
        //SuperscriptSpan，上标样式，比如数学上的次方运算，当然，还可以对上标文字进行缩放。构造函数：
        //SuperscriptSpan()：无参构造。
        //SuperscriptSpan(Parcel src)：一参构造，参数src并未起任何作用，源码中为：
        String content = "我是来测试SuperscriptSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.replace(5, 5 + 9, "SuperscriptSpan");
        Parcel parcel = Parcel.obtain();
        parcel.writeInt(6);
        int sixPosition = ssb.toString().indexOf("n");
        ssb.setSpan(new SuperscriptSpan(parcel), sixPosition, sixPosition + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        parcel.recycle();
        tvInfo16.setText(ssb);
    }

    private void setTvInfo17() {
        //TabStopSpan.Standard，制表位偏移样式，距离每行的leading margin的偏移量，据测试在首行加入制表符时才产生效果。构造函数：
        //TabStopSpan.Standard(int where)：参数where，偏移量。
        String content = "我是来测试 TabStopSpan.Standard 的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        String[] subs = content.split(" ");
        ssb = new SpannableStringBuilder();
        /**
         * TabStopSpan. Standard related to \t and \n
         * TabStopSpan.Standard 跟 \t 和 \n 有关系
         */
        for (String sub1 : subs) {
            ssb.append("\t").append(sub1).append(" ");
            ssb.append("\n");
        }
        ssb.setSpan(new TabStopSpan.Standard(50), 0, ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo17.setText(ssb);
    }

    private void setTvInfo18() {
        //TextAppearanceSpan，使用style文件来定义文本样式，该类有4个构造函数：
        //TextAppearanceSpan(Context context, int appearance)：参数context，传入的上下文，参数appearance，引用的样式表，如R.style.my_style。
        //TextAppearanceSpan(Context context, int appearance, int colorList)：参数context，使用的上下文，参数appearance，引用的样式表，
        // 如R.style.my_style，参数 colorList，使用方式未知，如果设置为小于0，则参数将不产生效果。
        //TextAppearanceSpan(String family, int style, int size,ColorStateList color, ColorStateList linkColor)：参数family，字体，
        // 仅支持系统自带的三种字体，MONOSPACE、SERIF和SANS，参数 style，TypeFace中定义的字体样式，BOLD、ITALIC等，参数size
        // ，字体大小，参数color，字体颜色，参数 linkColor，使用方式未知。TextAppearanceSpan(Parcel src)：参数src，含有样式信息的包装类，样式信息参照5参构造。
        String content = "我是来测试 TextAppearanceSpan 的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ColorStateList colorStateList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorStateList = this.getColorStateList(R.color._94c8f9);
        } else {
//            try {
//                colorStateList = ColorStateList.createFromXml(getResources(), getResources().getXml(R.color.pink));
//            } catch (XmlPullParserException | IOException e) {
//                e.printStackTrace();
//            }
        }
        ssb.setSpan(new TextAppearanceSpan("serif", Typeface.BOLD_ITALIC, getResources().getDimensionPixelSize(R.dimen.dp22),
                colorStateList, colorStateList), 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo18.setText(ssb);
    }

    private void setTvInfo19() {
        //TypefaceSpan，字体样式，可以设置不同的字体，比如系统自带的SANS_SERIF、MONOSPACE和SERIF。构造函数：
        //TypefaceSpan(String family)：参数family，字体的值，以字符串表示。
        //TypefaceSpan(Parcel src)： 参数src，包含字体family信息的包装类，
        String content = "我是来测试TyTyTyTyTyTyTypefaceSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new TypefaceSpan("serif"), 0, 13, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo19.setText(ssb);
    }

    private void setTvInfo20() {
        //UnderlineSpan，下划线样式，给一段文字加上下划线。构造函数：
        //UnderlineSpan()： 无参构造。
        //UnderlineSpan(Parcel src)：一参构造， 与无参构造效果相同，构造中未对src做处理
        String content = "我是来测试UnderlineSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new UnderlineSpan(), 0, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo20.setText(ssb);
    }

    private void setTvInfo21() {
        //URLSpan，可以打开一个链接。两个构造函数：
        //URLSpan(String url)：参数url，链接地址。
        //URLSpan(Parcel src)：参数src，包含链接地址信息的包装类
        String content = "我是来测试URLSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new URLSpan("https://github.com/CaMnter"), 0, 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo21.setText(ssb);
        // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        tvInfo21.setMovementMethod(LinkMovementMethod.getInstance());
        // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        tvInfo21.setHighlightColor(0xff8FABCC);
    }

    private void setTvInfo22() {
        //proportion：大小比例。
        String content = "我是来测试RelativeSizeSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new RelativeSizeSpan(6.0f), 0, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvInfo22.setText(ssb);
    }

    private void setTvInfo23() {
        //proportion：大小比例。
        String content = "我是来测试RelativeSizeSpan的用法的";
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        SpanClickableSpan spanClickableSpan = new SpanClickableSpan(0xffFF4081, new ClickableSpanNoUnderline.OnClickListener<SpanClickableSpan>() {
            /**
             * ClickableSpan被点击
             *
             * @param widget widget
             * @param span   span
             */
            @Override
            public void onClick(View widget, SpanClickableSpan span) {
                String urlString = span.getUrlString();
                if (TextUtils.isEmpty(urlString)) return;
                Uri uri = Uri.parse(urlString);
                Context context = widget.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.w("URLSpan", "Activity was not found for intent, " + intent.toString());
                }
            }
        });
        spanClickableSpan.setUrlString("https://github.com/CaMnter");
        ssb.setSpan(spanClickableSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvInfo23.setText(ssb);
        // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        tvInfo23.setMovementMethod(LinkMovementMethod.getInstance());
        // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        tvInfo23.setHighlightColor(0x00000000);
    }
}
