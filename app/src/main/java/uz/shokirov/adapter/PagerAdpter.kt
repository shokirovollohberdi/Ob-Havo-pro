package uz.shokirov.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.shokirov.models.WeatherModel
import uz.shokirov.obhavopro.ItemFragment

class PagerAdpter(var fragmentManager: FragmentManager, var list: ArrayList<WeatherModel>) :
    FragmentPagerAdapter(fragmentManager) {


    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return ItemFragment(list[position].lat!!, list[position].long!!)
    }
}