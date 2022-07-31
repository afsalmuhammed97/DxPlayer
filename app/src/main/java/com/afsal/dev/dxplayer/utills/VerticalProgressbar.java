package com.afsal.dev.dxplayer.utills;import android.content.Context;import android.graphics.Canvas;import android.util.AttributeSet;import android.widget.ProgressBar;public class VerticalProgressbar extends ProgressBar {    private int x, y, z, w;    public VerticalProgressbar(Context context) {        super(context);    }    public VerticalProgressbar(Context context, AttributeSet attrs) {        super(context, attrs);    }    public VerticalProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }    public VerticalProgressbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {        super(context, attrs, defStyleAttr, defStyleRes);    }    protected void onSizeChanged(int w, int h, int oldw, int oldh) {        super.onSizeChanged(h, w, oldh, oldw);        this.x = w;        this.y = h;        this.z = oldw;        this.w = oldh;    }    @Override    protected synchronized void onMeasure(int widthMeasureSpec,                                          int heightMeasureSpec) {        super.onMeasure(heightMeasureSpec, widthMeasureSpec);        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());    }    protected void onDraw(Canvas c) {//        c.rotate(-90);//        c.translate(-getHeight(), 0);        super.onDraw(c);    }    @Override    public synchronized void setProgress(int progress) {        if (progress >= 0)            super.setProgress(progress);        else            super.setProgress(0);        onSizeChanged(x, y, z, w);    }}