package vmodev.clearkeep.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.FragmentActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Inject

class ActivityDataBindingComponent @Inject constructor(val activity: FragmentActivity) : DataBindingComponent {
    private val activityBindingAdapters = ActivityBindingAdapters(activity);
    override fun getImageViewBindingAdapters(): ImageViewBindingAdapters {
        return activityBindingAdapters;
    }

    override fun getTextViewBindingAdapters(): TextViewBindingAdapters {
        return activityBindingAdapters;
    }

    override fun getISwitchCompatViewBindingAdapters(): ISwitchCompatViewBindingAdapters {
        return activityBindingAdapters;
    }
}