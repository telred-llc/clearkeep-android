package vmodev.clearkeep.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment

class FragmentDataBindingComponent constructor(fragment: Fragment) : DataBindingComponent {
    private val fragmentBindingAdapters: FragmentBindingAdapters = FragmentBindingAdapters(fragment);
    override fun getImageViewBindingAdapters(): ImageViewBindingAdapters {
        return fragmentBindingAdapters;
    }

    override fun getTextViewBindingAdapters(): TextViewBindingAdapters {
        return fragmentBindingAdapters;
    }
}