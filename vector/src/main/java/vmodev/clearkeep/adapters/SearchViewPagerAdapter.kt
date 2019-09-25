package vmodev.clearkeep.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment

class SearchViewPagerAdapter(fm: FragmentManager?, fragments: Array<ISearchFragment>) : FragmentPagerAdapter(fm!!, fragments.size) {
    private val fragments = fragments;
    private val titles = arrayOf("Rooms", "Messages", "People", "Files");

    override fun getItem(p0: Int): Fragment {
        return fragments[p0].getFragment();
    }

    override fun getCount(): Int {
        return fragments.size;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position];
    }
}