package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.databinding.FragmentSettingDoNotDisturdBinding
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.DateUtil
import vmodev.clearkeep.ultis.Debug
import vmodev.clearkeep.ultis.OnSingleClickListener
import vmodev.clearkeep.ultis.SharedPreferencesUtils
import java.text.SimpleDateFormat
import java.util.*

class SettingDoNotDisturbFragment : DataBindingDaggerFragment(), IFragment {
    private lateinit var binding: FragmentSettingDoNotDisturdBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_do_not_disturd, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
    }

    private fun setEvent() {
        binding.rlTimeFrom.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showTimePicker(binding.tvFromTime, true)
            }
        })
        binding.rlTimeTo.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showTimePicker(binding.tvToTime, false)
            }
        })
        binding.btnClick.setOnClickListener {
            if (DateUtil.isNotificationActive(activity!!)) {
                Toast.makeText(activity!!, "oke rồi!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showTimePicker(textView: TextView, isTimeForm: Boolean) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val time = SimpleDateFormat("HH:mm").format(cal.time)

            textView.text = time
            if (isTimeForm) {
                SharedPreferencesUtils.putString(activity!!, KEY_SHARE_PREFERENCES_TIME_FROM, time)
            } else {
                SharedPreferencesUtils.putString(activity!!, KEY_SHARE_PREFERENCES_TIME_TO, time)
            }
        }
        TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }


    override fun getFragment(): Fragment {
        return this
    }

    companion object {
        const val KEY_SHARE_PREFERENCES_TIME_FROM = "KEY_SHARE_PREFERENCES_TIME_FROM"
        const val KEY_SHARE_PREFERENCES_TIME_TO = "KEY_SHARE_PREFERENCES_TIME_TO"
    }

}