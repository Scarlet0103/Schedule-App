package scarlet.believe.schedule

import android.app.Notification
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent?) {
        val notificationHelper = NotificationHelper(p0)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager!!.notify(1, nb.build())
    }
}