package com.example.griddemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private DragView selectRect;
    private Rect currentSelectRect = new Rect();
    private RelativeLayout rl;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setStatusBarColor(this);
        setContentView(R.layout.activity_main);
        rl = findViewById(R.id.rl);
        gridView = findViewById(R.id.grid_view);
        selectRect = findViewById(R.id.view);
        selectRect.setTouchListener(new DragView.touchListener() {
            @Override
            public void moveView(Rect rect, int moveX, int moveY) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) selectRect.getLayoutParams();
                params.leftMargin = params.leftMargin + moveX;
                params.topMargin = params.topMargin + moveY;
                selectRect.setLayoutParams(params);
//                selectRect.setOrderViewRect(getOrderViewRect());
                selectRect.setMoving(false);
                Log.d("weeee", "moveView");
            }

            @Override
            public void scaleView(float scaleX, float scaleY) {
                Log.d("weeee", "scaleView");
                selectRect.setScaleX(scaleX);
                selectRect.setScaleY(scaleY);
            }

            @Override
            public void drawRect(Rect rect) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rect.right - rect.left, rect.bottom - rect.top);
                params.leftMargin = rect.left;
                params.topMargin = rect.top;
                selectRect.setLayoutParams(params);
                selectRect.setOrderViewRect(getOrderViewRect());
            }
        });
        gridView.setLinesX(30);
        gridView.setLinesY(15);
        showLine(true);
        gridView.setTouchListener(new GridView.touchListener() {
            @Override
            public void drawRect(Rect rect, int signalWidth, int signalHeight) {
                showSelectRect(rect);
                selectRect.setSignalWidth(signalWidth);
                selectRect.setSignalHeight(signalHeight);
            }
        });

        LinearLayout l = findViewById(R.id.ll);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(this));
        l.setLayoutParams(params);
    }

    private void showSelectRect(Rect rect) {
        currentSelectRect = rect;
        selectRect.setBorderRect(getBorderRect());
        selectRect.setOrderViewRect(getOrderViewRect());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rect.right - rect.left, rect.bottom - rect.top);
        params.leftMargin = rect.left;
        params.topMargin = rect.top;
        selectRect.setLayoutParams(params);
        selectRect.setScaleY(1);
        selectRect.setScaleX(1);
        selectRect.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    private Rect getBorderRect() {
        return new Rect(rl.getLeft(), rl.getTop(), rl.getRight(), rl.getBottom());
    }

    private Rect getOrderViewRect() {
        return new Rect(selectRect.getLeft() + rl.getLeft(), selectRect.getTop() + rl.getTop(),
                selectRect.getRight() + rl.getLeft(), selectRect.getBottom() + rl.getTop());
    }

    private void showLine(boolean showLine) {
        gridView.setShowLine(showLine);
        selectRect.setShowLine(false);
    }

    private void animationX(int moveX) {
        ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                selectRect,
                "TranslationX",
                selectRect.getWidth(),
                selectRect.getWidth()+moveX);
        mAnimatorY.setDuration(0);
        mAnimatorY.start();
    }

    private void animationY(int moveY) {
        ObjectAnimator mAnimatorY = ObjectAnimator.ofFloat(
                selectRect,
                "TranslationY",
                selectRect.getHeight(),
                selectRect.getHeight()+moveY);
        mAnimatorY.setDuration(0);
        mAnimatorY.start();
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static void showNotFulltStatusBar(Context context){
        Activity activity = (Activity) context;
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                        |WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}