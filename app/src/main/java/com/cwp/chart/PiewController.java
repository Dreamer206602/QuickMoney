package com.cwp.chart;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class PiewController {

	/*
	 * ��ȡ�ؼ���
	 */
	public static int getWidth(View view) {
		int w = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);
		int h = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return (view.getMeasuredWidth());
	}

	/*
	 * ��ȡ�ؼ��
	 */
	public static int getHeight(View view) {
		int w = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);
		int h = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return (view.getMeasuredHeight());
	}

	/*
	 * ���ÿؼ���?��λ��X�����Ҳ��ı��ߣ�XΪ����λ�ã���ʱY���܏�
	 */
	public static void setLayoutX(View view, int x) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(x, margin.topMargin, x + margin.width,
				margin.bottomMargin);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	/*
	 * ���ÿؼ���?��λ��Y�����Ҳ��ı��ߣ�YΪ����λ�ã���ʱX���܏�
	 */
	public static void setLayoutY(View view, int y) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(margin.leftMargin, y, margin.rightMargin, y
				+ margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	/*
	 * ���ÿؼ���?��λ��YY�����Ҳ��ı��ߣ�XYΪ����λ�Z
	 */
	public static void setLayout(View view, int x, int y) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(x, y, x + margin.width, y + margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	public static int measureWidth(int pWidthMeasureSpec) {
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
		 * ���ؼ���Сһ����������ӿռ�����ݽ��б仯����ʱ�ؼ��ߴ�ֻҪ���������ؼ�����Ėc?�ߴ缴��
		 * ����ˣ���ʱ��mode��AT_MOST��size�����˸��ؼ���������ߴ�?
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

	public static int measureHeight(int pHeightMeasureSpec) {
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

	public static void mySetMargins(View view, int left, int top, int right,
			int bottom) {
		FrameLayout.LayoutParams layoutParam = (FrameLayout.LayoutParams) view
				.getLayoutParams();
		layoutParam.setMargins(left, top, right, bottom);
	}

}
