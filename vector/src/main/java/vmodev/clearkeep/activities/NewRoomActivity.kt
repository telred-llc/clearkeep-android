package vmodev.clearkeep.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import im.vector.R
import im.vector.databinding.ActivityNewRoomBinding
import kotlinx.android.synthetic.main.item_view_toolbar.view.*
import kotlinx.android.synthetic.main.vector_preference_bing_rule.view.*
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.enums.TypeIconToolBar
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.ListRoomFragment

class NewRoomActivity : DataBindingDaggerActivity(), IActivity {

    private lateinit var binding: ActivityNewRoomBinding;
    private lateinit var navController: NavController;
    private var startWith: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startWith = intent.getIntExtra(START_WITH, 0);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_room, dataBinding.getDataBindingComponent());
        binding.toolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
        navController = Navigation.findNavController(this, R.id.container);
        when (startWith) {
            0 -> {
                navController.navigate(R.id.findAndCreateMewConversationFragment);
            }
            1 -> {
                navController.navigate(R.id.navigationNewRoom);
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar?.title = destination.label;
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.tvTitle.text = destination.label;
        }
    }



    fun setIconToolBar(typeIconToolBar: TypeIconToolBar) {
        if (typeIconToolBar == TypeIconToolBar.ICON_BACK) {
            binding.toolbar.imgBack.setImageDrawable(
                    ContextCompat.getDrawable(
                            applicationContext, // Context
                            R.drawable.ic_back // Drawable
                    )
            )
        } else if (typeIconToolBar == TypeIconToolBar.ICON_CLOSE) {
            binding.toolbar.imgBack.setImageDrawable(
                    ContextCompat.getDrawable(
                            applicationContext, // Context
                            R.drawable.ic_close_black_24dp // Drawable
                    ))
        } else {

        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val START_WITH = "START_WITH";
    }
}
