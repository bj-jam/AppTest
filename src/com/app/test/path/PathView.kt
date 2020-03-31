package com.app.test.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.support.annotation.RequiresApi
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
class PathView : View {
    private val paint: Paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.color = resources.getColor(R.color.Green)
            it.style = Paint.Style.STROKE
            it.strokeWidth = DensityUtil.dp2px(5).toFloat()
        }
    }
    private var path: Path? = null
    private val path2: Path by lazy { Path() }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        path = Path();
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path?.also {
            it.moveTo(200f, 200f)
            it.lineTo(100f, 400f)
            it.lineTo(300f, 400f)
            it.close()
            canvas?.drawPath(it, paint)
        }
        path?.also {
            it.reset()
            it.moveTo(100f, 800f)
            it.lineTo(200f, 500f)
            it.lineTo(300f, 800f)
            it.lineTo(400f, 500f)
            it.lineTo(500f, 800f)
            canvas?.drawPath(it, paint)
        }
        paint.color = resources.getColor(R.color.red)
        path?.also {
            it.reset()
            //添加各种角度弧线
            it.arcTo(100f, 0f, 200f, 100f, 0f, 90f, true);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset()
            it.arcTo(100f, 100f, 200f, 200f, 0f, 180f, true);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset()
            it.arcTo(100f, 200f, 200f, 300f, 270f, 180f, true);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset()
            it.arcTo(100f, 300f, 200f, 400f, 0f, 180f, false);
            canvas?.drawPath(it, paint);
        }
        paint.color = resources.getColor(R.color.Blue)

        path?.also {
            it.reset()
            //添加弧形到path
            it.addArc(100f, 100f, 300f, 300f, 0f, 270f);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            //添加圆形到path
            it.addCircle(200f, 500f, 100f, Path.Direction.CCW);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            //添加矩形到path
            it.addRect(100f, 700f, 300f, 800f, Path.Direction.CW);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            //添加椭圆到path
            it.addOval(100f, 900f, 300f, 1000f, Path.Direction.CCW);
            canvas?.drawPath(it, paint);

        }




        paint.style = Paint.Style.FILL;
        paint.color = resources.getColor(R.color.yello)
        path?.also {
            it.reset()
            path2.reset();
            it.addCircle(450f, 150f, 100f, Path.Direction.CW);
            path2.addCircle(600f, 150f, 100f, Path.Direction.CW);
            it.op(path2, Path.Op.UNION);
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset();
            path2.reset();
            it.addCircle(450f, 400f, 100f, Path.Direction.CW);
            path2.addCircle(600f, 400f, 100f, Path.Direction.CW);
            it.op(path2, Path.Op.REVERSE_DIFFERENCE);
            //Path.Op.REVERSE_DIFFERENCE
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset();
            path2.reset();
            it.addCircle(450f, 650f, 100f, Path.Direction.CW);
            path2.addCircle(600f, 650f, 100f, Path.Direction.CW);
            it.op(path2, Path.Op.INTERSECT);
            //Path.Op.INTERSECT
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset();
            path2.reset();

            it.addCircle(450f, 900f, 100f, Path.Direction.CW);
            path2.addCircle(600f, 900f, 100f, Path.Direction.CW);
            it.op(path2, Path.Op.DIFFERENCE);
            //Path.Op.DIFFERENCE
            canvas?.drawPath(it, paint);
        }
        path?.also {
            it.reset();
            path2.reset();
            it.addCircle(450f, 1150f, 100f, Path.Direction.CW);
            path2.addCircle(600f, 1150f, 100f, Path.Direction.CW);
            it.op(path2, Path.Op.XOR);
            //Path.Op.XOR
            canvas?.drawPath(it, paint);
        }
    }
}