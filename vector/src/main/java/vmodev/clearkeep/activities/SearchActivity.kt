package vmodev.clearkeep.activities

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.SearchView
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivitySearchBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.adapters.SearchViewPagerAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
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

    private val publishSubjectSearchView : PublishSubject<String> = PublishSubject.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search, dataBindingComponent);
        binding.textViewCancel.setOnClickListener { v -> finish() }
        val pagerAdapter = SearchViewPagerAdapter(supportFragmentManager, arrayOf(SearchRoomsFragment.newInstance("", ""), SearchMessagesFragment.newInstance("", ""),
                SearchPeopleFragment.newInstance("", ""), SearchFilesFragment.newInstance("", "")));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.adapter = pagerAdapter;

        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.isIconified = false;

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { s -> publishSubjectSearchView.onNext(s) };
                return false;
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish();
    }

    override fun onDestroy() {
        super.onDestroy()
        publishSubjectSearchView.onComplete();
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun getSearchViewTextChange(): Observable<String> {
        return publishSubjectSearchView;
    }
}
