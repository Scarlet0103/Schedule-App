package scarlet.believe.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_attendance_notification.*
import java.util.*

class AttendanceNotification : AppCompatActivity() {

    private var todaysub: MutableList<Sub> = mutableListOf()
    private var mysubs: MutableList<String> = mutableListOf()
    private var myattend: MutableList<Int> = mutableListOf()
    private lateinit var ref1: DatabaseReference
    private lateinit var ref2: DatabaseReference
    private lateinit var ref: DatabaseReference
    private lateinit var subAdapter: SubjectsAdapter
    val colorlist = arrayListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_notification)


        var uid = FirebaseAuth.getInstance().uid
        infotext.isVisible = false
        infoani.setOnClickListener {
            infotext.isVisible = !infotext.isVisible
        }

        //setting color for each sub
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s1_pinkish, null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s2_blue, null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s3_purple, null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s4_orange, null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s5_violet, null))


        //setting subject adapter
        notification_recyclerview.setHasFixedSize(true)
        notification_recyclerview.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)


        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val todayweek = setalllist(day)
        if (todayweek == "NO") {

            var sub = Sub("No Class Today",0)
            todaysub.add(sub)
            subAdapter = SubjectsAdapter(todaysub,colorlist)
            notification_recyclerview.adapter = subAdapter
            card_view_notification.isVisible = false
        }
        else
        {

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(notification_recyclerview)
        ref1 = FirebaseDatabase.getInstance().getReference("/schedule/$todayweek")
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (s in p0.children) {
                        var sub = s.getValue(String::class.java)
                        var addsub = Sub(sub!!, 0)
                        todaysub.add(addsub)
                    }
                    Log.i("value", "$todaysub[0]")
                }
            }
        })


        ref2 = FirebaseDatabase.getInstance().getReference("/users/$uid/atten")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    myattend.clear()
                    for (s in p0.children) {
                        val sub = s.getValue(Int::class.java)
                        myattend.add(sub!!)
                    }
                    Log.i("value", "$myattend[0]")
                }
            }
        })


        ref = FirebaseDatabase.getInstance().getReference("/users/$uid/subs")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    mysubs.clear()
                    for (s in p0.children) {
                        val sub = s.getValue(String::class.java)
                        mysubs.add(sub!!)
                    }
                    Log.i("value", "$mysubs[0]")
                }
                for (i in todaysub.size - 1 downTo 0) {

                    var flag = 0
                    for (j in 0 until mysubs.size) {
                        if (todaysub[i].sub == mysubs[j]) {
                            flag = 1
                            todaysub[i].pos = j
                        }
                    }
                    if (flag == 0) {
                        todaysub.removeAt(i)
                    }
                }
                subAdapter = SubjectsAdapter(todaysub, colorlist)
                notification_recyclerview.adapter = subAdapter
                card_view_notification.isVisible = false
            }
        })
    }
}


    var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or  ItemTouchHelper.LEFT ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(ItemTouchHelper.RIGHT==direction){

                    var spos = todaysub[viewHolder.adapterPosition].pos
                    myattend[spos]++
                }
                todaysub.removeAt(viewHolder.adapterPosition)
                subAdapter.notifyDataSetChanged()
                if(todaysub.size==0){
                    closeapp()
                }
            }
        }


    private fun closeapp(){

        ref2.setValue(myattend)
            .addOnSuccessListener {
                Log.d("attendence list saved","$myattend")
                finish()
            }
    }

    private fun setalllist(day: Int) : String {
        when (day) {
            Calendar.MONDAY -> {
                return "monday"
            }
            Calendar.TUESDAY -> {
                return "tuesday"
            }
            Calendar.WEDNESDAY -> {
                return "wednesday"
            }
            Calendar.THURSDAY -> {
                return "thursday"
            }
            Calendar.FRIDAY -> {
                return "friday"
            }
        }
        return "NO"
    }
}

data class Sub(var sub : String,var pos : Int)
