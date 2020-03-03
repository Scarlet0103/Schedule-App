package scarlet.believe.schedule.homepages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapterMain (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentlist : MutableList<Fragment> = mutableListOf()
    private var fragmenttitlelist : MutableList<String> = mutableListOf()
    override fun getItem(position: Int): Fragment {
        return fragmentlist[position]
    }

    override fun getCount(): Int {
        return fragmenttitlelist.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmenttitlelist[position]
    }

    public fun AddFragment(fragment : Fragment, title : String){
        fragmentlist.add(fragment)
        fragmenttitlelist.add(title)
    }

}