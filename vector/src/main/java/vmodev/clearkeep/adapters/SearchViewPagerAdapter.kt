package vmodev.clearkeep.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class SearchViewPagerAdapter(fm: FragmentManager?, fragments: Array<Fragment>) : FragmentStatePagerAdapter(fm) {
    private val fragments = fragments;
    private val titles = arrayOf("Rooms", "Messages", "People", "Files");

    override fun getItem(p0: Int): Fragment {
        return fragments[p0];
    }

    override fun getCount(): Int {
        return fragments.size;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position];
    }
}