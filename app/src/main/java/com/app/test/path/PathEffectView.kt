package com.app.test.path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.app.test.R
import com.app.test.util.DensityUtil

/**
 *
 * @author lcx
 * Created at 2020.3.31
 * Describe:
 */
class PathEffectView : View {
    private val paint: Paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.color = resources.getColor(R.color.Green)
            it.style = Paint.Style.STROKE
            it.strokeWidth = DensityUtil.dp2px(1).toFloat()
        }
    }
    private val path by lazy { Path() }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val pathEffect: PathEffect = DashPathEffect(floatArrayOf(10f, 5F), 10f);
        paint.pathEffect = pathEffect;
        canvas?.drawCircle(100f, 100f, 50f, paint);

        path.reset()
        path.moveTo(100f, 100f)
        path.lineTo(150f, 50f)
        path.lineTo(200f, 150f)
        path.lineTo(250f, 0f)
        canvas?.drawPath(path, paint);

        path.reset()
        path.moveTo(100f, 200f)
        path.lineTo(150f, 150f)
        path.lineTo(200f, 250f)
        path.lineTo(250f, 100f)
        //CornerPathEffect 拐角变圆点
        val pathEffect1: PathEffect = CornerPathEffect(50f);
        paint.pathEffect = pathEffect1;
        canvas?.drawPath(path, paint);

        path.reset()
        path.moveTo(100f, 300f)
        path.lineTo(150f, 250f)
        path.lineTo(200f, 350f)
        path.lineTo(250f, 200f)
        val pathEffect2: PathEffect = DiscretePathEffect(20f, 5f);
        paint.pathEffect = pathEffect2;
        canvas?.drawPath(path, paint);

        //PathDashPathEffect 利用 Path 绘制 线条
        path.reset()
        path.moveTo(100f, 400f)
        path.lineTo(150f, 350f)
        path.lineTo(200f, 450f)
        path.lineTo(250f, 300f)
        val dashPath: Path = Path(); // 使用一个三角形来做 dash
        dashPath.moveTo(0f, 0f)
        dashPath.lineTo(10f, 10f)
        dashPath.lineTo(10f, 0f)
        dashPath.close()
        val pathEffect3: PathEffect = PathDashPathEffect(dashPath, 10f, 0f, PathDashPathEffect.Style.TRANSLATE);
        paint.pathEffect = pathEffect3;
        canvas?.drawPath(path, paint);

        path.reset()
        path.moveTo(100f, 500f)
        path.lineTo(150f, 450f)
        path.lineTo(200f, 550f)
        path.lineTo(250f, 600f)
        val dashPath1: Path = Path(); // 使用一个三角形来做 dash
        dashPath.moveTo(0f, 0f)
        dashPath.lineTo(10f, 10f)
        dashPath.lineTo(10f, 0f)
        dashPath.close()
        val pathEffect4: PathEffect = PathDashPathEffect(dashPath1, 10f, 0f, PathDashPathEffect.Style.TRANSLATE);
        paint.pathEffect = pathEffect4;
        canvas?.drawPath(path, paint);
    }
}