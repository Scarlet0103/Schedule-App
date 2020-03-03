package scarlet.believe.schedule

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.io.File

class NotificationHelper(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }

            return mManager
        }
    val activityIntent = Intent(this,AttendanceNotification::class.java)
    val contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0
    )
    val channelNotification: NotificationCompat.Builder
        @RequiresApi(Build.VERSION_CODES.M)
        get() = NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("Attendance")
            .setContentText("mark your attendance")
            .setSmallIcon(scarlet.believe.schedule.R.drawable.ic_notification_24dp)
            .setColor(getColor(scarlet.believe.schedule.R.color.background_light))
            .setSound(Uri.parse("android.resource://"+getPackageName()+"/raw/wind_chimes"))
            .setAutoCancel(true)
            .setContentIntent(contentIntent)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

        manager!!.createNotificationChannel(channel)
    }

    companion object {
        val channelID = "channelID"
        val channelName = "Channel Name"
    }
}