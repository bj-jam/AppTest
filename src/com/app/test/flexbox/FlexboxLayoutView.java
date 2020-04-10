package com.app.test.flexbox;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.test.R;
import com.app.test.util.Utils;
import com.binioter.guideview.Component;
import com.binioter.guideview.Guide;
import com.binioter.guideview.GuideBuilder;

import zhy.com.highlight.HighLight;

/**
 * @author lcx
 * Created at 2020.4.1
 * Describe:
 */
public class FlexboxLayoutView extends FrameLayout implements View.OnClickListener {
    private ImageView ivFirst;
    private TextView tvTwo;
    private TextView tvThree;
    private ImageView ivForth;
    private ImageView ivFive;

    public FlexboxLayoutView(Context context) {
        super(context);
        initView();
    }

    public FlexboxLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlexboxLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView() {
        View.inflate(getContext(), R.layout.view_flexbox, this);
        ivFirst = findViewById(R.id.iv_first);
        tvTwo = findViewById(R.id.tv_two);
        tvThree = findViewById(R.id.tv_three);
        ivForth = findViewById(R.id.iv_forth);
        ivFive = findViewById(R.id.iv_five);
        ivFirst.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        ivForth.setOnClickListener(this);
        ivFive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        showGuideView(v);
        onCreateView((Activity) getContext(), v);
    }


    private void onCreateView(Activity activity, View targetView) {
        ViewGroup overlay = (ViewGroup) activity.getWindow().getDecorView();
        GuideView guideView = new GuideView(activity);
        guideView.setFullingColor(activity.getResources().getColor(R.color.black));
        guideView.setFullingAlpha(175);
        guideView.setHighTargetCorner(15);
        guideView.setPadding(2);
//        maskView.setHighTargetGraphStyle();
//        maskView.setOverlayTarget(mConfiguration.mOverlayTarget);
//        maskView.setOnKeyListener(this);
        int parentX = 0;
        int parentY = 0;
        if (overlay != null) {
            int[] loc = new int[2];
            overlay.getLocationInWindow(loc);
            parentX = loc[0];
            parentY = loc[1];
        }
        guideView.setTargetRect(GuideView.getViewAbsRect(targetView, parentX, parentY));
        guideView.setClickable(true);
        guideView.setGuideClickListen(new GuideView.GuideClickListen() {
            @Override
            public void clickTarget() {
                Toast.makeText(getContext(),"关闭",Toast.LENGTH_SHORT).show();
            }
        });
        View tips = View.inflate(getContext(), R.layout.layout_find_guide_tips, null);
        tips.findViewById(R.id.iv_target).setVisibility(GONE);
        guideView.addView(tips, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (!Utils.isEmpty(overlay))
            overlay.addView(guideView);
    }


    Guide guide;

    public void showGuideView(View v) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(v)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
            }
        });

        builder.addComponent(new SimpleComponent());
        guide = builder.createGuide();
        guide.show((Activity) getContext());
    }

    public class SimpleComponent implements Component {

        @Override
        public View getView(LayoutInflater inflater) {
            View ll = inflater.inflate(R.layout.item_move, null);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
                }
            });
            return ll;
        }

        @Override
        public int getAnchor() {
            return Component.ANCHOR_BOTTOM;
        }

        @Override
        public int getFitPosition() {
            return Component.FIT_END;
        }

        @Override
        public int getXOffset() {
            return 100;
        }

        @Override
        public int getYOffset() {
            return 10;
        }
    }

    HighLight mHightLight;

//    public void showNextKnownTipView(View view) {
//        mHightLight = new HighLight(getContext())//
//                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
////                .intercept(false)//设置拦截属性为false 高亮布局不影响后面布局的滑动效果
//                .intercept(true)//拦截属性默认为true 使下方ClickCallback生效
//                .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
////                .setClickCallback(new HighLight.OnClickCallback() {
////                    @Override
////                    public void onClick() {
////                        Toast.makeText(MainActivity.this, "clicked and remove HightLight view by yourself", Toast.LENGTH_SHORT).show();
////                        remove(null);
////                    }
////                })
//                .anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
//                .addHighLight(R.id.btn_rightLight, R.layout.info_known, new OnLeftPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
//                .addHighLight(R.id.btn_light, R.layout.info_known, new OnRightPosCallback(5), new BaseLightShape(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()), 0) {
//                    @Override
//                    protected void resetRectF4Shape(RectF viewPosInfoRectF, float dx, float dy) {
//                        //缩小高亮控件范围
//                        viewPosInfoRectF.inset(dx, dy);
//                    }
//
//                    @Override
//                    protected void drawShape(Bitmap bitmap, HighLight.ViewPosInfo viewPosInfo) {
//                        //custom your hight light shape 自定义高亮形状
//                        Canvas canvas = new Canvas(bitmap);
//                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                        paint.setDither(true);
//                        paint.setAntiAlias(true);
//                        //blurRadius必须大于0
//                        if (blurRadius > 0) {
//                            paint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID));
//                        }
//                        RectF rectF = viewPosInfo.rectF;
//                        canvas.drawOval(rectF, paint);
//                    }
//                })
//                .addHighLight(R.id.btn_bottomLight, R.layout.info_known, new OnTopPosCallback(), new CircleLightShape())
//                .addHighLight(view, R.layout.info_known, new OnBottomPosCallback(10), new OvalLightShape(5, 5, 20))
//                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {//监听移除回调
//                    @Override
//                    public void onRemove() {
//                        Toast.makeText(getContext(), "The HightLight view has been removed", Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//                .setOnShowCallback(new HighLightInterface.OnShowCallback() {//监听显示回调
//                    @Override
//                    public void onShow(HightLightView hightLightView) {
//                        Toast.makeText(getContext(), "The HightLight view has been shown", Toast.LENGTH_SHORT).show();
//                    }
//                }).setOnNextCallback(new HighLightInterface.OnNextCallback() {
//                    @Override
//                    public void onNext(HightLightView hightLightView, View targetView, View tipView) {
//                        // targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
//                        Toast.makeText(getContext(), "The HightLight show next TipView，targetViewID:" + (targetView == null ? null : targetView.getId()) + ",tipViewID:" + (tipView == null ? null : tipView.getId()), Toast.LENGTH_SHORT).show();
//                    }
//                });
//        mHightLight.show();
//    }
}
