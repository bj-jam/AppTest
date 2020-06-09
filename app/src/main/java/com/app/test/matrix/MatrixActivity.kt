package com.app.test.matrix

import android.app.Activity
import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView

/**
 * @author lcx
 * Created at 2020.1.20
 * Describe:
 */
class MatrixActivity : Activity() {
    private lateinit var mMatrixImageView: MatrixImageView
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        mMatrixImageView = MatrixImageView(this@MatrixActivity)
        mMatrixImageView.scaleType = ImageView.ScaleType.MATRIX //??
        mMatrixImageView.setOnTouchListener(TouchListenerImpl())
        setContentView(mMatrixImageView)
    }

    private inner class TouchListenerImpl : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_UP) {
                //1 测试平移
//                testTranslate();
                //2 测试围绕图片中心点旋转
                //testRotate();
                //3 测试围绕原点旋转后平移
                //testRotateAndTranslate();
                //4 缩放
                //testScale();
                //5 水平倾斜
                //testSkewX();
                //6 垂直倾斜
                //testSkewY();
                //7 水平且垂直倾斜
//                testSkewXY();
                //8 水平对称
                //testSymmetryX();
                //9 垂直对称
                //testSymmetryY();
                //10 关于X=Y对称
                testSymmetryXY()
            }
            return true
        }
    }

    //平移
    private fun testTranslate() {
        val matrix = Matrix()
        val width = mMatrixImageView.bitmap.width
        val height = mMatrixImageView.bitmap.height
        matrix.postTranslate(width.toFloat(), height.toFloat())
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    //围绕图片中心点旋转
    private fun testRotate() {
        val matrix = Matrix()
        val width = mMatrixImageView.bitmap.width
        val height = mMatrixImageView.bitmap.height
        matrix.postRotate(45f, width / 2.toFloat(), height / 2.toFloat())
        matrix.postTranslate(width.toFloat(), height.toFloat())
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    //围绕原点旋转后平移
    //注意以下三行代码的执行顺序:
    //matrix.setRotate(45f);
    //matrix.preTranslate(-width, -height);
    //matrix.postTranslate(width, height);
    //先执行matrix.preTranslate(-width, -height);
    //后执行matrix.setRotate(45f);
    //再执行matrix.postTranslate(width, height);
    private fun testRotateAndTranslate() {
        val matrix = Matrix()
        val width = mMatrixImageView.bitmap.width
        val height = mMatrixImageView.bitmap.height
        matrix.setRotate(45f)
        matrix.preTranslate(-width.toFloat(), -height.toFloat())
        matrix.postTranslate(width.toFloat(), height.toFloat())
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    //缩放
    private fun testScale() {
        val matrix = Matrix()
        matrix.setScale(0.5f, 0.5f)
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    //水平倾斜
    private fun testSkewX() {
        val matrix = Matrix()
        matrix.setSkew(0.5f, 0f)
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    // 垂直倾斜
    private fun testSkewY() {
        val matrix = Matrix()
        matrix.setSkew(0f, 0.5f)
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    // 水平且垂直倾斜
    private fun testSkewXY() {
        val matrix = Matrix()
        matrix.setSkew(0.5f, 0.5f)
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    // 水平对称--图片关于X轴对称
    private fun testSymmetryX() {
        val matrix = Matrix()
        val height = mMatrixImageView.bitmap.height
        val matrixValues = floatArrayOf(1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f)
        matrix.setValues(matrixValues)
        //若是matrix.postTranslate(0, height);
        //表示将图片上下倒置
        matrix.postTranslate(0f, height * 2.toFloat())
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    // 垂直对称--图片关于Y轴对称
    private fun testSymmetryY() {
        val matrix = Matrix()
        val width = mMatrixImageView.bitmap.width
        val matrixValues = floatArrayOf(-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
        matrix.setValues(matrixValues)
        //若是matrix.postTranslate(width,0);
        //表示将图片左右倒置
        matrix.postTranslate(width * 2.toFloat(), 0f)
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    // 关于X=Y对称--图片关于X=Y轴对称
    private fun testSymmetryXY() {
        val matrix = Matrix()
        val width = mMatrixImageView.bitmap.width
        val height = mMatrixImageView.bitmap.height
        val matrixValues = floatArrayOf(0f, -1f, 0f, -1f, 0f, 0f, 0f, 0f, 1f)
        matrix.setValues(matrixValues)
        matrix.postTranslate(width + height.toFloat(), width + height.toFloat())
        mMatrixImageView.imageMatrix = matrix
        showMatrixEveryValue(matrix)
    }

    //获取变换矩阵Matrix中的每个值
    private fun showMatrixEveryValue(matrix: Matrix) {
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)
        for (i in 0..2) {
            var valueString = ""
            for (j in 0..2) {
                valueString = matrixValues[3 * i + j].toString() + ""
                println("第" + (i + 1) + "行的第" + (j + 1) + "列的值为" + valueString)
            }
        }
    }
}