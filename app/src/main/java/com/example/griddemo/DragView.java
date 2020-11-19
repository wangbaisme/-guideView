package com.example.griddemo;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class DragView extends RelativeLayout {

    private int lastX, lastY, currentX, currentY;
    private Rect orderViewRect = new Rect();
    private Rect borderRect = new Rect();
    private touchListener listener = null;
    private boolean showLine = false;
    private int signalWidth;
    private int signalHeight;
    float realMoveX, realMoveY;
    private boolean isMoving = false;

    public DragView(Context context) {
        super(context);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    public int getSignalWidth() {
        return signalWidth;
    }

    public void setSignalWidth(int signalWidth) {
        this.signalWidth = signalWidth;
    }

    public int getSignalHeight() {
        return signalHeight;
    }

    public void setSignalHeight(int signalHeight) {
        this.signalHeight = signalHeight;
    }

    public Rect getOrderViewRect() {
        return orderViewRect;
    }

    public void setOrderViewRect(Rect rect) {
        this.orderViewRect.set(rect.left, rect.top, rect.right, rect.bottom);
    }

    public Rect getBorderRect() {
        return borderRect;

    }

    public void setBorderRect(Rect rect) {
        this.borderRect.set(rect.left, rect.top, rect.right, rect.bottom);
    }



    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX() + orderViewRect.left;
                lastY = (int) event.getY() + orderViewRect.top;
                break;
            case MotionEvent.ACTION_MOVE:
                if (listener != null) {
                    synchronized (listener) {
                        currentX = (int)event.getX() + orderViewRect.left;
                        currentY = (int) event.getY() + orderViewRect.top;
                        int[] realXY = getRealMoveXY(lastX, lastY, currentX, currentY);
                        isMoving = true;
                        listener.moveView(orderViewRect, realXY[0], realXY[1]);
                        lastX = currentX;
                        lastY = currentY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private int[] getRealMoveXY(int lastX, int lastY, int currentX, int currentY) {
        int realMoveX = 0, realMoveY = 0;
        int[] realXY = new int[2];
        int moveX, moveY;
        moveX = currentX - lastX;
        moveY = currentY - lastY;
        if (moveX >= 0) {
            realMoveX = orderViewRect.right + moveX > borderRect.right ?
                    borderRect.right - orderViewRect.right : moveX;
        } else {
            realMoveX = orderViewRect.left + moveX < borderRect.left ?
                    borderRect.left - orderViewRect.left : moveX;
        }
        if (moveY >= 0) {
            realMoveY = orderViewRect.bottom + moveY > borderRect.bottom ?
                    borderRect.bottom - orderViewRect.bottom : moveY;
        } else {
            realMoveY = orderViewRect.top + moveY < borderRect.top ?
                    borderRect.top - orderViewRect.top : moveY;
        }
        orderViewRect.left += realMoveX;
        orderViewRect.right += realMoveX;
        orderViewRect.top += realMoveY;
        orderViewRect.bottom += realMoveY;
        realXY[0] = realMoveX;
        realXY[1] = realMoveY;
        return realXY;
    }

    private float[] getRealScaleXY(float lastX, float lastY, float currentX, float currentY) {
        float realScaleX = 0, realScaleY = 0;
        float[] realScaleXY = new float[2];
        float moveX, moveY;
        moveX = currentX - lastX;
        moveY = currentY - lastY;
        realMoveX += moveX;
        realMoveY += moveY;
        realScaleX = (float) (orderViewRect.right - orderViewRect.left + realMoveX) / (float) (orderViewRect.right - orderViewRect.left);
        realScaleY = (float) (orderViewRect.bottom - orderViewRect.top + realMoveY) / (float) (orderViewRect.bottom - orderViewRect.top);
        realScaleXY[0] = realScaleX;
        realScaleXY[1] = realScaleY;
        return realScaleXY;
    }

    public void setTouchListener(touchListener listener) {
        this.listener = listener;
    }

    public interface touchListener {
        void moveView(Rect rect, int moveX, int moveY);
        void scaleView(float scaleX, float scaleY);
        void drawRect(Rect rect);
    }
}
