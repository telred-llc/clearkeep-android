package vmodev.clearkeep.activities

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.util.Log
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivitySearchBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.adapters.SearchViewPagerAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchMessagesFragment
import vmodev.clearkeep.fragments.SearchPeopleFragment
import vmodev.clearkeep.fragments.SearchRoomsFragment
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity(), SearchRoomsFragment.OnFragmentInteractionListener
        , SearchFilesFragment.OnFragmentInteractionListener
        , SearchMessagesFragment.OnFragmentInteractionListener
        , SearchPeopleFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);

    private val publishSubjectSearchView: PublishSubject<String> = PublishSubject.create();

    private var disposable: Disposable? = null;

    private val arraySearchFragment: Array<ISearchFragment> = arrayOf(SearchRoomsFragment.newInstance("", ""), SearchMessagesFragment.newInstance("", ""),
            SearchPeopleFragment.newInstance("", ""), SearchFilesFragment.newInstance("", ""));

    private var currentSearchFragment: ISearchFragment? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search, dataBindingComponent);
        binding.textViewCancel.setOnClickListener { v -> finish() }
        val pagerAdapter = SearchViewPagerAdapter(supportFragmentManager, arraySearchFragment);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.adapter = pagerAdapter;

        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.isIconified = false;

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { s -> publishSubjectSearchView.onNext(s) };
                return false;
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }
        });

        disposable = RxViewPager.pageSelections(binding.viewPager).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: Int? ->
                    t?.let { i ->
                        currentSearchFragment?.unSelectedFragment();
                        currentSearchFragment = arraySearchFragment[i].selectedFragment(binding.searchView.query.toString());
                    };
                }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish();
    }

    override fun onDestroy() {
        super.onDestroy()
        publishSubjectSearchView.onComplete();
        disposable?.dispose();
    }

    override fun getSearchViewTextChange(): Observable<String> {
        return publishSubjectSearchView;
    }
}
