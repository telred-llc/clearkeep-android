package vmodev.clearkeep.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import im.vector.databinding.FragmentRoomBinding
import androidx.lifecycle.Observer
import im.vector.databinding.FragmentRoomShareFileBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IRoomShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomShareFileFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomShareFileFragment : DataBindingDaggerFragment(), IRoomShareFileFragment {

    companion object{
        @JvmStatic
        fun newInstance() =
                RoomShareFileFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    private lateinit var binding: FragmentRoomShareFileBinding;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomShareFileFragmentViewModel>;

    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.SHARE_FILE)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    private val onClickItem: PublishSubject<String> = PublishSubject.create();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_share_file, container, false, dataBinding.getDataBindingComponent());
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()

    }


    override fun setQuery(query: String) {
        if (!::binding.isInitialized || !::viewModelFactory.isInitialized)
            return;
        if (query.isEmpty()) {
            binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
            viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
        } else {
            binding.rooms = viewModelFactory.getViewModel().getSearchResult();
            viewModelFactory.getViewModel().setQueryForSearch("%" + query + "%");
        }
    }

    override fun onClickItemtRoom(): Observable<String> {
        return onClickItem
    }

    override fun getFragment(): Fragment {
        return this;
    }

    private fun initData() {
        binding.lifecycleOwner = viewLifecycleOwner;
        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        binding.rvListRoom.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.rvListRoom.adapter = listRoomAdapter.getAdapter();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer {
            listRoomAdapter.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().setListType(arrayOf(2, 130))
    }

    override fun onDestroy() {
        super.onDestroy()
        onClickItem.onComplete();
    }

}