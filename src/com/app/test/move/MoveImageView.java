package com.app.test.move;

import android.content.Context;
import android.graphics.PointF;

import com.app.test.R;
import com.app.test.util.FrameAnimationUtils;
import com.app.test.util.UIUtils;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
public class MoveImageView extends android.support.v7.widget.AppCompatImageView {

    private FrameAnimationUtils frameAnimationUtils;

    public MoveImageView(Context context) {
        super(context);
        frameAnimationUtils = getFrameAnimationUtilsConfig();
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }

    private FrameAnimationUtils getFrameAnimationUtilsConfig() {
        FrameAnimationUtils.Config config = new FrameAnimationUtils.Config();
        config.setImageView(this);
        config.setFrameResArray(UIUtils.getRes(getContext(), R.array.game_gold_scale));
        config.setDuration(10);
        config.setRepeatCount(-1);
        return config.build();
    }

    public void stopAnim() {
        if (!Utils.isEmpty(frameAnimationUtils))
            frameAnimationUtils.release();
    }

    public void startAnim() {
        if (!Utils.isEmpty(frameAnimationUtils))
            frameAnimationUtils.start();
    }
}