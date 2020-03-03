package scarlet.believe.schedule.homepages


import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_subject_.*
import scarlet.believe.schedule.authentication.RegistrationFirebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import scarlet.believe.schedule.R


class Subject_Fragment : Fragment() {

    private var attendencelist : MutableList<Int> = mutableListOf()
    private var subjectlist : MutableList<String> = mutableListOf()
    private var classno = 0
    val colorlist = arrayListOf<Int>()
    private var auth = FirebaseAuth.getInstance()
    private var uid = FirebaseAuth.getInstance().uid
    private lateinit var ref : DatabaseReference
    private lateinit var ref1 : DatabaseReference
    private lateinit var recycleview : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject_, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val progress = progress_loading
        recycleview = attend_recycler
        recycleview.setHasFixedSize(true)
        recycleview.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        colorlist.add(ResourcesCompat.getColor(resources,
            R.color.s2_blue,null))
        colorlist.add(ResourcesCompat.getColor(resources,
            R.color.s3_purple,null))
        colorlist.add(ResourcesCompat.getColor(resources,
            R.color.s4_orange,null))
        colorlist.add(ResourcesCompat.getColor(resources,
            R.color.s5_violet,null))
        colorlist.add(ResourcesCompat.getColor(resources,
            R.color.s6_skin,null))
        colorlist.add(ResourcesCompat.getColor(resources,
            R.color.s1_pinkish,null))



        sign_outBtn.setOnClickListener {

            auth.signOut()
            val intent = Intent(this.context, RegistrationFirebase::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        //setting up firebase to fetch attendence
        ref = FirebaseDatabase.getInstance().getReference("/users/$uid/subs")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (s in p0.children)
                    {
                        val sub = s.getValue(String ::class.java)
                        subjectlist.add(sub!!)
                    }
                }
                classno = subjectlist.size
            }
        })
        ref = FirebaseDatabase.getInstance().getReference("/users/$uid/atten")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (s in p0.children)
                    {
                        val sub = s.getValue(Int ::class.java)
                        attendencelist.add(sub!!)
                    }
                }
                Log.i("Before","$attendencelist")
                val attensize = attendencelist.size
                for(i in  attensize-1 downTo classno){
                    attendencelist.removeAt(i)
                }
                Log.i("after","$attendencelist , $classno, $attensize")
                setadapterforsubs()
                progress.isVisible=false
            }
        })

        //setting link for notes of each sub
        getnoteslink()

        //setting link for marks of each subject
        getmarks()

    }

    private fun setadapterforsubs(){

        val scheduleAdapter = SubjectAttendenceAdapter(
            attendencelist,
            subjectlist,
            colorlist
        )
        recycleview.adapter = scheduleAdapter

    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun getnoteslink(){

        all_notes.setText(Html.fromHtml("<a href=\"https://drive.google.com/open?id=1NzETKQyAIdA99_6E6Lt5z7o3hBirSO2J\">Notes</a>"))
        all_notes.setMovementMethod(LinkMovementMethod.getInstance())
        all_notes.setTextColor(resources.getColor(R.color.white))

    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getmarks(){

        all_marks.setText(Html.fromHtml("<a href=\"https://drive.google.com/open?id=1wB0YmB21J7IuVOrarAxRLe4GJKeWTjmP\">Marks</a>"))
        all_marks.setMovementMethod(LinkMovementMethod.getInstance())
    }


}
