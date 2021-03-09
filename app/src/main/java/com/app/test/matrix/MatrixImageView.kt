package com.app.test.matrix

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.appcompat.widget.AppCompatImageView
import com.app.test.R

/**
 * @author lcx
 * Created at 2020.1.20
 * Describe:
 */
class MatrixImageView(context: Context?) : AppCompatImageView(context) {
    private val mMatrix: Matrix = Matrix()
    private val mBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_pic17)
    override fun onDraw(canvas: Canvas) {
        println("---> onDraw")
        //画原图
        canvas.drawBitmap(mBitmap, 0f, 0f, null)
        //画经过Matrix变化后的图
        canvas.drawBitmap(mBitmap, mMatrix, null)
        super.onDraw(canvas)
    }

    override fun setImageMatrix(matrix: Matrix) {
        println("---> setImageMatrix")
        mMatrix.set(matrix)
        super.setImageMatrix(matrix)
    }

    val bitmap: Bitmap
        get() {
            println("---> getBitmap" + mBitmap.byteCount)
            return mBitmap
        }

}