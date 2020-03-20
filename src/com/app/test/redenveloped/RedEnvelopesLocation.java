package com.app.test.redenveloped;

import android.graphics.Bitmap;

import java.util.Random;

public class RedEnvelopesLocation {
    private static RedEnvelopesLocation singleObj;
    private Random random;
    private int randomBound;
    private RedEnvelopesLocation() {
        random=new Random();
    }
    public static RedEnvelopesLocation get(){
        if(singleObj==null){
            synchronized (RedEnvelopesLocation.class){
                if(singleObj==null){
                    singleObj=new RedEnvelopesLocation();
                }
            }
        }
        return singleObj;
    }


    private int preRedPacketX;
    //获取红包随机生成的x坐标
    public int getNextRedPacketX(int offsetSpace, int parentWidth, int redPacketWidth) {
        if(offsetSpace<=0){
            offsetSpace=30;
        }
        int redPacketSpace = redPacketWidth /*+ dp2px(context, 10)*/;
        if(random==null){
            random = new Random();
        }
        if(randomBound<=0){
            randomBound=(parentWidth - offsetSpace * 2 - redPacketWidth);
        }
        int redPacketX = random.nextInt(randomBound) + offsetSpace;

        int nowSpace=Math.abs(preRedPacketX - redPacketX);
        //防止连续两个红包间距过小
        if(nowSpace < redPacketSpace){
            //上一个在左边
            if(preRedPacketX <= redPacketX){
                redPacketX=redPacketX+redPacketWidth;
                //如果超出屏幕右边
                if(redPacketX>(parentWidth-redPacketWidth-offsetSpace)){
                    redPacketX=offsetSpace;
                }
            }else{
                //上一个在右边
                redPacketX=redPacketX-redPacketWidth;
                //如果超出屏幕左边
                if(redPacketX<=0){
                    redPacketX=parentWidth-redPacketWidth*2;
                }
            }
        }
        preRedPacketX = redPacketX;
        return redPacketX;
    }

    public Bitmap getRandomRedPacket(Bitmap[]bitmaps){
        int length;
        if(bitmaps==null){
            return null;
        }
        length=bitmaps.length;
        if(length<=0){
            return null;
        }
        if(random==null){
            random = new Random();
        }
        return bitmaps[random.nextInt(length)];
    }
    public int getBitmapWidth(Bitmap bitmap){
        if(bitmap==null){
            return 204;
        }
        return bitmap.getWidth();
    }
}
