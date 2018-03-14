package com.zhoutianchu.framework.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import com.zhoutianchu.framework.R;


/**
 * Created by caifangmao on 1/27/16.
 */

public class NexusStyleLockView extends GestureLockView {


    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private int mCenterX, mCenterY;

    private int mWidth, mHeight;

    private int mRadius;


    private int COLOR_NORMAL = 0xFF029FF1;

    private int COLOR_ERROR = 0xFFFF0000;


    private float innerRate = 0.10F;

    private float outerWidthRate = 0.10F;

    private float outerRate = 0.5F;


    private float arrowRate = 0.1F;

    private float arrowDistanceRate = 0.35F;

    private int arrowDistance;


    private Path arrow;


    public NexusStyleLockView(Context context) {
        this(context, null);
    }


    public NexusStyleLockView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);

    }


    public NexusStyleLockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        COLOR_NORMAL = context.getResources().getColor(R.color.colorGestureNormal);
        COLOR_ERROR = context.getResources().getColor(R.color.colorGestureError);
        arrow = new Path();
    }


    @Override

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);


        mWidth = w;

        mHeight = h;


        mCenterX = w / 2;

        mCenterY = h / 2;


        mRadius = w > h ? h : w;

        mRadius /= 2;


        arrowDistance = (int) (mRadius * arrowDistanceRate);


        int length = (int) (mRadius * arrowRate);

        arrow.reset();

        arrow.moveTo(mCenterX - length, mCenterY + length - arrowDistance);

        arrow.lineTo(mCenterX, mCenterY - arrowDistance);

        arrow.lineTo(mCenterX + length, mCenterY + length - arrowDistance);

        arrow.close();

    }


    @Override

    protected void doArrowDraw(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(COLOR_ERROR);

        canvas.drawPath(arrow, mPaint);

    }


    @Override

    protected void doDraw(LockerState state, Canvas canvas) {

        switch (state) {

            case LOCKER_STATE_NORMAL:

                mPaint.setStyle(Paint.Style.FILL);

                mPaint.setColor(COLOR_NORMAL);

                canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);

                break;

            case LOCKER_STATE_SELECTED:

                mPaint.setStyle(Paint.Style.STROKE);

                mPaint.setColor(COLOR_NORMAL);

                mPaint.setStrokeWidth(mRadius * outerWidthRate);

                canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);

                mPaint.setStrokeWidth(2);

                canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);

                break;

            case LOCKER_STATE_ERROR:

                mPaint.setStyle(Paint.Style.STROKE);

                mPaint.setColor(COLOR_ERROR);

                mPaint.setStrokeWidth(mRadius * outerWidthRate);

                canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);

                mPaint.setStrokeWidth(2);

                canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);

                break;

        }

    }

}