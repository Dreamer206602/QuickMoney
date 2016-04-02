package com.cwp.chart.widget;

import java.util.ArrayList;

import com.cwp.chart.ChartProp;
import com.cwp.chart.listener.ChartPropChangeListener;
import com.cwp.chart.PiewController;
import com.cwp.cmoneycharge.activity.PayChart;
import com.cwp.cmoneycharge.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import android.graphics.Point;
import android.graphics.RectF;

import android.graphics.Paint;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.view.View;

public class PieView extends View implements View.OnTouchListener, Runnable {

	private float m_x = 0;
	private float m_y = 0;

	private static ArrayList<ChartProp> mChartProps;
	private float screenHight, screenWidth;// ��Ļ�Ŀ�͸�

	private float radius;// ����Բ�İ뾶
	private float startAngle;// ��?�Ƕ�
	private float sweepAngle = 0.0f; // ɨ��Ľǎ�
	private int itemCount;// ѡ�����
	private float startSpeed = 0;

	private boolean firstEnter = true; // �����Ƿ��Ѿ��T?
	private boolean rotateEnabled = false;
	private boolean tounched = false;
	private boolean done = false;
	private Paint mPaint;

	private Paint textPaint;
	private Canvas mCanvas;

	private ChartPropChangeListener listener;
	private static ChartProp mChartProp = null;
	private static ChartProp tChartProp = null;
	private static float[] percents;

	private Point centerPoint;
	private Bitmap myBg;
	private String amount;

	private Thread myThread;

	public void setBackgroundPaintColor(int color) {

		invalidate();
	}

	private void writeLog(String string) {
		Log.e("caizh", string);
	}

	public void destory() {
		// myBg.recycle();
		done = true;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.draw(canvas);

		if (firstEnter) {
			firstInit();
		} else {

			if (!tounched) {

				float middleAngle = ((mChartProp.getEndAngle() + mChartProp
						.getStartAngle()) / 2);
				float distance = (middleAngle - getBorderAngle());

				startAngle -= (distance / 2);

				// startAngle=()
			}

		}

		mCanvas = canvas;

		float f1 = centerPoint.x;
		float f2 = centerPoint.y;
		// ��䱳��ܵ
		/*
		 * mCanvas.drawColor(0xff639EC3); mCanvas.save();
		 */

		// *********************************ȷ�����D?��*************************************
		float f3 = f1 - radius;// X��- ��
		float f4 = f2 - radius; // Y��- د
		float f5 = f1 + radius; // X��- ��
		float f6 = f2 + radius; // Y��- د
		RectF rectF = new RectF(f3, f4, f5, f6);

		// *********************************��ÿ���������ɫ�}********************************
		drawItem(rectF);
		// *********************************�����Ͻ����Բ������*******************************

		drawableCenterCircle(f1, f2);
		// ����Canvas������Ⱦ��ǰͼ��

		if (!(mChartProp == tChartProp)) {
			listener.getChartProp(mChartProp);
			tChartProp = mChartProp;
		}

	}

	public PieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PieView(Context context) {
		super(context);
		init();
	}

	private void init() {

		myThread = new Thread(this);
		// ����د?�µ�SurfaceHolder�߲������������Ϊ��Ļص�(callback)

		mChartProps = new ArrayList<ChartProp>();

		// ͼ�񻭱�
		mPaint = new Paint();

		mPaint.setAntiAlias(true);
		// ���ֻ���
		textPaint = new Paint();
		textPaint.setTextSize(22);
		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);

		// �뾶

		startAngle = 90f;

		myBg = BitmapFactory.decodeResource(getResources(),
				R.drawable.mask_piechartformymoney);

		this.setOnTouchListener(this);

	}

	public void start() {
		myThread.start();
	}

	@Override
	public void run() {

		while (!done) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (rotateEnabled) {

				postInvalidate();
			}
		}

	}

	private Bitmap createBitmap(Bitmap b, float x, float y) {
		int w = b.getWidth();
		int h = b.getHeight();

		float sx = (float) x / w;
		float sy = (float) y / h;
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
		return resizeBmp;
	}

	/**
	 * create charts' property ������״ͼ������
	 * 
	 * @param chartsNum
	 *            charts' number ��״ͼ�ĸ���
	 * @return charts' property's list ��״ͼ���Ե�list
	 */
	public ArrayList<ChartProp> createCharts(int itemCount) {
		this.itemCount = itemCount;
		percents = new float[itemCount];
		mChartProps.clear();
		createChartProp(itemCount);

		return mChartProps;
	}

	// ���³�ʼ��
	public void reInit() {
		startAngle = 90f;
		firstEnter = true; // �����Ƿ��Ѿ��T?
	}

	// ��һ�β�����иı��ĳ�ʼ������
	public void initPercents() {

		for (int i = 0; i < itemCount; i++) {

			percents[i] = mChartProps.get(i).getPercent();
			mChartProps.get(i).setPercent(0.01f);

		}

	}

	public void setChartPropChangeListener(ChartPropChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * actually create chartProp objects. �����������{?����
	 * 
	 * @param chartsNum
	 *            charts' number ��״ͼ�ĸ���
	 */
	private void createChartProp(int chartsNum) {
		for (int i = 0; i < chartsNum; i++) {
			ChartProp chartProp = new ChartProp(this);
			chartProp.setId(i);
			mChartProps.add(chartProp);
		}
	}

	public ChartProp createNullChartProp() {
		mChartProps.clear();
		ChartProp chartProp = new ChartProp(this);
		mChartProps.add(chartProp);
		return chartProp;
	}

	private void initResoure() {
		float y = myBg.getHeight();
		float x = myBg.getWidth();
		float r;

		if (screenHight > screenWidth) {
			r = screenWidth;
			myBg = createBitmap(myBg, screenWidth, (screenWidth * y) / x);
		} else {
			r = screenHight;
			myBg = createBitmap(myBg, screenHight, (screenHight * y) / x);
		}

		centerPoint = new Point();

		centerPoint.x = (int) (r / 2);
		centerPoint.y = (int) ((r * 19) / 32);

		radius = (centerPoint.x * 0.8843f);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// �߶�
		screenHight = (float) PiewController.measureWidth(heightMeasureSpec);

		screenWidth = (float) PiewController.measureHeight(widthMeasureSpec);
		Log.e("caizh", "screenWidth01=" + screenWidth);
		initResoure();
		setMeasuredDimension((int) screenWidth, (int) screenHight);
	}

	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// �õ�ģʽ
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// �õ��ߴ�

		switch (widthMode) {
		/**
		 * mode�������������ȡֵ�ֱ�ΪMeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
		 * MeasureSpec.AT_MOST?
		 * 
		 * 
		 * MeasureSpec.EXACTLY�Ǿ�ȷ�ߴ磬
		 * �����ǽ��ؼ���layout_width��layout_heightָ��Ϊ������ֵʱ��andorid
		 * :layout_width="50dip"������ΪFILL_PARENT�ǣ����ǿؼ���С�Ѿ�ȷ������������Ǿ�ȷ�ߴ�?
		 * 
		 * 
		 * MeasureSpec.AT_MOST�����ߴ磬
		 * ���ؼ���layout_width��layout_heightָ��ΪWRAP_CONTENT�J
		 * ���ؼ���Сһ����������ӿռ�����ݽ��б仯����ʱ�ؼ��ߴ�ֻҪ������ؼ�����Ėc?�ߴ缴��
		 * ����ˣ���ʱ��mode��AT_MOST��size����˸��ؼ���������ߴ�?
		 * 
		 * 
		 * MeasureSpec.UNSPECIFIED��δָ���ߴ磬����������࣬د?���Ǹ��ؼ���AdapterView��
		 * ͨ��measure���������ģʽ?
		 */
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = widthSize;
			break;
		}
		return result;
	}

	private int measureHeight(int pHeightMeasureSpec) {
		int result = 0;

		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

		switch (heightMode) {
		case MeasureSpec.AT_MOST:
		case MeasureSpec.EXACTLY:
			result = heightSize;
			break;
		}
		return result;
	}

	private void firstInit() {
		startSpeed += 0.05f;
		if (startSpeed >= 1) {
			startSpeed = 0.9f;
		}
		float total = 0f;
		if (!mChartProps.isEmpty()) {
			for (int i = 0; i < itemCount; i++) {
				if (i < mChartProps.size()) {
					float percent = mChartProps.get(i).getPercent();
					// writeLog("percent="+percent);
					float distancePercent = (percents[i] - percent);

					if (distancePercent < 0.0001) {
						mChartProps.get(i).setPercent(percents[i]);

					} else {

						mChartProps.get(i).setPercent(
								percent + (distancePercent * startSpeed));
					}

					total = total + mChartProps.get(i).getPercent();
				}
			}
		}

		if (total >= 1) {
			firstEnter = false;
		}

	}

	public void getCurrentChartProp(ChartProp chartProp, float f) {

		if ((chartProp.getStartAngle() <= f) && (chartProp.getEndAngle() > f)) {

			mChartProp = chartProp;

		}
	}

	public void drawableCenterCircle(float x, float y) {
		/*
		 * Paint centerCirclePaint = new Paint();
		 * centerCirclePaint.setColor(Color.BLACK);
		 * 
		 * centerCirclePaint.setAlpha(150); mCanvas.drawCircle(x, y, radius / 3,
		 * centerCirclePaint);
		 */

		// Paint localPaint = new Paint();
		// ����ȡ����Ч��
		// localPaint.setAntiAlias(true);
		// ���ΪԲ�x
		/*
		 * localPaint.setStyle(Paint.Style.STROKE); // Բ�����
		 * localPaint.setStrokeWidth(circleRadius);
		 */
		// Բ������

		mCanvas.drawBitmap(myBg, 0, 0, null);
		mCanvas.save();

		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(x / 10);

		mCanvas.drawText("�ܼ�", x, y - x / 12, textPaint);
		mCanvas.drawText(PayChart.getamount(), x, y + x / 10, textPaint); // �ܼ�
		mCanvas.save();
	}

	private float getBorderAngle() {
		float borderAngle;
		int circleCount = 0;
		if (startAngle >= 0) {
			circleCount = (int) (startAngle / 360);
			if ((startAngle % 360) > (90)) {
				borderAngle = (float) (90 + 360 * (circleCount + 1));
			} else {
				borderAngle = (float) (90 + 360 * circleCount);
			}

		} else {
			circleCount = (int) (startAngle / 360);
			if ((startAngle % 360) < (-270)) {
				borderAngle = (float) (90 + 360 * (circleCount - 1));
			} else {
				borderAngle = (float) (90 + 360 * circleCount);
			}

		}

		return borderAngle;

	}

	public ChartProp getCurrentChartProp() {
		if (mChartProp != null) {
			return mChartProp;
		}
		float temp = startAngle;
		float borderAngle = getBorderAngle();

		for (int i = 0; i < itemCount; i++) {

			sweepAngle = mChartProps.get(i).getSweepAngle();
			mChartProps.get(i).setStartAngle(temp);

			temp += sweepAngle;
			mChartProps.get(i).setEndAngle(temp);

			getCurrentChartProp(mChartProps.get(i), borderAngle);
		}

		return mChartProp;
	}

	// *********************************��ÿ���ȏ�********************************
	public void drawItem(RectF localRectf) {
		float temp = startAngle;
		float borderAngle = getBorderAngle();
		// System.out.println("itemCount" + itemCount);
		if (!mChartProps.isEmpty()) {
			if (itemCount > 0) {
				for (int i = 0; i < itemCount; i++) {
					if (i < mChartProps.size()) {
						mPaint.setColor(mChartProps.get(i).getColor());
						// startAngleΪÿ���ƶ��ĽǶȴ�С
						sweepAngle = mChartProps.get(i).getSweepAngle();
						mChartProps.get(i).setStartAngle(temp);

						/*
						 * oval��Բ�����ڵ���Բ����?startAngle��Բ������ʼ�Ƕȡ�sweepAngle��Բ���ĽǶ�?
						 * useCenter��
						 * �Ƿ���ʾ�뾶���ߣ�true��ʾ��ʾԲ����Բ�ĵİ뾶���ߣ�false��ʾ����ʾ?paint
						 * ������ʱ��?�õĻ���
						 */
						mCanvas.drawArc(localRectf, temp, sweepAngle, true,
								mPaint);
						// mCanvas.drawText(localRectf, temp, sweepAngle,
						// mChartProps.get(i).getName());
						mCanvas.save();

						temp += sweepAngle;
						mChartProps.get(i).setEndAngle(temp);

						getCurrentChartProp(mChartProps.get(i), borderAngle);
					}

				}
			}
		}

	}

	public boolean isRotateEnabled() {
		return rotateEnabled;
	}

	public void setRotateEnabled(boolean rotateEnabled) {
		this.rotateEnabled = rotateEnabled;
	}

	public void rotateEnable() {
		rotateEnabled = true;
	}

	public void rotateDisable() {
		rotateEnabled = false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		boolean enable = true;
		float x = centerPoint.x;
		float y = centerPoint.y;
		float y1 = event.getY();
		float x1 = event.getX();

		float x2 = v.getLeft();
		float y2 = v.getTop();

		float x3 = x1 - x2;
		float y3 = y1 - y2;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			m_x = x3;
			m_y = y3;
			tounched = true;

		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			m_x = 0;
			m_y = 0;
			tounched = false;

		}

		float d = (float) Math.sqrt((Math.pow(Math.abs((x - x3)), 2) + Math
				.pow(Math.abs((y - y3)), 2)));
		if (d > radius) {
			m_x = 0;
			m_y = 0;
			enable = false;

		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			if ((m_x == 0) && (m_y == 0)) {

				enable = false;
			}
			if (enable) {
				float c = (float) Math
						.sqrt((Math.pow(Math.abs((m_x - x3)), 2) + Math.pow(
								Math.abs((m_y - y3)), 2)));
				float a = (float) Math
						.sqrt((Math.pow(Math.abs(m_x - x), 2) + Math.pow(
								Math.abs((m_y - y)), 2)));
				float b = (float) Math
						.sqrt((Math.pow(Math.abs(x3 - x), 2) + Math.pow(
								Math.abs((y3 - y)), 2)));

				float cosc = (float) (Math.pow(a, 2) + Math.pow(b, 2) - Math
						.pow(c, 2)) / (2 * a * b);

				float m_angle = getAngle((m_x - x), (m_y - y));

				float angle3 = getAngle((x3 - x), (y3 - y));
				if ((90 > m_angle) && (angle3 > 270)) {
					m_angle = m_angle + (float) 360;
				}
				if ((90 > angle3) && (m_angle > 270)) {
					angle3 = angle3 + (float) 360;
				}
				if (angle3 - m_angle > 0) {
					startAngle = (float) (startAngle + Math.acos(cosc) * 180
							/ Math.PI);
				} else {
					startAngle = (float) (startAngle - Math.acos(cosc) * 180
							/ Math.PI);
				}
				m_x = x3;
				m_y = y3;
			}

		}

		return true;
	}

	private float getAngle(float x, float y) {

		if ((x == 0) && (y == 0)) {
			return 0;
		}

		float angle = (float) (Math.atan(y / x) * 180 / Math.PI);

		if (x == 0) {
			if (y > 0) {
				return 90;
			} else {
				return 270;
			}
		}

		if (x > 0) {
			if (y < 0) {
				return (float) 360 + angle;
			}
		}
		if (x < 0) {
			if (y > 0) {
				return (float) 180 + angle;
			} else if (y == 0) {
				return 180;
			} else if (y < 0) {
				return (float) 180 + angle;
			}

		}

		return angle;

	}
}
