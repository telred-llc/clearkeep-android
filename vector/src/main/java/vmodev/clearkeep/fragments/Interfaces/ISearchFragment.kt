package vmodev.clearkeep.fragments.Interfaces

import android.support.v4.app.Fragment

interface ISearchFragment {
    fun selectedFragment(query : String) : ISearchFragment;
    fun unSelectedFragment();
    fun getFragment() : Fragment;
}