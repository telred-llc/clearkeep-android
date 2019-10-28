package vmodev.clearkeep.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.content.res.ResourcesCompat;

import im.vector.R;

public class CustomAvatarPicture  extends View {

    Paint paint;
    Path path;

    public CustomAvatarPicture(Context context) {
        super(context);
        init();
    }

    public CustomAvatarPicture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAvatarPicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(ResourcesCompat.getColor(this.getResources(), R.color.color_avatar_edit_room, null));
        paint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        if (width > height) {
            radius = height / 2;
        } else {
            radius = width / 2;
        }

        Path path = new Path();
        path.addCircle(width / 2,
                height / 2, radius,
                Path.Direction.CW);

        Paint paint = new Paint();
        paint.setColor(ResourcesCompat.getColor(this.getResources(), R.color.color_avatar_edit_room, null));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);

        float center_x, center_y;
        final RectF oval = new RectF();

        center_x = width / 2;
        center_y = height / 2;

        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval, 10, 160, false, paint);

    }
}
