package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.FragmentFeedBackBinding
import im.vector.databinding.FragmentListRoomBinding
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.OnSingleClickListener
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractFeedBackFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenFragmentViewModel
import javax.inject.Inject

class FeedBackFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var feedBackViewModelFactory: IViewModelFactory<AbstractFeedBackFragmentViewModel>;
    private lateinit var binding: FragmentFeedBackBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_back, container, false, dataBinding.getDataBindingComponent())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nestedScrollview.isNestedScrollingEnabled = false
        setEvent()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setEvent() {
        binding.btnSubmit.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                submitFeedback(binding.edtComment.text.toString(), binding.rating.numStars)
            }
        })
        binding.rating.setOnRatingBarChangeListener { ratingBar, ratingNumber, b ->
            binding.btnSubmit.isSelected = ratingNumber > 0
            binding.btnSubmit.isEnabled = ratingNumber > 0

        }
        binding.tvClearAll.setOnClickListener {
            clearAll()
        }
        binding.edtComment.setOnTouchListener { p0, p1 ->
            if (p0?.id == R.id.edtComment) {
                p0.parent?.requestDisallowInterceptTouchEvent(true)
                when (p1?.action?.and(MotionEvent.ACTION_MASK)) {
                    MotionEvent.ACTION_UP -> p0.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
    }

    private fun submitFeedback(content: String, stars: Int) {
        feedBackViewModelFactory.getViewModel().submitFeedback(content, stars).observe(viewLifecycleOwner, Observer {
            it?.data?.message?.let {
                if (it == "success") {
                    Toast.makeText(this.activity, getString(R.string.msg_feed_back_success), Toast.LENGTH_LONG).show()
                    clearAll()
                } else {
                    Toast.makeText(this.activity, getString(R.string.msg_feed_back_failed), Toast.LENGTH_LONG).show()
                }
            }
        })
        binding.btnSubmit.isSelected = false
    }

    private fun clearAll() {
        binding.edtComment.text?.clear()
        binding.rating.rating = 0F
    }

    override fun getFragment(): Fragment {
        return this
    }

}