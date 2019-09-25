package vmodev.clearkeep.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HomeScreenPagerAdapter(fragmentManager: FragmentManager, fragments : Array<Fragment>) : FragmentStatePagerAdapter(fragmentManager) {

    private var fragments: Array<Fragment> = fragments;
    private var titles : Array<String> = arrayOf("Direct Message","Room");

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