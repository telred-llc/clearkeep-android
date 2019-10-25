package vmodev.clearkeep.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;

public abstract class NormalDecoration extends RecyclerView.ItemDecoration {
    protected String TAG = "QDX";
    private Paint mHeaderTxtPaint;
    private Paint mHeaderContentPaint;

    protected int headerHeight = 136;
    private int textPaddingLeft = 50;
    private int textSize = 50;
    private int textColor = Color.BLACK;
    private int headerContentColor = 0xffeeeeee;
    private final float txtYAxis;
    private RecyclerView mRecyclerView;


    public NormalDecoration() {
        mHeaderTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTxtPaint.setColor(textColor);
        mHeaderTxtPaint.setTextSize(textSize);
        mHeaderTxtPaint.setTextAlign(Paint.Align.LEFT);
        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        mHeaderTxtPaint.setTypeface(typeface);

        mHeaderContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderContentPaint.setColor(headerContentColor);
        Paint.FontMetrics fontMetrics = mHeaderTxtPaint.getFontMetrics();
        float total = -fontMetrics.ascent + fontMetrics.descent;
        txtYAxis = total / 2 - fontMetrics.descent;
    }
    private boolean isInitHeight = false;


    @Override
    public void getItemOffsets(Rect outRect, View itemView, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, itemView, parent, state);
        if (mRecyclerView == null) {
            mRecyclerView = parent;
        }

        if (headerDrawEvent != null && !isInitHeight) {
            View headerView = headerDrawEvent.getHeaderView(0);
            headerView
                    .measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            headerHeight = headerView.getMeasuredHeight();
            isInitHeight = true;
        }

        int pos = parent.getChildAdapterPosition(itemView);
        String curHeaderName = getHeaderName(pos);

        if (curHeaderName == null) {
            return;
        }
        if (pos == 0 || !curHeaderName.equals(getHeaderName(pos - 1))) {
            outRect.top = headerHeight;
        }
    }

    public abstract String getHeaderName(int pos);

    private SparseArray<Integer> stickyHeaderPosArray = new SparseArray<>();
    private GestureDetector gestureDetector;


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView;
        }
        if (gestureDetector == null) {
            gestureDetector = new GestureDetector(recyclerView.getContext(), gestureListener);
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        stickyHeaderPosArray.clear();

        int childCount = recyclerView.getChildCount();
        int left = recyclerView.getLeft() + recyclerView.getPaddingLeft();
        int right = recyclerView.getRight() - recyclerView.getPaddingRight();

        String firstHeaderName = null;
        int firstPos = 0;
        int translateTop = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            int pos = recyclerView.getChildAdapterPosition(childView);
            String curHeaderName = getHeaderName(pos);
            if (i == 0) {
                firstHeaderName = curHeaderName;
                firstPos = pos;
            }
            if (curHeaderName == null)
                continue;

            int viewTop = childView.getTop() + recyclerView.getPaddingTop();
            if (pos == 0 || !curHeaderName.equals(getHeaderName(pos - 1))) {
                if (headerDrawEvent != null) {
                    View headerView;
                    if (headViewMap.get(pos) == null) {
                        headerView = headerDrawEvent.getHeaderView(pos);
                        headerView
                                .measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        headerView.setDrawingCacheEnabled(true);
                        headerView.layout(0, 0, right, headerHeight);
                        headViewMap.put(pos, headerView);
                        canvas.drawBitmap(headerView.getDrawingCache(), left, viewTop - headerHeight, null);

                    } else {
                        headerView = headViewMap.get(pos);
                        canvas.drawBitmap(headerView.getDrawingCache(), left, viewTop - headerHeight, null);
                    }
                } else {
                    canvas.drawRect(left, viewTop - headerHeight, right, viewTop, mHeaderContentPaint);
                    if(!TextUtils.isEmpty(curHeaderName) || !curHeaderName.equals("null")){
                        canvas.drawText(curHeaderName, left + textPaddingLeft, viewTop - headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
                    }
                }
                if (headerHeight < viewTop && viewTop <= 2 * headerHeight) {
                    translateTop = viewTop - 2 * headerHeight;
                }
                stickyHeaderPosArray.put(pos, viewTop);
            }
        }
        if (firstHeaderName == null)
            return;


        canvas.save();
        canvas.translate(0, translateTop);
        if (headerDrawEvent != null) {//inflater
            View headerView;
            if (headViewMap.get(firstPos) == null) {
                headerView = headerDrawEvent.getHeaderView(firstPos);
                headerView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                headerView.setDrawingCacheEnabled(true);
                headerView.layout(0, 0, right, headerHeight);
                headViewMap.put(firstPos, headerView);
                canvas.drawBitmap(headerView.getDrawingCache(), left, 0, null);
            } else {
                headerView = headViewMap.get(firstPos);
                canvas.drawBitmap(headerView.getDrawingCache(), left, 0, null);
            }
        } else {
            canvas.drawRect(left, 0, right, headerHeight, mHeaderContentPaint);
            canvas.drawText(firstHeaderName, left + textPaddingLeft, headerHeight / 2 + txtYAxis, mHeaderTxtPaint);
        }
        canvas.restore();

    }

    private HashMap<Integer, View> headViewMap = new HashMap<>();

    public interface OnHeaderClickListener {
        void headerClick(int pos);
    }

    private OnHeaderClickListener headerClickEvent;

    public void setOnHeaderClickListener(OnHeaderClickListener headerClickListener) {
        this.headerClickEvent = headerClickListener;
    }


    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < stickyHeaderPosArray.size(); i++) {
                int value = stickyHeaderPosArray.valueAt(i);
                float y = e.getY();
                if (value - headerHeight <= y && y <= value) {
                    if (headerClickEvent != null) {
                        headerClickEvent.headerClick(stickyHeaderPosArray.keyAt(i));
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    private OnDecorationHeadDraw headerDrawEvent;

    public interface OnDecorationHeadDraw {
        View getHeaderView(int pos);
    }


    public void setOnDecorationHeadDraw(OnDecorationHeadDraw decorationHeadDraw) {
        this.headerDrawEvent = decorationHeadDraw;
    }


    private Map<String, Drawable> imgDrawableMap = new HashMap<>();

    private Drawable getImg(String url) {
        return imgDrawableMap.get(url);
    }

    public void onDestroy() {
        headViewMap.clear();
        imgDrawableMap.clear();
        stickyHeaderPosArray.clear();
        mRecyclerView = null;
        setOnHeaderClickListener(null);
        setOnDecorationHeadDraw(null);
    }


    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public void setTextPaddingLeft(int textPaddingLeft) {
        this.textPaddingLeft = textPaddingLeft;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        this.mHeaderTxtPaint.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.mHeaderTxtPaint.setColor(textColor);
    }

    public void setHeaderContentColor(int headerContentColor) {
        this.headerContentColor = headerContentColor;
        this.mHeaderContentPaint.setColor(headerContentColor);
    }
}