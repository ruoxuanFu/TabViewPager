package com.quaie.wms.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by yue on 2017/1/17.
 * 　　　　　　　  ┏┓　 ┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　     ┃
 * 　　　　　　　┃　　　━　    ┃ ++ + + +
 * 　　　　　　 ████━████     ┃++  ++
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃  +  +
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */

public class ViewPagerIndicator extends LinearLayout {

    //画笔
    private Paint mPaint;
    //三角形可以通过三条线构成一个闭合区域
    private Path mPath;
    //三角形宽度
    private int mTriangleWidth;
    //三角形高度
    private int mTriangleHeight;
    //三角形底边宽度和vp的每一个tab的宽度的比例
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;
    //三角形初始化的偏移位置
    private int mInitTranslationX;
    //三角形移动时的位置
    private int mTranslationX;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        //画出来的连接线会有一个圆角的效果
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //此方法可以在控件的宽高发生变化时，回调次方法
    //用次方法来绘制三角形的宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //三角形底边宽度：总宽度/条目数*占底边的比例
        mTriangleWidth = (int) (w / 3 * RADIO_TRIANGLE_WIDTH);
        //初始化三角形的位置：w/3 = 每个条目的宽度 ，w/3/2 = 每个条目一半的宽度，
        //w/3/2 - mTriangleWidth / 2 = 当前条目一半的宽度再向左边偏移 1/2个三角形底边宽度
        mInitTranslationX = w / 3 / 2 - mTriangleWidth / 2;

        initTriangle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();
        //画笔平移到指定位置
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
        //画出三角形
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 初始化三角形指示器
     */
    private void initTriangle() {

        mTriangleHeight = mTriangleWidth / 2;

        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    /**
     * 三角指示器跟随手指滑动
     * 确定mTranslationX的值
     *
     * @param position
     * @param positionOffset
     */
    public void scroll(int position, float positionOffset) {
        int tabWidth = getWidth() / 3;
        mTranslationX = (int) (tabWidth * (positionOffset + position));

        //确定了三角形的位置，接下来让三角形进行重绘
        invalidate();
    }
}
