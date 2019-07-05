package vmodev.clearkeep.dialogfragments

import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import im.vector.R
import im.vector.databinding.DialogFragmentReceivedShareFileBinding
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.HomeScreenPagerAdapter
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.fragments.RoomFragment
import javax.inject.Inject
import javax.inject.Named

class ReceivedShareFileDialogFragment : DataBindingDaggerDialogFragment() {
    private lateinit var binding: DialogFragmentReceivedShareFileBinding;
    @Inject
    @field:Named(value = IShowListRoomFragmentFactory.DIRECT_MESSAGE_FRAGMENT_FACTORY)
    lateinit var directMessageFragmentFactory: IShowListRoomFragmentFactory;
    @Inject
    @field:Named(value = IShowListRoomFragmentFactory.ROOM_MESSAGE_FRAGMENT_FACTORY)
    lateinit var roomMessageFragmentFactory: IShowListRoomFragmentFactory;

    private lateinit var fragments: Array<ISearchRoomFragment>;
    private lateinit var currentFragment: ISearchRoomFragment;
    private lateinit var userId: String;
    private lateinit var intentData: Intent;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragments = arrayOf(directMessageFragmentFactory.createNewInstance(), roomMessageFragmentFactory.createNewInstance());
        arguments?.let {
            userId = it.getString(USER_ID);
            intentData = it.getParcelable(INTENT_DATA);
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_received_share_file, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPage();
        binding.imageViewClose.setOnClickListener {
            this.dismiss();
        }
        RxTabLayout.selections(binding.tabLayout).subscribe {
            currentFragment = fragments[it.position]
        }
        RxTextView.textChanges(binding.editTextQuery).subscribe {
            currentFragment.setQuery(it.toString());
        }
        fragments.forEach {
            it.onClickItemtRoom().subscribe {
                val roomIntent = Intent(this.context, RoomActivity::class.java);
                roomIntent.putExtra(RoomActivity.EXTRA_MATRIX_ID, userId);
                roomIntent.putExtra(RoomActivity.EXTRA_ROOM_ID, it)
                roomIntent.putExtra(RoomActivity.EXTRA_ROOM_INTENT, intentData);
                startActivity(roomIntent);
                this.dismiss();
            }
        }
    }

    private fun setUpViewPage() {
        binding.viewPager.adapter = HomeScreenPagerAdapter(childFragmentManager, arrayOf(fragments[0].getFragment(), fragments[1].getFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        currentFragment = fragments[0];
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String, intentData: Intent) = ReceivedShareFileDialogFragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId);
                putParcelable(INTENT_DATA, intentData);
            }
        }

        const val USER_ID = "USER_ID";
        const val INTENT_DATA = "INTENT_DATA";
    }
}