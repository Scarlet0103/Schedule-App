package scarlet.believe.schedule.authentication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.contentValuesOf
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import scarlet.believe.schedule.homepages.HomePage
import scarlet.believe.schedule.R
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    val handler = Handler()
    private lateinit var ref1 : DatabaseReference
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wifi_animation.isVisible = false
        val uid = FirebaseAuth.getInstance().uid
        wifi_animation.isVisible = !internetconnection()
        handler.postDelayed({

            ref1 = FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        // The child doesn't exist
                        auth.signOut()
                        val intent = Intent(this@MainActivity, RegistrationFirebase::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    else
                    {
                        val intent = Intent(this@MainActivity, HomePage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            })

        },2000)
    }

    private fun internetconnection() : Boolean {

        var i : Boolean = false
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                i = true
                super.onAvailable(network)

            }

            override fun onLost(network: Network) {
                i = false
                super.onLost(network)
            }
        })

        return i
    }
}
