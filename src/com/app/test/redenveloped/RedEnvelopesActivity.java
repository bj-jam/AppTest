package com.app.test.redenveloped;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.app.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
public class RedEnvelopesActivity extends Activity {
    private RedEnvelopesLayout rpflRedPacket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_envelopes);
        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rpflRedPacket.setData(toRedPacketList(), 15);
                rpflRedPacket.readyGo();
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        rpflRedPacket = (RedEnvelopesLayout) findViewById(R.id.rpfl_red_packet);
    }

    public static List<RedEnvelopes> toRedPacketList() {
        ArrayList<RedEnvelopes> redPacketList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            RedEnvelopes redPacket = new RedEnvelopes();
            redPacket.setIndex(i);
            redPacket.setMoney(2);
            redPacketList.add(redPacket);
        }
        return redPacketList;
    }
}
