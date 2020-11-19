package com.example.griddemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GridView extends View {

    private int linesX = 5;
    private int linesY = 10;

    private int width;
    private int height;
    private Paint mLinePaint = null;
    private Paint mRectPaint = null;
    private touchListener listener = null;
    private int startX, startY;
    private boolean showLine = false;

    public GridView(Context context) {
        this(context, null);
    }

    public GridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridView);
        linesX = a.getInteger(R.styleable.GridView_linesX, linesX);
        linesY = a.getInteger(R.styleable.GridView_linesY, linesY);
        a.recycle();
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(1);

        mRectPaint = new Paint();
        mRectPaint.setColor(Color.RED);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(1);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showLine) {
            int x = width / (linesX + 1);
            int y = height / (linesY + 1);
            for (int i = 1; i <= linesX; i++) {
                canvas.drawLine(x * i, 0, x * i, height, mLinePaint);
            }

            for (int i = 1; i <= linesY; i++) {
                canvas.drawLine(0, y * i, width, y * i, mLinePaint);
            }
            canvas.drawLine(0, 0, 0, height, mLinePaint);
            canvas.drawLine(width-1, 0, width-1, height, mLinePaint);
            canvas.drawLine(0, 0, width, 0, mLinePaint);
            canvas.drawLine(0, height-1, width, height-1, mLinePaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                Rect rect;
                if (showLine) {
                    rect = getOrderRect(startX, startY, (int)event.getX(), (int)event.getY());
                } else {
                    rect = getRect(startX, startY, (int)event.getX(), (int)event.getY());
                }
                if (listener != null) {
                    listener.drawRect(rect, width/(linesX+1), height/(linesY+1));
                }
                break;
        }
        return true;
    }

    private Rect getOrderRect(int startX, int startY, int stopX, int stopY){
        int selectRectStartX, selectRectStopX, selectRectStartY, selectRectStopY;
        Rect rect = new Rect();
        int signalWidth = width/(linesX+1);
        int signalHeight = height/(linesY+1);
        if (startX <= stopX) {
            selectRectStartX = (startX / signalWidth) * signalWidth;
            selectRectStopX = stopX > width ? width : ((stopX / signalWidth) + 1) * signalWidth;
        } else {
            selectRectStopX = ((startX / signalWidth) + 1) * signalWidth;
            selectRectStartX = stopX < 0 ? 0 : ((stopX / signalWidth)) * signalWidth;
        }
        if (startY <= stopY) {
            selectRectStartY = (startY / signalHeight) * signalHeight;
            selectRectStopY = stopY > height ? height : ((stopY / signalHeight) + 1) * signalHeight;
        } else {
            selectRectStopY = ((startY / signalHeight) + 1) * signalHeight;
            selectRectStartY = stopY < 0 ? 0 : ((stopY / signalHeight)) * signalHeight;
        }
        rect.set(selectRectStartX, selectRectStartY, selectRectStopX, selectRectStopY);
        return rect;
    }

    private Rect getRect(int startX, int startY, int stopX, int stopY){
        int selectRectStartX, selectRectStopX, selectRectStartY, selectRectStopY;
        Rect rect = new Rect();
        if (startX <= stopX) {
            selectRectStartX = startX;
            selectRectStopX = stopX > width ? width : stopX;
        } else {
            selectRectStopX = startX;
            selectRectStartX = stopX < 0 ? 0 : stopX;
        }
        if (startY <= stopY) {
            selectRectStartY = startY;
            selectRectStopY = stopY > height ? height : stopY;
        } else {
            selectRectStopY = startY;
            selectRectStartY = stopY < 0 ? 0 : stopY;
        }
        rect.set(selectRectStartX, selectRectStartY, selectRectStopX, selectRectStopY);
        return rect;
    }

    public void setTouchListener(touchListener listener) {
        this.listener = listener;
    }

    public interface touchListener {
        void drawRect(Rect rect, int signalWidth, int signalHeight);
    }

    public int getLinesX() {
        return linesX;
    }

    public void setLinesX(int linesX) {
        this.linesX = linesX;
    }

    public int getLinesY() {
        return linesY;
    }

    public void setLinesY(int linesY) {
        this.linesY = linesY;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }


}