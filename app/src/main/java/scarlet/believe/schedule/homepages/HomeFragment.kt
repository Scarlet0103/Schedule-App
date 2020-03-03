package scarlet.believe.schedule.homepages


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import scarlet.believe.schedule.weekschedule.*
import java.util.*
import androidx.fragment.app.FragmentActivity
import scarlet.believe.schedule.R


class HomeFragment : Fragment() {

    private lateinit var myContext : FragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myContext = activity as FragmentActivity
        val fragManager = myContext.getSupportFragmentManager()

        //viewpager setup
        val  pager = pageview_main
        val pageadapter : ViewPagerAdapter =
            ViewPagerAdapter(fragManager)

        //Adding Fragments
        pageadapter.AddFragment(MondaySchedule(),"Mon")
        pageadapter.AddFragment(TuesdaySchedule(),"Tue")
        pageadapter.AddFragment(WednesdaySchedule(),"Wed")
        pageadapter.AddFragment(ThursdaySchedule(),"Thu")
        pageadapter.AddFragment(FridaySchedule(),"Fri")

        //Setting pageadapter to viewpager
        pager.adapter = pageadapter
        week_tab.setupWithViewPager(pager)
        val tabLayout = week_tab
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        val dayint = setday(day)
        val tab = tabLayout.getTabAt(dayint)
        tab!!.select()
    }

    private fun setday(day : Int) : Int {
        when (day) {
            Calendar.MONDAY -> return 0
            Calendar.TUESDAY -> return 1
            Calendar.WEDNESDAY -> return 2
            Calendar.THURSDAY -> return 3
            Calendar.FRIDAY -> return 4
        }
        return 0
    }

}
