package vmodev.clearkeep.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import im.vector.R;

import static android.graphics.Color.BLACK;

/**
 * Created by OngNau on 12/27/2017.
 */

public class CircleImage extends AppCompatImageView {
    private static final float STROKE_WIDTH = 20f;
    private RectF mRect = new RectF();
    private Path mClipPath = new Path();
    //    private int mImageSize;
    private Drawable mDrawable;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mBorderColor;
    private float stroke_width;
//    private int mImageResource;

    public CircleImage(Context context) {
        super(context);
        init();
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
        init();
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(attrs);
        init();
    }

    private void getAttributes(AttributeSet attr) {
        TypedArray typedArray = getContext()
                .getTheme()
                .obtainStyledAttributes(attr, R.styleable.CircleImage, 0, 0);
        try {
            mBorderColor = typedArray.getColor(R.styleable.CircleImage_border_color, Color.WHITE);
            stroke_width = typedArray.getDimension(R.styleable.CircleImage_stroke_width, 0);
        } finally {
            typedArray.recycle();
        }

    }

    protected void init() {
        /**
         * Below Jelly Bean, clipPath on canvas would not work because lack of hardware acceleration
         * support. Hence, we should explicitly say to use software acceleration.
         **/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        setBorderColor(mBorderColor);

    }

    /**
     * create placeholder drawable
     */
    private void createDrawable() {
        mDrawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                int centerX = Math.round(canvas.getWidth() * 0.5f);
                int centerY = Math.round(canvas.getHeight() * 0.5f);

                /**
                 * draw a circle shape for placeholder image
                 */
                canvas.drawCircle(centerX, centerY, (canvas.getHeight() / 2) - stroke_width, mBorderPaint);

            }

            @Override
            public void setAlpha(int i) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
    }

    private void fillImages() {
        mPaint.setColor(BLACK);
        createDrawable();
    }

    /**
     * Set the canvas bounds here
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        mRect.set(0, 0, screenWidth, screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float borderWidth = 2f;
//        canvas.drawCircle(mRect.centerX(), mRect.centerY(), (mRect.height() / 2) - borderWidth, mPaint);
        canvas.drawCircle(mRect.centerX(), mRect.centerY(), (mRect.height() / 2), mBorderPaint);
        mClipPath.addCircle(mRect.centerX(), mRect.centerY(), (mRect.height() / 2) - stroke_width, Path.Direction.CW);
        canvas.clipPath(mClipPath);
        super.onDraw(canvas);
        showImage();
    }

    public void showImage() {
        fillImages();
    }

    private void setBorderColor(int color) {
        mBorderPaint.setColor(color);
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setAntiAlias(true);
        invalidate();
    }
}