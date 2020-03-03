package scarlet.believe.schedule.authentication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_registration_firebase.*
import scarlet.believe.schedule.homepages.HomePage
import scarlet.believe.schedule.R

class RegistrationFirebase : AppCompatActivity() {


    private lateinit var dialogBtn : Button
    private var flag : Int = 0
    private var classno : Int? = 0
    private var classno1 : Int? = 0
    private lateinit var sublist : MutableList<String>
    private lateinit var subpos : MutableList<Int>
    private lateinit var ref : DatabaseReference
    private lateinit var ref1 : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_firebase)

        card_view_reg.isVisible = true
        dialogBtn = findViewById<Button>(R.id.listBtn)
        sublist = mutableListOf()
        var reglist : MutableList<String>? = mutableListOf()
        subpos = mutableListOf()
        var sublist1 = arrayOf("1", "2", "3", "4", "5")
        var checkedItems:BooleanArray = booleanArrayOf()

        ref = FirebaseDatabase.getInstance().getReference("/subjects/number")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    var num = p0.child("n1").getValue(Int ::class.java)
                    classno = num
                    num = p0.child("n2").getValue(Int ::class.java)
                    classno1 = num
                }
                val aray : Array<String> = Array(classno1!!, { i -> "one"})
                sublist1 = aray
            }
        })

        ref1 = FirebaseDatabase.getInstance().getReference("/subjects/names")
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    var i = 0
                    for (s in p0.children)
                    {
                            val sub = s.getValue(String ::class.java)
                            sublist.add(sub!!)
                    }
                    Log.i("scarlet","$sublist[0]")
                    for(i in 0 until sublist.size){
                        //sublist1[i] = sublist[i]
                        sublist1.set(i,sublist[i])
                    }
                    Log.i("scarlet2","$sublist1[1]")
                    checkedItems = BooleanArray(sublist1.size)
                    //setdialogbox(sublist1,checkedItems)

                }
                card_view_reg.isVisible = false
            }

        })

        var click : Int = 0
        dialogBtn.setOnClickListener {
            flag = 1
            reglist?.clear()
            val mBuilder = AlertDialog.Builder(this,R.style.DialogTheme)
            mBuilder.setTitle("Select Subjects")
            mBuilder.setMultiChoiceItems(sublist1, checkedItems,
                DialogInterface.OnMultiChoiceClickListener { dialogInterface, position, isChecked ->
                    if (isChecked) {
                        subpos.add(position)
                        click++
                    } else {
                        subpos.remove((Integer.valueOf(position)))
                        click--
                    }
                    checkedItems[position] = isChecked
                    if(click > classno!!){
                        checkedItems[position] = false
                        click--
                        Toast.makeText(this,"You Selected Too many",Toast.LENGTH_SHORT).show()
                        (dialogInterface as AlertDialog).getListView().setItemChecked(position, false)
                    }
                })

            mBuilder.setCancelable(false)
            mBuilder.setNeutralButton("Done",
                    DialogInterface.OnClickListener { dialogInterface, which ->

                        var count = 0
                        for(i in 0 until checkedItems.size){
                            if(checkedItems[i]){
                                count++
                            }
                        }
                        if(count==classno){
                            for (i in 0 until subpos.size) {
                                reglist?.add(sublist1[subpos[i]]!!)
                            }
                            Log.i("Reg List","$reglist")
                            dialogInterface.dismiss()
                        }else{
                            Toast.makeText(this,"Select $classno Subjects for this semester",Toast.LENGTH_SHORT).show()
                            for(i in 0 until checkedItems.size){
                                checkedItems[i] = false
                                subpos.clear()
                            }
                            click = 0
                            flag = 0
                            reglist?.clear()
                            dialogInterface.dismiss()
                        }
                    })
            mBuilder.setNegativeButton("Cancel",DialogInterface.OnClickListener{ dialogInterface, i ->

                for(i in 0 until checkedItems.size){
                    checkedItems[i] = false
                    subpos.clear()
                }
                click = 0
                reglist?.clear()
                flag = 0
                dialogInterface.dismiss()
            })

            val mDialog = mBuilder.create()
            mDialog.show()

        }




        login_txt.setOnClickListener {
            val intent = Intent(this, LoginFirebase::class.java)
            startActivity(intent)
        }

        //registration
        register_btn.setOnClickListener {
            val usernametxt = username_txt_reg.text.toString()
            val emailtxt = email_txt_reg.text.toString()
            val passwordtxt = password_txt_reg.text.toString()
            if(usernametxt.isEmpty() || emailtxt.isEmpty() || passwordtxt.isEmpty() || flag==0){
                Toast.makeText(this,"Please enter username/email/password/subjects",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            card_view_reg.isVisible = true
            //firebase auth with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailtxt,passwordtxt)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Main","Success registration: ${it.result?.user?.uid}")
                    saveusertofirebase(reglist)
                }
                .addOnFailureListener {
                    Log.d("Main","Failed to create user : ${it.message}")
                    card_view_reg.isVisible = false
                    Toast.makeText(this,"Registration failed... Try again",Toast.LENGTH_SHORT).show()
                }
        }
    }





    private fun saveusertofirebase(reglist : MutableList<String>?){
        val uid = FirebaseAuth.getInstance().uid
        val uname = username_txt_reg.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/subs")
        val ref2 = FirebaseDatabase.getInstance().getReference("/users/$uid/atten")
        val ref3 = FirebaseDatabase.getInstance().getReference("/users/$uid/data")
        val user : User = User(uid,uname)
        val attensubs : AttenSubs = AttenSubs()
        ref.setValue(reglist)
            .addOnSuccessListener {
                Log.d("reg list saved","user saved to database")
            }
        ref3.setValue(user)
            .addOnSuccessListener {
                Log.d("reg list saved","user saved to database")
            }
        ref2.setValue(attensubs)
            .addOnSuccessListener {
                Log.d("user saved","user saved to database")
                val intent = Intent(this, HomePage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}

class AttenSubs(val s1 : Int = 0,val s2 : Int = 0,val s3 : Int = 0,val s4: Int = 0,val s5 : Int = 0,val s6 : Int = 0,val s7 : Int = 0,val s8 : Int = 0)
class User(val uid : String?,val uname : String)

