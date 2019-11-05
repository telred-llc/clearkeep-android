package vmodev.clearkeep.customviews.interfaces

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface IStickyHeaderItemDecorationListener {
    fun getHeaderPositionForItem(itemPosition: Int): Int;
    fun isHeader(itemPosition: Int): Boolean;
    fun createView(headerPosition: Int, parent: RecyclerView) : View;
}