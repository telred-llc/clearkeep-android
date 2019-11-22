package vmodev.clearkeep.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import im.vector.R
import im.vector.databinding.ItemMessageSearchBinding
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import java.util.*

class ListSearchMessageRecyclerViewAdapter constructor( appExecutors: AppExecutors,
                                                       private val dataBindingComponent: IDataBindingComponent,
                                                       diffCallback: DiffUtil.ItemCallback<MessageRoomUser>,
                                                       private val itemClick: (MessageRoomUser) -> Unit?)
    : ListAdapter<MessageRoomUser, DataBoundViewHolder<ItemMessageSearchBinding>>(AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()) {

    private  var searchText : String? = null

    public fun getSearchText(text : String) {
        searchText = text
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataBoundViewHolder<ItemMessageSearchBinding> {
        val binding = DataBindingUtil.inflate<ItemMessageSearchBinding>(LayoutInflater.from(p0.context),
                R.layout.item_message_search, p0, false, dataBindingComponent.getDataBindingComponent());
        binding.root.setOnClickListener { v ->
            binding.result?.let {
                itemClick?.invoke(it)
            }
        }
        return DataBoundViewHolder(binding);
    }

    override fun onBindViewHolder(p0: DataBoundViewHolder<ItemMessageSearchBinding>, p1: Int) {
        p0.binding.result = getItem(p1);
        var fullText =getItem(p1).message?.encryptedContent
        if (searchText!= null && !searchText!!.isEmpty()) {
            var spannable = SpannableString(fullText)
//            var searchWords = searchText!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            for (searchWord in searchWords) {
                var startPos = fullText!!.toLowerCase(Locale.US).indexOf(searchText!!.toLowerCase(Locale.US))
                var endPos = startPos + searchText!!.length

                if (startPos != -1) {
                    val blueColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ResourcesCompat.getColor(p0.itemView.resources,R.color.color_highlight, null)))
                    val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, blueColor, null)
                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
//            }
            p0.binding.textViewLastMessage.text= spannable
        }else{
            p0.binding.textViewLastMessage.text= fullText
        }
    }
}