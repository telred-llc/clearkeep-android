package vmodev.clearkeep.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import im.vector.R
import im.vector.adapters.model.Header
import org.zakariya.stickyheaders.SectioningAdapter
import vmodev.clearkeep.viewmodelobjects.RoomListUser

class ListRoomWithHeaderAdapter constructor(private val headers: List<Header>, private val listData: List<List<RoomListUser>>) : SectioningAdapter() {

    class HeaderViewHolder(itemView: View?) : SectioningAdapter.HeaderViewHolder(itemView) {
        private var headerName: TextView?;
        private var number: TextView?;
        private var imgIcon : ImageView?

        init {
            headerName = itemView?.findViewById(R.id.txtFavourites);
            number = itemView?.findViewById(R.id.txtNumber);
            imgIcon = itemView?.findViewById(R.id.icon_favourites);

        }

        public fun getHeaderName(): TextView? {
            return headerName;
        }

        public fun getNumber(): TextView? {
            return number;
        }

        public fun getImgIcon(): ImageView? {
            return imgIcon;
        }
    }

    class ItemViewHolder(itemView: View?) : SectioningAdapter.ItemViewHolder(itemView) {
        private var textView: TextView?;

        init {
            textView = itemView?.findViewById(R.id.text_view_name);
        }

        public fun getTextView(): TextView? {
            return textView;
        }
    }

    class FooterViewHolder(itemView: View?) : SectioningAdapter.FooterViewHolder(itemView) {
        private var headerName: TextView?;
        private var number: TextView?;
        private var imgIcon : ImageView?
        init {
            headerName = itemView?.findViewById(R.id.txtFavourites);
            number = itemView?.findViewById(R.id.txtNumber);
            imgIcon = itemView?.findViewById(R.id.icon_favourites);

        }

        public fun getHeaderName(): TextView? {
            return headerName;
        }

        public fun getNumber(): TextView? {
            return number;
        }

        public fun getImgIcon(): ImageView? {
            return imgIcon;
        }
    }

    init {

    }

    override fun getNumberOfSections(): Int {
        return listData.size;
    }

    override fun getNumberOfItemsInSection(sectionIndex: Int): Int {
        Log.d("DataSize", listData[sectionIndex].size.toString())
        return listData[sectionIndex].size;
    }

    override fun doesSectionHaveHeader(sectionIndex: Int): Boolean {
        return !headers[sectionIndex].getHeaderName().isEmpty();
    }

    override fun doesSectionHaveFooter(sectionIndex: Int): Boolean {
        return !headers[sectionIndex].getHeaderName().isEmpty();
    }

    override fun onBindItemViewHolder(viewHolder: SectioningAdapter.ItemViewHolder?, sectionIndex: Int, itemIndex: Int, itemUserType: Int) {
        super.onBindItemViewHolder(viewHolder, sectionIndex, itemIndex, itemUserType)
        val vh = viewHolder as ItemViewHolder;
        vh.getTextView()?.text = listData[sectionIndex][itemIndex].room?.name;
    }

    override fun onBindHeaderViewHolder(viewHolder: SectioningAdapter.HeaderViewHolder?, sectionIndex: Int, headerUserType: Int) {
        super.onBindHeaderViewHolder(viewHolder, sectionIndex, headerUserType)
        val vh = viewHolder as HeaderViewHolder;
        vh.getHeaderName()?.text = headers[sectionIndex].getHeaderName();
        vh.getNumber()?.text = headers[sectionIndex].getNumber()
        vh.getImgIcon()?.resources!!.getDrawable(headers[sectionIndex].getImgIcon())
    }

    override fun onBindFooterViewHolder(viewHolder: SectioningAdapter.FooterViewHolder?, sectionIndex: Int, footerUserType: Int) {
        super.onBindFooterViewHolder(viewHolder, sectionIndex, footerUserType)
        val vh = viewHolder as FooterViewHolder;
        vh.getHeaderName()?.text = headers[sectionIndex].getHeaderName();
        vh.getNumber()?.text = headers[sectionIndex].getNumber()
        vh.getImgIcon()?.resources!!.getDrawable(headers[sectionIndex].getImgIcon())
    }

    override fun onCreateItemViewHolder(parent: ViewGroup?, itemUserType: Int): SectioningAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_room, parent, false);
        return ItemViewHolder(view);
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?, headerUserType: Int): SectioningAdapter.HeaderViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_header, parent, false);
        return HeaderViewHolder(view);
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup?, footerUserType: Int): SectioningAdapter.FooterViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_header, parent, false);
        return FooterViewHolder(view);
    }
}