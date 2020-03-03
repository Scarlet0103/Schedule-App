package scarlet.believe.schedule.homepages

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_home_page.*
import scarlet.believe.schedule.*
import java.util.*

class HomePage : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notification_Btn : LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        //notification Button setup
        notification_Btn = findViewById(R.id.notification_Btn)
        notificationManager = NotificationManagerCompat.from(this)
        notification_Btn.setOnClickListener {
            notification_Btn.cancelAnimation()
            notification_Btn.progress = 0f
            sendOnChannel1(it)
            Toast.makeText(this,"You'll be notified",Toast.LENGTH_SHORT).show()
        }
        //viewpager setup
        val  pager = findViewById<ViewPager>(R.id.main_pageviewer)
        val pageadapter : PageAdapterMain =
            PageAdapterMain(supportFragmentManager)
        pageadapter.AddFragment(HomeFragment(),"HOME")
        pageadapter.AddFragment(Subject_Fragment(),"SUBJECTS")
        pager.adapter = pageadapter
        home_tab.setupWithViewPager(pager)

    }


    fun sendOnChannel1(v: View) {
        val c = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        c.set(Calendar.HOUR_OF_DAY,17)
        c.set(Calendar.MINUTE,30)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setInexactRepeating( AlarmManager.RTC_WAKEUP,c.timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
