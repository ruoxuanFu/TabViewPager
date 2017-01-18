package com.quaie.wms.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaie.wms.myapplication.R;

import java.util.List;

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
    //三角形最大尺寸
    private final int TRIANGLE_MAXSIZE = (int) (getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH);
    //三角形最小尺寸
    private final int TRIANGLE_MINSIZE = (int) (getScreenWidth() / 5 * RADIO_TRIANGLE_WIDTH);
    //三角形初始化的偏移位置
    private int mInitTranslationX;
    //三角形移动时的位置
    private int mTranslationX;
    //设置可见的tab数量
    private int mTabVisibleCount;
    //设置默认的tab数量
    private static final int COUNT_TAB_DEFULT = 3;
    //动态添加tab
    private List<String> mTitles;
    //设置进来的viewpager
    private ViewPager mViewPager;
    //设置选中的tab颜色
    private int mTabHighLightColor;
    //tab默认的颜色
    private int mDeTabHighLightColor = Color.BLACK;

    //由于viewpager的滑动监听被占用，所以这里再实现一个滑动监听viewpager
    public interface OnPageChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    //自定义接口的成员
    public OnPageChangeListener mListener;

    //自定义接口对外开放的set方法
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性，这里是可见tab的数量
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);

        mTabVisibleCount = ta.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_TAB_DEFULT);

        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_TAB_DEFULT;
        }

        mTabHighLightColor = ta.getColor(R.styleable.ViewPagerIndicator_color_tab_highlight, mDeTabHighLightColor);

        ta.recycle();

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
        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);

        if (TRIANGLE_MAXSIZE < mTriangleWidth) {
            mTriangleWidth = TRIANGLE_MAXSIZE;
        } else if (TRIANGLE_MINSIZE > mTriangleWidth) {
            mTriangleWidth = TRIANGLE_MINSIZE;
        }

        //初始化三角形的位置：w/3 = 每个条目的宽度 ，w/3/2 = 每个条目一半的宽度，
        //w/3/2 - mTriangleWidth / 2 = 当前条目一半的宽度再向左边偏移 1/2个三角形底边宽度
        mInitTranslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2;

        initTriangle();
    }

    //当XML加载完成后，会回调这个方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取子view的个数
        int cCount = getChildCount();

        if (cCount == 0) {
            return;
        }
        //获取子view
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams Llp = (LayoutParams) view.getLayoutParams();
            Llp.weight = 0;
            //设置tab宽度
            Llp.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(Llp);
        }

        setItemClickEvent();
    }

    /**
     * 获得屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
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
        int tabWidth = getWidth() / mTabVisibleCount;
        mTranslationX = (int) (tabWidth * (positionOffset + position));

        if (position >= mTabVisibleCount - 2 && positionOffset > 0 && getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * positionOffset), 0);
            } else {
                this.scrollTo(position * tabWidth + (int) (tabWidth * positionOffset), 0);
            }
        }

        //确定了三角形的位置，接下来让三角形进行重绘
        invalidate();
    }

    /**
     * 代码设置可见的tab数量，如果xml文件没有设置此值，需要在代码中设置此值，并且必须在setTabItemTitles()方法之前调用
     *
     * @param VisibleCount
     */
    public void setmTabVisibleCount(int VisibleCount) {
        mTabVisibleCount = VisibleCount;
    }

    //根据titles手动创建tab，而不是在xml文件中
    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            //清空控件中的所有view
            this.removeAllViews();

            //给title赋值
            mTitles = titles;
            for (String title : mTitles) {
                //添加控件
                addView(generateTextView(title));
            }

            setItemClickEvent();
        }
    }

    /**
     * 根据title创建tab
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mTabVisibleCount;
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(lp);
        return textView;
    }

    /**
     * 设置关联的viewpager和当前选中的位置
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);

                //设置自定义接口的回调
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {

                //设置自定义接口的回调
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }

                highLightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                //设置自定义接口的回调
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });

        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    /**
     * 设置选中高亮
     *
     * @param pos 高亮的位置
     */
    private void highLightTextView(int pos) {
        //重置tab颜色
        resetHighLightTextView();
        //上色
        View view = getChildAt(pos);
        //判断
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mTabHighLightColor);
        }
    }

    /**
     * 重置tab的高亮
     */
    private void resetHighLightTextView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            //判断
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mDeTabHighLightColor);
            }
        }
    }

    /**
     * 设置tab点击事件
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }


}
