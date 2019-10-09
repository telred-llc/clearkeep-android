package vmodev.clearkeep.activities

import android.content.Intent

import org.matrix.androidsdk.core.FileContentUtils
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.data.RoomMediaMessage
import java.io.File
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.SplashActivity
import im.vector.activity.VectorHomeActivity
import im.vector.adapters.VectorRoomsSelectionAdapter
import im.vector.databinding.ActivityShareFileBinding
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.RoomSummary
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.adapters.HomeScreenPagerAdapter
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShareFileFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IRoomShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class ShareFileActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    @field:Named(value = IShareFileFragmentFactory.DIRECT_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY)
    lateinit var directMessageFragmentFactory: IShareFileFragmentFactory;
    @Inject
    @field:Named(value = IShareFileFragmentFactory.ROOM_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY)
    lateinit var roomMessageFragmentFactory: IShareFileFragmentFactory;

    private lateinit var fragments: Array<IRoomShareFileFragment>;
    private lateinit var currentFragment: IRoomShareFileFragment;

    private val LOG_TAG = ShareFileActivity::class.java.simpleName
    private val SHARED_FOLDER = "VectorShared"
    private var session: MXSession? = null

    private lateinit var binding: ActivityShareFileBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_file, dataBinding.getDataBindingComponent());
//        setUpViewPage();
        initUiAndData()
        setEvent()
    }

    private fun setUpViewPage() {
        fragments = arrayOf(directMessageFragmentFactory.createNewInstance(), roomMessageFragmentFactory.createNewInstance());
        binding.viewPager.adapter = HomeScreenPagerAdapter(supportFragmentManager, arrayOf(fragments[0].getFragment(), fragments[1].getFragment()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        currentFragment = fragments[0];
        fragments.forEach {
            it.onClickItemtRoom().subscribe {
                val roomIntent = Intent(this, RoomActivity::class.java);
                roomIntent.putExtra(RoomActivity.EXTRA_MATRIX_ID, application.getUserId());
                roomIntent.putExtra(RoomActivity.EXTRA_ROOM_ID, it)
                roomIntent.putExtra(RoomActivity.EXTRA_ROOM_INTENT, intent);
                startActivity(roomIntent);
                finish();
            }
        }
    }

//    override fun getLayoutRes(): Int {
//        return R.layout.activity_share_file
//    }

    fun initUiAndData() {
        // retrieve the current intent
        val anIntent = intent

        if (null != anIntent) {
            val action = anIntent.action
            val type = anIntent.type

            Log.d(LOG_TAG, "onCreate : action $action type $type")

            // send files from external application
            // check the params
            if ((Intent.ACTION_SEND == action || Intent.ACTION_SEND_MULTIPLE == action) && type != null) {
                var hasCredentials = false
                var isLaunched = false

                try {
                    session = Matrix.getInstance(this)!!.defaultSession

                    if (null != session) {
                        hasCredentials = true
                        isLaunched = session!!.dataHandler.store.isReady
                    }
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "## onCreate() : failed " + e.message, e)
                }

                // go to the home screen if the application is launched
                if (hasCredentials) {
                    launchActivity(anIntent, isLaunched)
                } else {
                    Log.d(LOG_TAG, "onCreate : go to login screen")

                    // don't know what to do, go to the login screen
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                }
            } else {
                Log.d(LOG_TAG, "onCreate : unsupported action")

                setUpViewPage()
            }
        } else {
            Log.d(LOG_TAG, "onCreate : null intent")
            setUpViewPage()
        }

    }

    private fun setEvent() {
        binding.imgCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                onBackPressed()
            }

        })
    }

    /**
     * Extract the medias list, copy them into a tmp directory and provide them to the home activity
     *
     * @param intent        the intent
     * @param isAppLaunched true if the application is resumed
     */
    private fun launchActivity(intent: Intent, isAppLaunched: Boolean) {
        val sharedFolder = File(cacheDir, SHARED_FOLDER)

        /**
         * Clear the existing folder to reduce storage memory usage
         */
        if (sharedFolder.exists()) {
            FileContentUtils.deleteDirectory(sharedFolder)
        }

        sharedFolder.mkdir()

        val cachedFiles = ArrayList(RoomMediaMessage.listRoomMediaMessages(intent))

        if (null != cachedFiles) {
            for (sharedDataItem in cachedFiles) {
                sharedDataItem.saveMedia(this, sharedFolder)
            }
        }

        Log.d(LOG_TAG, "onCreate : launch home activity with the files list " + cachedFiles.size + " files")

        val activityIntent: Intent

        if (isAppLaunched) {
            setUpViewPage()
        } else {
            activityIntent = Intent(this, SplashActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(activityIntent)
        }
    }

//
//    private fun sendFilesTo() {
//        if (null == session || !session!!.isAlive || this.isFinishing) {
//            return
//        }
//
//        val mergedSummaries = ArrayList<RoomSummary>(session!!.dataHandler.store.summaries)
//
//        // keep only the joined room
//        var index = 0
//        while (index < mergedSummaries.size) {
//            val summary = mergedSummaries.get(index)
//            val room = session!!.dataHandler.getRoom(summary.roomId)
//
//            if (null == room || room.isInvited || room.isConferenceUserRoom) {
//                mergedSummaries.removeAt(index)
//                index--
//            }
//            index++
//        }
//
//        Collections.sort(mergedSummaries, Comparator<RoomSummary> { lhs, rhs ->
//            if (lhs == null || lhs.latestReceivedEvent == null) {
//                return@Comparator 1
//            } else if (rhs == null || rhs.latestReceivedEvent == null) {
//                return@Comparator -1
//            }
//
//            if (lhs.latestReceivedEvent.getOriginServerTs() > rhs.latestReceivedEvent.getOriginServerTs()) {
//                return@Comparator -1
//            } else if (lhs.latestReceivedEvent.getOriginServerTs() < rhs.latestReceivedEvent.getOriginServerTs()) {
//                return@Comparator 1
//            }
//            0
//        })
//        val adapter = VectorRoomsSelectionAdapter(this, R.layout.adapter_item_vector_recent_room, session)
//        adapter.addAll(mergedSummaries)
//        Toast.makeText(this, adapter.count.toString(), Toast.LENGTH_LONG).show()
//    }


    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}