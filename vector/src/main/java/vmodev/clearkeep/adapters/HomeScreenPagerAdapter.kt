package vmodev.clearkeep.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.RoomFragment

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