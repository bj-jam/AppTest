package com.app.test.redenveloped

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.app.test.R
import java.util.*

/**
 * @author lcx
 * Created at 2020.1.6
 * Describe:
 */
class RedEnvelopesActivity : Activity() {
    private lateinit var redPacketLayout: RedEnvelopesLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_envelopes)
        initView()
        Handler().postDelayed({
            redPacketLayout.setData(toRedPacketList(), 15)
            redPacketLayout.readyGo()
        }, 2000)
    }

    private fun initView() {
        redPacketLayout = findViewById<View>(R.id.rpfl_red_packet) as RedEnvelopesLayout
    }

    companion object {
        fun toRedPacketList(): List<RedEnvelopes> {
            val redPacketList = ArrayList<RedEnvelopes>()
            for (i in 0..49) {
                val redPacket = RedEnvelopes()
                redPacket.index = i
                redPacket.money = 2
                redPacketList.add(redPacket)
            }
            return redPacketList
        }
    }
}