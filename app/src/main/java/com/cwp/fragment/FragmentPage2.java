package com.cwp.fragment;

import com.cwp.cmoneycharge.activity.AddPay;
import com.cwp.cmoneycharge.activity.MainActivity;
import com.cwp.cmoneycharge.activity.PayChart;
import com.cwp.cmoneycharge.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class FragmentPage2 extends BaseFragment implements OnClickListener {
	static int userid;
	private static FragmentPage1 fragmentPage1;
	private FragmentPage2 fragmentPage2;
	private static FragmentPage3 fragmentPage3;
	private static FragmentPage4 fragmentPage4;
	// ���岼�ֶ���
	private static FrameLayout friendfeedFl;
	private static FrameLayout myfeedFl;
	private static FrameLayout homeFl;
	private static FrameLayout moreFl;

	// ����ͼƬ�������
	private static ImageView friendfeedIv;
	private static ImageView myfeedIv;
	private static ImageView homeIv;
	private static ImageView moreIv;

	// ���尴ťͼƬ���
	private ImageView toggleImageView, plusImageView;

	// ����PopupWindow
	private PopupWindow popWindow;
	private LinearLayout popWinLayout;

	// ����pop���
	private LinearLayout pop_voiceView;
	private LinearLayout pop_quickView;

	int value = 0;
	private LinearLayout pop_photoView;

	static FragmentActivity act;

	public FragmentPage2(FragmentActivity activity) {
		this.act = activity;
	}

	public FragmentPage2() {
	}

	@Override
	public void filngtonext() {

	}

	@Override
	public void filngtonpre() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_2, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intentr = getActivity().getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);

		initView();
		initData();

		switch (value) {
		case 0:
			clickFriendfeedBtn();
			break;
		case 1:
			clickFriendfeedBtn();
			break;
		case 2:
			clickMyfeedBtn();
			break;
		case 3:
			clickHomeBtn();
			break;
		case 4:
			clickMoreBtn();
			break;
		}
	}

	/**
	 * ��ʼ�����
	 */
	public void initView() {
		value = MainActivity.getValueFM();
		// ʵ�������ֶ���
		friendfeedFl = (FrameLayout) getActivity().findViewById(
				R.id.layout_friendfeed);
		myfeedFl = (FrameLayout) getActivity().findViewById(R.id.layout_myfeed);
		homeFl = (FrameLayout) getActivity().findViewById(R.id.layout_home);
		moreFl = (FrameLayout) getActivity().findViewById(R.id.layout_more);

		// ʵ����ͼƬ�������
		friendfeedIv = (ImageView) getActivity().findViewById(
				R.id.image_friendfeed);
		myfeedIv = (ImageView) getActivity().findViewById(R.id.image_myfeed);
		homeIv = (ImageView) getActivity().findViewById(R.id.image_home);
		moreIv = (ImageView) getActivity().findViewById(R.id.image_more);

		// ʵ������ťͼƬ���
		toggleImageView = (ImageView) getActivity().findViewById(
				R.id.toggle_btn);
		plusImageView = (ImageView) getActivity().findViewById(R.id.plus_btn);
	}

	private void initData() {
		// �����ֶ������ü���
		friendfeedFl.setOnClickListener(this);
		myfeedFl.setOnClickListener(this);
		homeFl.setOnClickListener(this);
		moreFl.setOnClickListener(this);

		// ����ťͼƬ���ü���
		toggleImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// System.out.println("�Ұ���" + v.getId());
		switch (v.getId()) {
		// �����̬��ť
		case R.id.layout_friendfeed:
			clickFriendfeedBtn();
			break;
		// ���������ذ�ť
		case R.id.layout_myfeed:
			clickMyfeedBtn();
			break;
		// ����ҵĿռ䰴ť
		case R.id.layout_home:
			clickHomeBtn();
			break;
		// ������ఴť
		case R.id.layout_more:
			clickMoreBtn();
			break;
		// ����м䰴ť
		case R.id.toggle_btn:
			clickToggleBtn();
			break;
		// ����м䰴ť
		case R.id.pop_voice:
			clickPop_voiceBtn();
			break;
		case R.id.pop_quick:
			clickPop_quickBtn();
			break;
		case R.id.pop_photo:
			clickPop_photoViewBtn();
			break;
		}
	}

	private void clickPop_photoViewBtn() {
		Intent intent = new Intent(getActivity(), AddPay.class);// ����Intent����
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.photo", "");// ���ô�������
		startActivity(intent);
	}

	/**
	 * ��ʾPopupWindow�����˵�
	 */
	private void showPopupWindow(View parent) {
		DisplayMetrics dm = parent.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		// System.out.println("����豸w_screen��" + w_screen + " h_screen��" +
		// h_screen);
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
			popWinLayout = (LinearLayout) view.findViewById(R.id.popwindow);
			// ����һ��PopuWidow����
			float radiowith = w_screen / 480.0f;
			float radioheight = h_screen / 800.0f;
			popWindow = new PopupWindow(view,
					(int) (popWinLayout.getLayoutParams().width), h_screen / 4);
		}
		// ʹ��ۼ� ��Ҫ������˵���ؼ����¼��ͱ���Ҫ���ô˷���
		popWindow.setFocusable(true);

		pop_voiceView = (LinearLayout) popWinLayout
				.findViewById(R.id.pop_voice);
		pop_quickView = (LinearLayout) popWinLayout
				.findViewById(R.id.pop_quick);
		pop_photoView = (LinearLayout) popWinLayout
				.findViewById(R.id.pop_photo);
		pop_voiceView.setOnClickListener(this);
		pop_quickView.setOnClickListener(this);
		pop_photoView.setOnClickListener(this);

		// ����������������ʧ
		popWindow.setOutsideTouchable(true);
		// ���ñ����������Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		// ���ò˵���ʾ��λ��

		int xPos = (w_screen - popWinLayout.getLayoutParams().width) / 2;
		popWindow.showAsDropDown(parent, xPos, 12);
		// popWindow.showAsDropDown(parent, Gravity.CENTER, 0);

		// �����˵��Ĺر��¼�
		popWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// �ı���ʾ�İ�ťͼƬΪ����״̬
				changeButtonImage();
			}
		});

		// ���������¼�
		popWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					// �ı���ʾ�İ�ťͼƬΪ����״̬
					changeButtonImage();
				}

				return false;
			}
		});
	}

	/**
	 * ����ˡ���̬����ť
	 */
	public static void clickFriendfeedBtn() {
		// ʵ����Fragmentҳ��
		fragmentPage1 = new FragmentPage1();
		// �õ�Fragment���������
		FragmentTransaction fragmentTransaction;
		fragmentTransaction = act.getSupportFragmentManager()
				.beginTransaction();

		// �滻��ǰ��ҳ��
		fragmentTransaction.replace(R.id.frame_content, fragmentPage1);
		// ��������ύ
		fragmentTransaction.commit();

		friendfeedFl.setSelected(true);
		friendfeedIv.setSelected(true);

		myfeedFl.setSelected(false);
		myfeedIv.setSelected(false);

		homeFl.setSelected(false);
		homeIv.setSelected(false);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	/**
	 * ����ˡ�������ء���ť
	 */
	public static void clickMyfeedBtn() {

		// Intent intentr = new Intent(getActivity(), PayData.class);
		Intent intentr = new Intent(act, PayChart.class);
		intentr.putExtra("cwp.id", userid);
		intentr.putExtra("type", 0);
		act.startActivity(intentr);
		act.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

	}

	/**
	 * ����ˡ��ҵĿռ䡱��ť
	 */
	public static void clickHomeBtn() {
		// ʵ����Fragmentҳ��
		fragmentPage3 = new FragmentPage3();
		// �õ�Fragment���������
		FragmentTransaction fragmentTransaction = act
				.getSupportFragmentManager().beginTransaction();
		// �滻��ǰ��ҳ��
		fragmentTransaction.replace(R.id.frame_content, fragmentPage3);
		// ��������ύ
		fragmentTransaction.commit();

		friendfeedFl.setSelected(false);
		friendfeedIv.setSelected(false);

		myfeedFl.setSelected(false);
		myfeedIv.setSelected(false);

		homeFl.setSelected(true);
		homeIv.setSelected(true);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	/**
	 * ����ˡ����ࡱ��ť
	 */
	public static void clickMoreBtn() {
		// ʵ����Fragmentҳ��
		fragmentPage4 = new FragmentPage4();
		// �õ�Fragment���������
		FragmentTransaction fragmentTransaction = act
				.getSupportFragmentManager().beginTransaction();
		// �滻��ǰ��ҳ��
		fragmentTransaction.replace(R.id.frame_content, fragmentPage4);
		// ��������ύ
		fragmentTransaction.commit();

		friendfeedFl.setSelected(false);
		friendfeedIv.setSelected(false);

		myfeedFl.setSelected(false);
		myfeedIv.setSelected(false);

		homeFl.setSelected(false);
		homeIv.setSelected(false);

		moreFl.setSelected(true);
		moreIv.setSelected(true);
	}

	private void clickPop_voiceBtn() {

		Intent intent = new Intent(getActivity(), AddPay.class);// ����Intent����
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.voice", "");// ���ô�������
		startActivity(intent);
	}

	private void clickPop_quickBtn() {

		Intent intent = new Intent(getActivity(), AddPay.class);// ����Intent����
		intent.putExtra("cwp.id", userid);
		intent.putExtra("keyboard", "true");
		startActivity(intent);
	}

	/**
	 * ������м䰴ť
	 */
	private void clickToggleBtn() {
		showPopupWindow(plusImageView);
		// �ı䰴ť��ʾ��ͼƬΪ����ʱ��״̬
		plusImageView.setImageResource(R.drawable.toolbar_plusback);
		toggleImageView.setImageResource(R.drawable.toolbar_btn_pressed);
	}

	/**
	 * �ı���ʾ�İ�ťͼƬΪ����״̬
	 */
	private void changeButtonImage() {
		plusImageView.setImageResource(R.drawable.toolbar_plus);
		toggleImageView.setImageResource(R.drawable.toolbar_btn_normal);
	}

}
