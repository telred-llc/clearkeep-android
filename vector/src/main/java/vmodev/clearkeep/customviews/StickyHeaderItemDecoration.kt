package vmodev.clearkeep.customviews

import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vmodev.clearkeep.customviews.interfaces.IStickyHeaderItemDecorationListener

class StickyHeaderItemDecoration constructor(private val listener: IStickyHeaderItemDecorationListener, private val recyclerView: RecyclerView) : RecyclerView.ItemDecoration() {
    private var stickyHeaderHeight: Int = 0;
    private var currentHeaderPos: Int = -1;
    private var currentHeader: View? = null;
    private val touchListener = object : RecyclerView.OnItemTouchListener {
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            if (e.y <= stickyHeaderHeight && MotionEvent.ACTION_DOWN == e.action) {
                listener.onClickStickyHeader(currentHeaderPos);
                return true;
            }
            return false;
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    init {
        recyclerView.addOnItemTouchListener(touchListener);
    }

    protected fun finalize() {
        recyclerView.removeOnItemTouchListener(touchListener);
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        parent.getChildAt(0)?.let { topChild ->
            val topChildPosition = parent.getChildAdapterPosition(topChild);
            if (topChildPosition == RecyclerView.NO_POSITION)
                return;
            val headerPosition = listener.getHeaderPositionForItem(topChildPosition);
            if (currentHeaderPos != headerPosition) {
                currentHeader = listener.createView(headerPosition, parent);
            }
            currentHeader?.let {
                fixLayoutSize(parent, it)
                val contactPoint = it.bottom;
                val childContact = getChildInContact(parent, contactPoint, headerPosition);
                if (childContact != null && listener.isHeader(parent.getChildAdapterPosition(childContact))) {
                    moveHeader(c, it, childContact);
                } else {
                    drawHeader(c, it);
                }
            };
            currentHeaderPos = headerPosition;
        };
    }

    private fun fixLayoutSize(viewGroup: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.width, View.MeasureSpec.EXACTLY);
        val heightSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.height, View.MeasureSpec.UNSPECIFIED);

        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, viewGroup.paddingLeft + viewGroup.paddingRight, view.layoutParams.width);
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, viewGroup.paddingTop + viewGroup.paddingBottom, view.layoutParams.height);
        view.measure(childWidthSpec, childHeightSpec);
        stickyHeaderHeight = view.measuredHeight;
        view.layout(0, 0, view.measuredWidth, view.measuredHeight);
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int, currentHeaderPos: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            var heightTolerance = 0
            val child = parent.getChildAt(i)

            if (currentHeaderPos != i) {
                val isChildHeader = listener?.isHeader(parent.getChildAdapterPosition(child))
                if (isChildHeader) {
                    heightTolerance = stickyHeaderHeight - child.height
                }
            }

            val childBottomPosition: Int
            childBottomPosition = if (child.top > 0) {
                child.bottom + heightTolerance
            } else {
                child.bottom
            }

            if (childBottomPosition > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }
}