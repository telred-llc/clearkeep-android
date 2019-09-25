package vmodev.clearkeep.fragments.Interfaces

interface ISearchFragment : IFragment {
    fun selectedFragment(query : String) : ISearchFragment;
    fun unSelectedFragment();
}