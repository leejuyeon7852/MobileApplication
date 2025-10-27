package ddwu.com.mobile.alarmtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 전달받은 intent 에서 ALARM_MESSGE 확인 구현
        val alarmMessage = intent.getStringExtra("ALARM_MESSAGE")
        Log.d("MyBReceiver", "ALARM_MESSAGE: $alarmMessage")
    }
}