package scarlet.believe.schedule.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_firebase.*
import scarlet.believe.schedule.homepages.HomePage
import scarlet.believe.schedule.R

class LoginFirebase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_firebase)

        //progress_loading_login.isVisible = false
        card_loading.isVisible=false

        //login with login button
        login_btn.setOnClickListener {
            val emailtxt = email_txt_login.text.toString()
            val passwordtxt = password_txt_login.text.toString()
            if(emailtxt.isEmpty() || passwordtxt.isEmpty()){
                Toast.makeText(this,"Please enter email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //progress_loading_login.isVisible=true
            card_loading.isVisible=true
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailtxt,passwordtxt)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Main","Login Success: ${it.result?.user?.uid}")
                    val intent = Intent(this, HomePage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    //progress_loading_login.isVisible=false
                    card_loading.isVisible=false
                    Toast.makeText(this,"Login failed... Try again", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
