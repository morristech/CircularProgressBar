package com.brkckr.circularprogressbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CircularProgressBar extends View
{
    private static final int START_ANGLE = 270;

    public enum State
    {
        CLOCKWISE, COUNTERCLOCKWISE
    }

    private State progressState = State.CLOCKWISE;

    private float progressValue = 0;

    private float progressWidth;
    private int progressColor;

    private float backgroundWidth;
    private int backgroundColor;

    private RectF rectF;
    private Paint backgrounPaint;
    private Paint progressPaint;

    public CircularProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);

        int state = typedArray.getInt(R.styleable.CircularProgressBar_cpbState, 0);
        if (state == 0)
        {
            progressState = State.CLOCKWISE;
        } else
        {
            progressState = State.COUNTERCLOCKWISE;
        }

        progressValue = typedArray.getFloat(R.styleable.CircularProgressBar_cpbProgressValue, progressValue);
        progressWidth = typedArray.getDimension(R.styleable.CircularProgressBar_cpbProgressWidth, getResources().getDimension(R.dimen.progress_width));
        backgroundWidth = typedArray.getDimension(R.styleable.CircularProgressBar_cpbBackgroundWidth, getResources().getDimension(R.dimen.background_width));
        progressColor = typedArray.getInt(R.styleable.CircularProgressBar_cpbProgressColor, Color.BLACK);
        backgroundColor = typedArray.getInt(R.styleable.CircularProgressBar_cpbBackgroundColor, Color.GRAY);

        typedArray.recycle();

        rectF = new RectF();

        backgrounPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgrounPaint.setColor(backgroundColor);
        backgrounPaint.setStyle(Paint.Style.STROKE);
        backgrounPaint.setStrokeWidth(backgroundWidth);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgrounPaint);

        if (progressState == State.CLOCKWISE)
        {
            float angle = 360 * progressValue / 100;
            canvas.drawArc(rectF, START_ANGLE, angle, false, progressPaint);
        } else
        {
            float angle = 360 * progressValue / 100 - 360;
            canvas.drawArc(rectF, START_ANGLE, angle, false, progressPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (progressWidth > backgroundWidth) ? progressWidth : backgroundWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    public State getState()
    {
        return progressState;
    }

    public void setState(State state)
    {
        progressState = state;

        requestLayout();
        invalidate();
    }

    public float getProgressWidth()
    {
        return progressWidth;
    }

    public void setProgressWidth(float width)
    {
        progressWidth = width;
        progressPaint.setStrokeWidth(progressWidth);
        requestLayout();
        invalidate();
    }

    public float getBackgroundWidth()
    {
        return backgroundWidth;
    }

    public void setBackgroundWidth(float width)
    {
        backgroundWidth = width;
        backgrounPaint.setStrokeWidth(backgroundWidth);
        requestLayout();
        invalidate();
    }

    @ColorInt
    public int getProgressColor()
    {
        return progressColor;
    }

    public void setProgressColor(@ColorInt int color)
    {
        progressColor = color;
        progressPaint.setColor(progressColor);
        invalidate();
        requestLayout();
    }

    @ColorInt
    public int getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(@ColorInt int color)
    {
        backgroundColor = color;
        backgrounPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    public float getProgressValue()
    {
        return progressValue;
    }

    public void setProgressValue(float progress)
    {
        progressValue = (progress <= 100) ? progress : 100;
        invalidate();
    }

    public void setProgressValueWithAnimation(float progressValue)
    {
        setProgressValueWithAnimation(progressState, progressValue, 1500);
    }

    public void setProgressValueWithAnimation(float progressValue, int duration)
    {
        setProgressValueWithAnimation(progressState, progressValue, duration);
    }

    public void setProgressValueWithAnimation(State state, float progressValue)
    {
        setProgressValueWithAnimation(state, progressValue, 1500);
    }

    public void setProgressValueWithAnimation(State state, float progressValue, int duration)
    {
        progressState = state;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progressValue", progressValue);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}