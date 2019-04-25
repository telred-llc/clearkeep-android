package vmodev.clearkeep.activities

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityFindAndCreateNewConversationBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FindAndCreateNewConversationActivity : DaggerAppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    lateinit var userViewModel: AbstractUserViewModel;

    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityFindAndCreateNewConversationBinding>(this, R.layout.activity_find_and_create_new_conversation, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar!!.setTitle(R.string.new_conversation);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractUserViewModel::class.java);
        binding.lifecycleOwner = this;

        val listUserAdapter = ListUserRecyclerViewAdapter(appExecutors, dataBindingComponent = dataBindingComponent, diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }
        }) { user -> }
        binding.recyclerViewUsers.adapter = listUserAdapter;
        userViewModel.getUsers().observe(this, Observer { t ->
            kotlin.run {
                listUserAdapter.submitList(t?.data);
            }
        });
        var disposable : Disposable?;
        RxTextView.textChanges(binding.editQuery).subscribe { t: CharSequence? ->
            kotlin.run {
                if (disposable?.isDisposed) {
                    disposable.dispose()
                }
                disposable = Observable.timer(100, TimeUnit.MILLISECONDS).subscribe();
            }
        };
    }
}
