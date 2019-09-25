package vmodev.clearkeep.activities

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.MXCActionBarActivity
import im.vector.ui.themes.ActivityOtherThemes
import org.matrix.androidsdk.core.Log
import java.util.*

abstract class BaseSearchActivity : MXCActionBarActivity() {

    private var mActionBar: ActionBar? = null
    protected lateinit var mPatternToSearchEditText: SearchView
    protected lateinit var textViewCancel: TextView;

    private var mMicroMenuItem: MenuItem? = null
    private var mClearEditTextMenuItem: MenuItem? = null

    interface IVectorSearchActivity {
        fun refreshSearch()
    }

    override fun getOtherThemes(): ActivityOtherThemes {
        return ActivityOtherThemes.Search
    }

    @CallSuper
    override fun initUiAndData() {
        configureToolbar()

        mActionBar = supportActionBar
        val actionBarView = customizeActionBar()

        // add the search logic based on the text search input listener
        mPatternToSearchEditText = actionBarView.findViewById(R.id.room_action_bar_edit_text)
        textViewCancel = actionBarView.findViewById(R.id.text_view_cancel)
        actionBarView.postDelayed({
            mPatternToSearchEditText.requestFocus()
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.showSoftInput(mPatternToSearchEditText, 0)
        }, 100)

//        mPatternToSearchEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: android.text.Editable) {
//                refreshMenuEntries()
//                val fPattern = mPatternToSearchEditText.text.toString()
//
//                val timer = Timer()
//
//                try {
//                    // wait a little delay before refreshing the results.
//                    // it avoid UI lags when the user is typing.
//                    timer.schedule(object : TimerTask() {
//                        override fun run() {
//                            runOnUiThread {
//                                if (TextUtils.equals(mPatternToSearchEditText.text.toString(), fPattern)) {
//                                    runOnUiThread { onPatternUpdate(true) }
//                                }
//                            }
//                        }
//                    }, 100)
//                } catch (throwable: Throwable) {
//                    Log.e(LOG_TAG, "## failed to start the timer " + throwable.message, throwable)
//
//                    runOnUiThread {
//                        if (TextUtils.equals(mPatternToSearchEditText.text.toString(), fPattern)) {
//                            runOnUiThread { onPatternUpdate(true) }
//                        }
//                    }
//                }
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//        })
        mPatternToSearchEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                refreshMenuEntries()
                val fPattern = p0;

                val timer = Timer()
                try {
                    // wait a little delay before refreshing the results.
                    // it avoid UI lags when the user is typing.
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                if (TextUtils.equals(p0, fPattern)) {
                                    runOnUiThread { onPatternUpdate(true) }
                                }
                            }
                        }
                    }, 100)
                } catch (throwable: Throwable) {
                    Log.e(LOG_TAG, "## failed to start the timer " + throwable.message, throwable)

                    runOnUiThread {
                        if (TextUtils.equals(p0, fPattern)) {
                            runOnUiThread { onPatternUpdate(true) }
                        }
                    }
                }
                return false;
            }
        })
//        mPatternToSearchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH
//                    // hardware keyboard : detect the keydown event
//                    || null != event && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
//                onPatternUpdate(false)
//                return@OnEditorActionListener true
//            }
//            false
//        })
//
//        // required to avoid having the crash
//        // focus search returned a view that wasn't able to take focus!
//        mPatternToSearchEditText.setOnClickListener { }
        textViewCancel.setOnClickListener { v -> finish(); }
    }

    override fun onPause() {
        super.onPause()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mPatternToSearchEditText.applicationWindowToken, 0)
    }

    override fun onResume() {
        super.onResume()

        runOnUiThread { onPatternUpdate(false) }
    }

    /**
     * The search pattern has been updated.
     *
     * @param isTypingUpdate true when the pattern has been updated while typing.
     */
    protected open fun onPatternUpdate(isTypingUpdate: Boolean) {
        // do something here
    }

    /**
     * Add a custom action bar with a view
     *
     * @return the action bar inflated view
     */
    private fun customizeActionBar(): View {
        mActionBar!!.setDisplayShowCustomEnabled(true)
        mActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM or ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_HOME_AS_UP

        // add a custom action bar view containing an EditText to input the search text
        val layout = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        val actionBarLayout = layoutInflater.inflate(R.layout.action_bar_edit_tex, null)
        mActionBar!!.setDisplayShowHomeEnabled(false);
        mActionBar!!.setDisplayHomeAsUpEnabled(false);
        mActionBar!!.setCustomView(actionBarLayout, layout)
//
        return actionBarLayout
    }

//    override fun getMenuRes(): Int {
//        return R.menu.vector_searches
//    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // the application is in a weird state
        if (CommonActivityUtils.shouldRestartApp(this)) {
            return false
        }

        mMicroMenuItem = menu.findItem(R.id.ic_action_speak_to_search)
        mClearEditTextMenuItem = menu.findItem(R.id.ic_action_clear_search)

        refreshMenuEntries()

        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.ic_action_speak_to_search -> {
//                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//                startActivityForResult(intent, SPEECH_REQUEST_CODE)
//                return true
//            }
//            R.id.ic_action_clear_search -> {
//                mPatternToSearchEditText.setQuery("", false)
//                onPatternUpdate(false)
//                return true
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    /**
     * Handle the results from the voice recognition activity.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//
//            // one matched items
//            if (matches.size == 1) {
//                // use it
//                mPatternToSearchEditText.setQuery(matches[0], false)
//                onPatternUpdate(false)
//            } else if (matches.size > 1) {
//                // if they are several matches, let the user chooses the right one.
//                val mes = matches.toTypedArray()
//
//                AlertDialog.Builder(this)
//                        .setItems(mes) { dialog, item ->
//                            mPatternToSearchEditText.setQuery(matches[item], false)
//                            runOnUiThread { onPatternUpdate(false) }
//                        }
//                        .show()
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data)
    }

//    /**
//     * @return true of the device supports speech recognizer.
//     */
//    private fun supportSpeechRecognizer(): Boolean {
//        val pm = packageManager
//        val activities = pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
//
//        return null != activities && activities.size > 0
//    }

    /**
     * Refresh the menu entries
     */
    private fun refreshMenuEntries() {
        val hasText = !TextUtils.isEmpty(mPatternToSearchEditText.query.toString())
//
//        if (null != mMicroMenuItem) {
//            mMicroMenuItem!!.isVisible = !hasText && supportSpeechRecognizer()
//        }

        if (null != mClearEditTextMenuItem) {
            mClearEditTextMenuItem!!.isVisible = hasText
        }
    }

    companion object {
        private val LOG_TAG = BaseSearchActivity::class.java.simpleName

//        private val SPEECH_REQUEST_CODE = 1234
    }
}
