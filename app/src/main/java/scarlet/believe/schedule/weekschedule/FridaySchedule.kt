package scarlet.believe.schedule.weekschedule


import android.os.Bundle
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
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_friday_schedule.*
import kotlinx.android.synthetic.main.fragment_monday_schedule.*
import scarlet.believe.schedule.R

class FridaySchedule : Fragment() {

//    private lateinit var scheduleAdapter: ScheduleRecycleAdapter
    private lateinit var fridayrecycle : RecyclerView
    private lateinit var fridayloading : LottieAnimationView
    private lateinit var ref : DatabaseReference
    private lateinit var sublist : MutableList<String>
    private lateinit var timelist : MutableList<String>
    val colorlist = arrayListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friday_schedule, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        sublist = mutableListOf()
        timelist = mutableListOf()
        fridayloading = friday_loading
        fridayrecycle = friday_recycle
        fridayrecycle.setHasFixedSize(true)
        fridayrecycle.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s1_pinkish,null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s2_blue,null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s3_purple,null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s4_orange,null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s5_violet,null))
        colorlist.add(ResourcesCompat.getColor(resources, R.color.s6_skin,null))
        ref = FirebaseDatabase.getInstance().getReference("/schedule/friday")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    sublist.clear()
                    var i = 0
                    for (s in p0.children)
                    {
                        if(i%2==0){
                            val sub = s.getValue(String ::class.java)
                            timelist.add(sub!!)
                            i++
                        }
                        else{
                            val sub = s.getValue(String ::class.java)
                            sublist.add(sub!!)
                            i++
                        }
                    }
                    Log.i("value","$sublist[0]")
                    recyclerviewadder()
                }
            }

        })
        super.onActivityCreated(savedInstanceState)
    }

    private fun recyclerviewadder(){

        val scheduleAdapter = ScheduleRecycleAdapter(sublist,timelist,colorlist)
        fridayrecycle.adapter = scheduleAdapter
        fridayloading.isVisible = false
    }
}
