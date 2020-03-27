package com.app.test.move;

import android.content.Context;
import android.graphics.PointF;

import com.app.test.R;
import com.app.test.util.AnimUtils;
import com.app.test.util.Utils;

/**
 * @author lcx
 * Created at 2020.1.2
 * Describe:
 */
public class MoveImageView extends android.support.v7.widget.AppCompatImageView {

    private AnimUtils animUtils;

    public MoveImageView(Context context) {
        super(context);
        animUtils = getFrameAnimationUtilsConfig();
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }

    private AnimUtils getFrameAnimationUtilsConfig() {
        AnimUtils.Config config = new AnimUtils.Config();
        config.setImageView(this);
        config.setFrameResArray(Utils.getRes(getContext(), R.array.game_gold_scale));
        config.setDuration(10);
        config.setRepeatCount(-1);
        return config.build();
    }

    public void stopAnim() {
        if (!Utils.isEmpty(animUtils))
            animUtils.release();
    }

    public void startAnim() {
        if (!Utils.isEmpty(animUtils))
            animUtils.start();
    }
}