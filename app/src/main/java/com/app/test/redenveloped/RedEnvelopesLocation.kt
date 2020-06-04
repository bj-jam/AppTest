package com.app.test.redenveloped

import android.graphics.Bitmap
import java.util.*
import kotlin.math.abs

object RedEnvelopesLocation {
    private var random: Random = Random()
    private var randomBound = 0
    private var preRedPacketX = 0

    //获取红包随机生成的x坐标
    fun getNextRedPacketX(offsetSpace: Int, parentWidth: Int, redPacketWidth: Int): Int {
        var space = offsetSpace
        if (space <= 0) {
            space = 30
        }
        if (randomBound <= 0) {
            randomBound = parentWidth - space * 2 - redPacketWidth
        }
        var redPacketX = random.nextInt(randomBound) + space
        val nowSpace = abs(preRedPacketX - redPacketX)
        //防止连续两个红包间距过小
        if (nowSpace < redPacketWidth) {
            //上一个在左边
            if (preRedPacketX <= redPacketX) {
                redPacketX += redPacketWidth
                //如果超出屏幕右边
                if (redPacketX > parentWidth - redPacketWidth - space) {
                    redPacketX = space
                }
            } else {
                //上一个在右边
                redPacketX -= redPacketWidth
                //如果超出屏幕左边
                if (redPacketX <= 0) {
                    redPacketX = parentWidth - redPacketWidth * 2
                }
            }
        }
        preRedPacketX = redPacketX
        return redPacketX
    }

    fun getRandomRedPacket(bitmaps: Array<Bitmap?>?): Bitmap? {
        if (bitmaps == null) {
            return null
        }
        val length: Int = bitmaps.size
        if (length <= 0) {
            return null
        }
        return bitmaps[random.nextInt(length)]
    }

}