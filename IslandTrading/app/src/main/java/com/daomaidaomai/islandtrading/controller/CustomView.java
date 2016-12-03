package com.daomaidaomai.islandtrading.controller;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.daomaidaomai.islandtrading.R;

public class CustomView extends View {

	private Paint mbackPaint;
	private Paint mForWordPaint;
	private Paint mTextPaint;
	private float mProgress = 0;
	private float mMax = 100;
	private int width;
	private int height;
	

	private float mStokeWidth = 20;
	private float mRadius = 140;
	private RectF mRectf;
	public CustomView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public CustomView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		if(attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CustomView);
			mRadius = a.getDimension(R.styleable.CustomView_radius, 50);//�����Ǹ�����200�ǻ�ȡ������ʱ��ȥ�õ�
			mStokeWidth = a.getDimension(R.styleable.CustomView_stroke_width, 5);
		}
		init();
	}
	
	/**
	 * �������λ�õ� 
	 * 
	 */
	private void initRect(){
		if(mRectf == null) {
			mRectf = new RectF();
			int viewSize = (int) (mRadius*2);
			int left = (width - viewSize) / 2;//��ߵ�����
			int right = left + viewSize;
			int top = (height - viewSize) / 2;
			int bottom = top + viewSize;
			mRectf.set(left, top, right, bottom);//left���Ǿ��ξ�����ߵ�X��    top���Ǿ��ξ����ϱߵ�Y��   right���Ǿ��ξ����ұߵ�X��    bottom���Ǿ��ξ����±ߵ�Y��
		}
		
	}
	private void init() {
		mbackPaint = new Paint();
		mbackPaint.setColor(Color.WHITE);
		mbackPaint.setAntiAlias(true);
		mbackPaint.setStyle(Paint.Style.STROKE);
		mbackPaint.setStrokeWidth(mStokeWidth);
		
		mForWordPaint = new Paint();
		mForWordPaint.setColor(0xFF66C796);
		mForWordPaint.setAntiAlias(true);
		mForWordPaint.setStyle(Paint.Style.STROKE);
		mForWordPaint.setStrokeWidth(mStokeWidth); 
		
		mTextPaint = new Paint();
		mTextPaint.setColor(0xFF66C796);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(50);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		super.onDraw(canvas);
		initRect();
		
		float angle = mProgress / (float)mMax * 360;
		
		canvas.drawCircle(width / 2, height / 2, mRadius, mbackPaint);//cx��Բ�ĵ�x���ꡣ cy��Բ�ĵ�y���ꡣ

//		oval :ָ��Բ������������������
//		startAngle: Բ����ʼ�Ƕȣ���λΪ�ȡ�
//		sweepAngle: Բ��ɨ���ĽǶȣ�˳ʱ�뷽�򣬵�λΪ�ȡ�
//		useCenter: ���ΪTrueʱ���ڻ���Բ��ʱ��Բ�İ������ڣ�ͨ�������������Ρ�
//		paint: ����Բ���Ļ������ԣ�����ɫ���Ƿ����ȡ�
		canvas.drawArc(mRectf, -90, angle, false, mForWordPaint);
		
		//mXĬ�����ַ������������Ļ��λ�ã����������
		//paint.setTextAlign(Paint.Align.CENTER);
		//�Ǿ����ַ������ģ�mY��ָ������ַ�baseline����Ļ�ϵ�λ�á�
		canvas.drawText(mProgress+"%", width / 2, height / 2+10  , mTextPaint);
		if(mProgress < mMax) {
			mProgress += 2;
			
			invalidate();
		}
		
		
		
		
	}
	
	public void setYuanProgress(int a) {
		mProgress = (float)a;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getRealSize(widthMeasureSpec);//�õ�����view�Ŀ�� Ҳ���������ռ�Ŀ��
		height = getRealSize(heightMeasureSpec);//�߶�
		
		setMeasuredDimension(width, height);//����
	}
	
	public int getRealSize(int measure) {
		int result = -1;
		
		int mode = MeasureSpec.getMode(measure);
		int size = MeasureSpec.getSize(measure);
		
		if(mode == MeasureSpec.AT_MOST || size == MeasureSpec.UNSPECIFIED) {
			result = (int) (mRadius*2 + mStokeWidth);//�뾶*2+���ο�ȵĻ���������������ʾ��ȴ�С
		} else {
			result = size;
		}
		return result;
	}
	
}
