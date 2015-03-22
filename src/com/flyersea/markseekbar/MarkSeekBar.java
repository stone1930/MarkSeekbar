package com.flyersea.markseekbar;

import java.util.ArrayList;
import com.flyersea.markseekbar.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class MarkSeekBar extends SeekBar {

	private final static int DEFAULT_POINT_RADIUS = 6; // 默认原点半径
	private final static float DEFAULT_TEXT_SIZE = 10; // 进度字体大小 px单位

	private ArrayList<Mark> mMarks = new ArrayList<Mark>(); // 标记点集合
	private Bitmap mBarBitmap;
	private Bitmap mBubbleBitmap;
	private int mMarkPaddingTop;
	private int mMarkPaddingLeft;
	private int mMarkPaddingRight;
	private int mMarkPaddingBottom;
	private boolean isMysetPadding = true;
	private String mText;
	private float mTextWidth;
	private float mBubbleWidth;
	private float mBubbleHeight;
	private Paint mTextPaint;
	private Paint mShapePaint;
	private Resources res;
	private int mTextPaddingLeft;
	private int mTextPaddingTop;
	private int mImageOffsetLeft;
	private int mImageOffsetTop;
	private int mMarksOffsetTop;
	private int mMarksOffsetLeftRight;
	private float mPointRadius = DEFAULT_POINT_RADIUS;
	private float mTtextSize = DEFAULT_TEXT_SIZE;
	private int progressMax;
	private Rect mBarDst = new Rect(); // 背景区域模型

	public MarkSeekBar(Context context) {
		super(context);
		init();
	}

	public MarkSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MarkSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	// 修改setpadding 使其在外部调用的时候无效
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if (isMysetPadding) {
			super.setPadding(left, top, right, bottom);
		}
	}

	// 初始化
	private void init() {
		res = getResources();
		initBitmap();
		initDraw();
		initPadding();
		initMarks();
		progressMax = getMax();
	}

	private void initBitmap() {
		mBarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_zhou);
		mBubbleBitmap = BitmapFactory.decodeResource(res, R.drawable.popwindow_bg1);
		if (mBubbleBitmap != null) {
			mBubbleWidth = mBubbleBitmap.getWidth();
			mBubbleHeight = mBubbleBitmap.getHeight();
		} else {
			mBubbleWidth = 0;
			mBubbleHeight = 0;
		}
	}

	private void initDraw() {
		mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTypeface(Typeface.DEFAULT);
		mTextPaint.setTextSize(mTtextSize);
		mTextPaint.setColor(0xff23fc4f);
	}

	// 初始化padding 使其左右上 留下位置用于展示进度图片
	private synchronized void initPadding() {
		int top = getBubbleBitmapHeigh() + mMarkPaddingTop;
		int left = getBubbleBitmapWidth() / 2 + mMarkPaddingLeft;
		int right = getBubbleBitmapWidth() / 2 + mMarkPaddingRight;
		int bottom = mMarkPaddingBottom;
		isMysetPadding = true;
		setPadding(left, top, right, bottom);
		isMysetPadding = false;
	}

	private void initMarks(){
		mMarks.add(new Mark(0));
		mMarks.add(new Mark(getMax()));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int thumbOffset = getThumbOffset();
		int offset = thumbOffset - mBarBitmap.getHeight()/2;
		mBarDst.left = getPaddingLeft() - thumbOffset ;
		mBarDst.top = getPaddingTop() + offset;
		mBarDst.right = getWidth() - (getPaddingRight() - thumbOffset);
		mBarDst.bottom = getPaddingTop() + mBarBitmap.getHeight()+ offset;
		canvas.drawBitmap(mBarBitmap, null, mBarDst, null);
		
		// 初始化首尾两端的标记
		float cy = mBarDst.top + mBarBitmap.getHeight() / 2 + mMarksOffsetTop;
		//canvas.drawCircle(mBarDst.left + mPointRadius, cy, mPointRadius, mShapePaint);
		//canvas.drawCircle(getWidth() / 2, cy, mPointRadius, mShapePaint);
		//canvas.drawCircle(mBarDst.right - mPointRadius, cy, mPointRadius, mShapePaint);
		if (mMarks.size() > 0) {
			float tmpX;
			float mBarDstWidth = mBarDst.width();
			for (int i = 0; i < mMarks.size(); i++) {
				if(0 == mMarks.get(i).postion){
					tmpX = mBarDst.left + mPointRadius + mMarksOffsetLeftRight;
				} else if (progressMax == mMarks.get(i).postion){
					tmpX = mBarDst.left + mBarDstWidth - mPointRadius - mMarksOffsetLeftRight;
				} else {
					tmpX = mBarDst.left + mBarDstWidth * (mMarks.get(i).postion/(float)progressMax);
				}
				mMarks.get(i).left = tmpX - mPointRadius;
				mMarks.get(i).top = cy - mPointRadius;
				mMarks.get(i).right = tmpX + mPointRadius;
				mMarks.get(i).bottom = cy + mPointRadius;
				canvas.drawCircle(tmpX, cy, mPointRadius, mShapePaint);
			}
		} 

		mText = (getProgress() * 100 / getMax()) + "%";
		mTextWidth = mTextPaint.measureText(mText);
		Rect bounds = this.getProgressDrawable().getBounds();
		float xImg = bounds.width() * getProgress() / getMax() + mImageOffsetLeft + mMarkPaddingLeft;
		float yImg = mImageOffsetTop + mMarkPaddingTop;
		float xText = bounds.width() * getProgress() / getMax() + mBubbleWidth / 2 - mTextWidth / 2 + mTextPaddingLeft
				+ mMarkPaddingLeft;
		float yText = yImg + mTextPaddingTop + mBubbleHeight / 2 + getTextHei() / 4;
		canvas.drawBitmap(mBubbleBitmap, xImg, yImg, null);
		canvas.drawText(mText, xText, yText, mTextPaint);

		super.onDraw(canvas);
	}

	/**
	 * 设置展示进度背景图片
	 * 
	 * @param resid
	 */
	public void setBitmap(int resid) {
		mBubbleBitmap = BitmapFactory.decodeResource(res, resid);
		if (mBubbleBitmap != null) {
			mBubbleWidth = mBubbleBitmap.getWidth();
			mBubbleHeight = mBubbleBitmap.getHeight();
		} else {
			mBubbleWidth = 0;
			mBubbleHeight = 0;
		}
		initPadding();
	}

	/**
	 * 替代setpadding
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setMyPadding(int left, int top, int right, int bottom) {
		mMarkPaddingTop = top;
		mMarkPaddingLeft = left;
		mMarkPaddingRight = right;
		mMarkPaddingBottom = bottom;
		isMysetPadding = true;
		setPadding(left + getBubbleBitmapWidth() / 2, top + getBubbleBitmapHeigh(), right + getBubbleBitmapWidth() / 2, bottom);
		isMysetPadding = false;
	}

	/**
	 * 设置进度字体大小
	 * 
	 * @param textsize
	 */
	public void setTextSize(float textsize) {
		this.mTtextSize = textsize;
		mTextPaint.setTextSize(textsize);
	}

	/**
	 * 设置进度字体颜色
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		mTextPaint.setColor(color);
	}
	
	/**
	 * 设置标签圆上偏移
	 * @param offsetTop
	 */
	public void setMarksOffsetTop(int offsetTop) {
		this.mMarksOffsetTop = offsetTop;
		invalidate();
	}
	
	public void setMarksOffsetLeftRight(int offset) {
		this.mMarksOffsetLeftRight = offset;
		invalidate();
	}
	
	/**
	 * 调整进度字体的位置 初始位置为图片的正中央
	 * 
	 * @param top
	 * @param left
	 */
	public void setTextPadding(int top, int left) {
		this.mTextPaddingLeft = left;
		this.mTextPaddingTop = top;
	}

	/**
	 * 调整进图背景图的位置 初始位置为进度条正上方、偏左一半
	 * 
	 * @param top
	 * @param left
	 */
	public void setImageOffset(int top, int left) {
		this.mImageOffsetLeft = left;
		this.mImageOffsetTop = top;
	}

	private int getBubbleBitmapWidth() {
		return (int) Math.ceil(mBubbleWidth);
	}

	private int getBubbleBitmapHeigh() {
		return (int) Math.ceil(mBubbleHeight);
	}

	private float getTextHei() {
		FontMetrics fm = mTextPaint.getFontMetrics();
		return (float) Math.ceil(fm.descent - fm.top) + 2;
	}

	public int getTextPaddingLeft() {
		return mTextPaddingLeft;
	}

	public int getTextpaddingTop() {
		return mTextPaddingTop;
	}

	public int getImageOffsetLeft() {
		return mImageOffsetLeft;
	}

	public int getImageOffsetTop() {
		return mImageOffsetTop;
	}

	public float getTextsize() {
		return mTtextSize;
	}

	/**
	 * 
	 * 添加标签
	 * @param postion seekbar的progress
	 */
	public void addMark(int postion){
		mMarks.add(new Mark(postion));
	}
	
	private float mTouchDownX,mTouchDownY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
            return false;
        }
		
		
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	mTouchDownX = event.getX();
            	mTouchDownY = event.getY();
            	return true;
                
            case MotionEvent.ACTION_UP:
                
            	if(mTouchDownX == event.getX() && mTouchDownY == event.getY()){
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int selectedIndex = -1;
                    for (int i = 0; i < mMarks.size(); i++) {
                    	float px = mMarks.get(i).left;
                    	float py = mMarks.get(i).top;
            			if(mMarks.get(i).contains(x, y)){
            				selectedIndex = i;
            				break;
            			}
            		}
                    if(selectedIndex != -1){
                    	setProgress(mMarks.get(selectedIndex).postion);
                    	invalidate();
                    }
                    return true;
            	}
        		
                break;

        }
        return super.onTouchEvent(event);
		
	}
	
	
	

	
}
