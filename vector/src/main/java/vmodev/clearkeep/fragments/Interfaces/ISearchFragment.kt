package vmodev.clearkeep.fragments.Interfaces

import android.support.v4.app.Fragment

interface ISearchFragment : IFragment {
    fun selectedFragment(query : String) : ISearchFragment;
    fun unSelectedFragment();
}