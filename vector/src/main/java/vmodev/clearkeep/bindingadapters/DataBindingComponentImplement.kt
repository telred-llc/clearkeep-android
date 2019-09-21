package vmodev.clearkeep.bindingadapters

import androidx.databinding.DataBindingComponent
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import javax.inject.Inject

class DataBindingComponentImplement @Inject constructor(private val bindingAdapters : BindingAdaptersImplement) : DataBindingComponent, IDataBindingComponent {

    override fun getImageViewBindingAdapters(): ImageViewBindingAdapters {
        return bindingAdapters;
    }

    override fun getTextViewBindingAdapters(): TextViewBindingAdapters {
        return bindingAdapters;
    }

    override fun getISwitchCompatViewBindingAdapters(): ISwitchCompatViewBindingAdapters {
        return bindingAdapters;
    }

    override fun getDataBindingComponent(): DataBindingComponent {
        return this;
    }

    override fun getCardViewBindingAdapters(): CardViewBindingAdapters {
        return bindingAdapters;
    }
}