package com.app.test.vector.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.app.test.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 地图控件
 */
public class MapView extends View {
    private final int[] colors = new int[]{0xFF852800, 0xFFDA4000, 0xFFFFB08F, 0xFFFADED2};
    //资源id
    private int rawId;
    private List<ProvinceItem> provinceItems;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //选中的item
    private ProvinceItem selectItem;
    //svg的宽高
    private float svgWidth, svgHeight;
    private float scale = 1.3f;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rawId = R.raw.vector_china;

        ParseThread.start();
    }

    private Thread ParseThread = new Thread(new Runnable() {
        @Override
        public void run() {
            InputStream inputStream = getContext().getResources().openRawResource(rawId);
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = builder.parse(inputStream);
                Element root = document.getDocumentElement();//获取根节点
                NodeList nodeList = root.getElementsByTagName("path");

                provinceItems = new ArrayList<>(nodeList.getLength());
                float left, top, right, bottom;
                left = top = right = bottom = -1;

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);//path节点
                    //解析pathData属性
                    ProvinceItem provinceItem = new ProvinceItem();
                    provinceItem.setPath(PathParser.createPathFromPathData(element.getAttribute("android:pathData")));

                    Rect bound = provinceItem.getRegion().getBounds();
                    if (left > bound.left || left == -1) left = bound.left;
                    if (top > bound.top || top == -1) top = bound.top;
                    if (right < bound.right || right == -1) right = bound.right;
                    if (bottom < bound.bottom || bottom == -1) bottom = bound.bottom;
                    //设置color
                    int random = new Random().nextInt(4);
                    switch (random) {
                        case 0:
                            provinceItem.setColor(colors[0]);
                            break;
                        case 1:
                            provinceItem.setColor(colors[1]);
                            break;
                        case 2:
                            provinceItem.setColor(colors[2]);
                            break;
                        case 3:
                            provinceItem.setColor(colors[3]);
                            break;
                    }
                    provinceItems.add(provinceItem);
                }

                svgWidth = right - left;
                svgHeight = bottom - top;

                postInvalidate();
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onDraw(Canvas canvas) {
        scale = getMeasuredWidth() / svgWidth;
        //缩放取小的值
        scale = Math.min(scale, getMeasuredHeight() / svgHeight);

        canvas.save();
        canvas.scale(scale, scale);
        for (ProvinceItem provinceItem : provinceItems) {
            if (selectItem != provinceItem)
                provinceItem.drawPath(canvas, mPaint, false);
        }

        if (selectItem != null) selectItem.drawPath(canvas, mPaint, true);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP: {
                ProvinceItem temp = null;
                for (ProvinceItem provinceItem : provinceItems) {
                    if (provinceItem.isOnTouch(event.getX() / scale, event.getY() / scale)) {
                        temp = provinceItem;
                        break;
                    }
                }

                if (temp != null) {
                    selectItem = temp;
                    postInvalidate();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
