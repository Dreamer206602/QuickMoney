package com.cwp.cmoneycharge;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.cwp.chart.ChartProp;
import com.cwp.chart.listener.ChartPropChangeListener;
import com.cwp.chart.fragment.MenuFragment;
import com.cwp.chart.widget.MyButton;
import com.cwp.chart.widget.PieView;
import com.cwp.chart.manager.SystemBarTintManager;
import com.cwp.pattern.UnlockGesturePasswordActivity;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.KindData;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class PayChart extends SlidingActivity implements OnClickListener {
	DialogShowUtil dialogShowUtil = new DialogShowUtil(this, this, null, null,
			null);
	// ���岼�ֶ���
	private FrameLayout friendfeedFl, myfeedFl, homeFl, moreFl;

	// ����ͼƬ�������
	private ImageView friendfeedIv, myfeedIv, homeIv, moreIv;

	// ���尴ťͼƬ���
	private ImageView toggleImageView, plusImageView;

	// ����PopupWindow
	private PopupWindow popWindow;
	private LinearLayout popWinLayout;

	// ����pop���
	private LinearLayout pop_voiceView;
	private LinearLayout pop_quickView;

	private int[] itemColor;// ѡ����ɫ
	private String[] itemText;// ѡ������
	private PieView pieView;
	private float surfacViewWidth = 0;
	private float surfacViewHeight = 0;
	private MyButton myButton;
	LinearLayout piechart, pdataselect;
	private TextView textView;
	private TextView textView2, example_center;
	private ImageView example_left, example_left2, example_right;
	Button anytime;
	String date1 = "", date2 = "", dday, dmonth;
	Spinner year, month, day, yeare, monthe, daye;// �����ϵ�����ʱ��

	List<String> yearlist;
	static String amount = "";
	Time time;// ��ȡ��ǰʱ��
	int defaultMonth, defaultDay;
	int defaultYear;
	Intent intentr;
	int userid;
	int type;
	String datatype = "pay";
	Adapter adapter;

	PayDAO payDAO;
	IncomeDAO incomeDAO;
	List<KindData> KindDatap;
	PtypeDAO ptypeDAO;
	ItypeDAO itypeDAO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		SysApplication.getInstance().addActivity(this); // �����ٶ��������this
		setContentView(R.layout.paychart);
		// set the Behind View
		setBehindContentView(R.layout.frame_menu);

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.fragment2);

		payDAO = new PayDAO(PayChart.this);
		KindDatap = new ArrayList<KindData>();
		ptypeDAO = new PtypeDAO(PayChart.this);
		incomeDAO = new IncomeDAO(PayChart.this);
		itypeDAO = new ItypeDAO(PayChart.this);

		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		MenuFragment menuFragment = new MenuFragment();
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setShadowWidth(50);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(260);
		sm.setFadeDegree(0.4f);
		// ����slding menu�ļ�������ģʽ
		// TOUCHMODE_FULLSCREEN ȫ��ģʽ����contentҳ���У����������Դ�sliding menu
		// TOUCHMODE_MARGIN ��Եģʽ����contentҳ���У�������slding ,����Ҫ����Ļ��Ե�����ſ��Դ�slding
		// menu
		// TOUCHMODE_NONE ��Ȼ�ǲ���ͨ�����ƴ���
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// ʹ�����Ϸ�icon�ɵ㣬������onOptionsItemSelected����ſ��Լ�����R.id.home
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		initView();

	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onStart() {

		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		pieView.rotateEnable();
		SharedPreferences sp = this.getSharedPreferences("preferences",
				MODE_WORLD_READABLE);
		CrashApplication myApplaction = (CrashApplication) getApplication();
		if ((myApplaction.isLocked)
				&& (sp.getString("gesturepw", "").equals("��"))) {// �ж��Ƿ���Ҫ��ת���������
			Intent intent = new Intent(this,
					UnlockGesturePasswordActivity.class);
			startActivity(intent);
		}
	}

	protected void onPause() {
		super.onPause();

		if (pieView != null) {
			pieView.rotateDisable();
		}
	}

	/**
	 * 
	 * Description:��ʼ������Ԫ�H
	 * 
	 */

	public void initView() {

		final Calendar c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		defaultYear = c.get(Calendar.YEAR);// ��ȡ���
		defaultMonth = c.get(Calendar.MONTH) + 1;// ��ȡ�·�
		defaultDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ����
		if (defaultMonth < 10) {
			dmonth = "0" + Integer.toString(defaultMonth);
		} else {
			dmonth = Integer.toString(defaultMonth);
		}
		if (defaultDay < 10) {
			dday = "0" + Integer.toString(defaultDay);
		} else {
			dday = Integer.toString(defaultDay);
		}
		date2 = Integer.toString(defaultYear) + "-" + dmonth + "-" + dday;
		date1 = Integer.toString(defaultYear) + "-01-01";

		intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		defaultMonth = intentr.getIntExtra("default", defaultMonth);
		defaultYear = intentr.getIntExtra("defaulty", defaultYear);

		pieView = (PieView) this.findViewById(R.id.lotteryView);
		// myButton = (MyButton) this.findViewById(R.id.MyBt);
		textView = (TextView) this.findViewById(R.id.MyTV);
		textView2 = (TextView) this.findViewById(R.id.MyTVbottom);
		example_left = (ImageView) findViewById(R.id.example_left);
		example_left2 = (ImageView) findViewById(R.id.example_left2);
		example_right = (ImageView) findViewById(R.id.example_right);
		example_center = (TextView) this.findViewById(R.id.example_center);
		pdataselect = (LinearLayout) findViewById(R.id.pdataselect);
		year = (Spinner) findViewById(R.id.pyear);
		month = (Spinner) findViewById(R.id.pmonth);
		day = (Spinner) findViewById(R.id.pday);
		yeare = (Spinner) findViewById(R.id.pyeare);
		monthe = (Spinner) findViewById(R.id.pmonthe);
		daye = (Spinner) findViewById(R.id.pdaye);
		yearlist = new ArrayList<String>(); // ��������б� spinner
		anytime = (Button) findViewById(R.id.panytime);

		// ������
		for (int i = 0; i <= 10; i++) {
			yearlist.add(String.valueOf(defaultYear - i));
		}
		adapter = new ArrayAdapter<String>(PayChart.this,
				android.R.layout.simple_spinner_item, yearlist);
		year.setAdapter((SpinnerAdapter) adapter);
		yeare.setAdapter((SpinnerAdapter) adapter);

		// ʵ�������ֶ���
		friendfeedFl = (FrameLayout) this.findViewById(R.id.layout_friendfeed2);
		myfeedFl = (FrameLayout) this.findViewById(R.id.layout_myfeed2);
		homeFl = (FrameLayout) this.findViewById(R.id.layout_home2);
		moreFl = (FrameLayout) this.findViewById(R.id.layout_more2);

		// ʵ����ͼƬ�������
		friendfeedIv = (ImageView) this.findViewById(R.id.image_friendfeed2);
		myfeedIv = (ImageView) this.findViewById(R.id.image_myfeed2);
		homeIv = (ImageView) this.findViewById(R.id.image_home2);
		moreIv = (ImageView) this.findViewById(R.id.image_more2);

		// ʵ������ťͼƬ���
		toggleImageView = (ImageView) this.findViewById(R.id.toggle_btn2);
		plusImageView = (ImageView) this.findViewById(R.id.plus_btn2);
		initData();

		textView.setOnClickListener(this);
		example_left.setOnClickListener(this);
		example_left2.setOnClickListener(this);
		example_right.setOnClickListener(this);
		anytime.setOnClickListener(this);

		type = intentr.getIntExtra("type", 0);// Ϊ0��ѡ�������£�Ϊ1��ѡ������ʱ��
		if (intentr.getStringExtra("datatype") != null) {
			datatype = intentr.getStringExtra("datatype");
		}
		if ((intentr.getStringExtra("date1") != null)
				&& (intentr.getStringExtra("date2") != null)) {
			date1 = intentr.getStringExtra("date1");
			date2 = intentr.getStringExtra("date2");
		}
		// Ϊ0��ѡ�������£�Ϊ1��ѡ������ʱ��
		// ��ȡ����
		if (type == 0) {
			pdataselect.setVisibility(View.GONE);
			example_left.setVisibility(View.VISIBLE);
			example_right.setVisibility(View.VISIBLE);
			example_center.setText(String.valueOf(defaultYear) + "�� - "
					+ String.valueOf(defaultMonth) + "��");
			if (datatype.equals("pay")) {
				KindDatap = payDAO.getKDataOnMonth(userid, defaultYear,
						defaultMonth);
			} else {
				KindDatap = incomeDAO.getKDataOnMonth(userid, defaultYear,
						defaultMonth);
			}

		} else {
			pdataselect.setVisibility(View.VISIBLE);
			example_left.setVisibility(View.GONE);
			example_right.setVisibility(View.GONE);
			example_center.setText(date1 + " ~ " + date2);
			if (datatype.equals("pay")) {
				KindDatap = payDAO.getKDataOnDay(userid, date1, date2);
			} else {
				KindDatap = payDAO.getKDataOnDay(userid, date1, date2);
			}
			monthe.setSelection(defaultMonth - 1); // spinner��ʾ��ǰ��
			daye.setSelection(defaultDay - 1); // spinner��ʾ��ǰ��
		}

		initItem(); // ��ʼ������

		if (!(KindDatap.size() == 0)) { // ����ȡ������ʱ
			Message msg = new Message();
			msg.obj = pieView.getCurrentChartProp();
			handler.sendMessage(msg); // ������Ϣ������UI
		}

		pieView.setChartPropChangeListener(new ChartPropChangeListener() {

			@Override
			public void getChartProp(ChartProp chartProp) {
				Message msg = new Message();
				msg.obj = chartProp;
				handler.sendMessage(msg); // ������Ϣ������UI
			}
		});

		pieView.start();

	}

	public Handler handler = new Handler() { // �����߳�
		public void handleMessage(Message msg) {
			ChartProp chartProp = (ChartProp) msg.obj;
			// myButton.setBackgroundPaintColor(chartProp.getColor());
			textView.setText(chartProp.getName());
			textView2.setText(chartProp.getName2());
			textView.setTextColor(chartProp.getColor());
		};
	};
	private LinearLayout pop_photoView;
	DecimalFormat df = new DecimalFormat("#.00");

	/**
	 * 
	 * Description:��ʼ��ת�̵���ɫ
	 * 
	 */
	public void initItem() {
		int i = 0;
		int fivecolor[] = new int[] { Color.rgb(56, 220, 244), Color.GREEN,
				Color.RED, Color.YELLOW, Color.CYAN };

		if (KindDatap.size() == 0) { // û�����ݵ����
			amount = "��������"; // ����������������ʾ����

		} else { // ��ȡ���ݵ����
			double sum = 0.00;
			for (KindData kp : KindDatap) {
				sum += kp.getAmount();// ȡ���ܺ�
				i++;
			}
			// ��ʼ������
			String names[] = new String[i];
			float percent[] = new float[i];
			String names2[] = new String[i];
			int color[] = new int[i];
			i = 0;

			for (KindData kp : KindDatap) {
				if (datatype.equals("pay")) {
					names[i] = ptypeDAO.getOneName(userid, kp.getKindname());
				} else {
					names[i] = itypeDAO.getOneName(userid, kp.getKindname());
				}
				if (i < fivecolor.length) {
					color[i] = fivecolor[i]; // ʹ���Զ�����ɫ
				} else {
					color[i] = getRandomColor(); // ʹ��������ɵ���ɫ
				}
				NumberFormat percentFormat = NumberFormat
						.getPercentInstance();
				percentFormat.setMaximumFractionDigits(2); // ���С��λ��
				// �Զ�ת���ɰٷֱ���ʾ.
				names2[i] = percentFormat.format(kp.getAmount() / sum) + ":��"
						+ df.format(kp.getAmount());
				percent[i] = (float) (kp.getAmount() / sum); // ������ռ�ٷֱ�
				i++;
			}

			amount = df.format(sum); // �����ķ���

			// ����ͼ��
			ArrayList<ChartProp> acps = pieView.createCharts(i);
			int size = acps.size();
			for (int k = 0; k < size; k++) { // �����ݴ���ͼ��
				ChartProp chartProp = acps.get(k);
				chartProp.setColor(color[k]);
				chartProp.setPercent(percent[k]);
				chartProp.setName(names[k]);
				chartProp.setName2(names2[k]);
			}
			pieView.initPercents();
		}

	}

	private int getRandomColor() {// �ֱ����RBG��ֵ
		Random random = new Random();
		int R = random.nextInt(255);
		int G = random.nextInt(255);
		int B = random.nextInt(255);
		return Color.rgb(R, G, B);
	}

	public static String getamount() {
		return amount;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_friendfeed2:
			clickSwitchBtn("1");
			break;
		// ���������ذ�ť
		case R.id.layout_myfeed2:
			clickSwitchBtn("2");
			break;
		// ����ҵĿռ䰴ť
		case R.id.layout_home2:
			clickSwitchBtn("3");
			break;
		// ������ఴť
		case R.id.layout_more2:
			clickSwitchBtn("4");
			break;
		// ����м䰴ť
		case R.id.toggle_btn2:
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
		case R.id.panytime:
			getAnyDate();
			Intent intentp = new Intent(PayChart.this, PayChart.class);
			intentp.putExtra("type", 1);
			intentp.putExtra("datatype", datatype);
			intentp.putExtra("date1", date1);
			intentp.putExtra("date2", date2);
			intentp.putExtra("cwp.id", userid);
			startActivity(intentp);
			break;
		case R.id.example_left2:
			toggle();
			break;
		case R.id.example_left: // ��һ���µİ���
			if (defaultMonth != 1)
				defaultMonth = defaultMonth - 1;
			else {
				defaultMonth = 12;
				defaultYear = defaultYear - 1;
			}
			Intent intentl = new Intent(PayChart.this, PayChart.class);
			intentl.putExtra("type", 0);
			intentl.putExtra("defaulty", defaultYear);
			intentl.putExtra("default", defaultMonth);
			intentl.putExtra("datatype", datatype);
			intentl.putExtra("cwp.id", userid);
			startActivity(intentl);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			this.finish();
			break;
		case R.id.example_right: // ��һ���µİ���
			if (defaultMonth != 12)
				defaultMonth = defaultMonth + 1;
			else {
				defaultMonth = 1;
				defaultYear = defaultYear + 1;
			}
			Intent intentr = new Intent(PayChart.this, PayChart.class);
			intentr.putExtra("type", 0);
			intentr.putExtra("defaulty", defaultYear);
			intentr.putExtra("default", defaultMonth);
			intentr.putExtra("cwp.id", userid);
			intentr.putExtra("datatype", datatype);
			startActivity(intentr);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			this.finish();
			break;
		}
	}

	private void clickPop_photoViewBtn() {
		Intent intent = new Intent(PayChart.this, AddPay.class);// ����Intent����
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.photo", "");// ���ô�������
		startActivity(intent);
	}

	private void clickSwitchBtn(String i) {
		Intent intent = new Intent(PayChart.this, MainActivity.class);
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.Fragment", i);// ���ô�������
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			// toggle���ǳ����Զ��ж��Ǵ򿪻��ǹر�
			toggle();
			// getSlidingMenu().showMenu();// show menu
			// getSlidingMenu().showContent();//show content
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ʾPopupWindow�����˵�
	 */
	private void showPopupWindow(View parent) {
		DisplayMetrics dm = parent.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		// System.out
		// .println("����豸w_screen��" + w_screen + " h_screen��" + h_screen);
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this
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

	private void clickPop_voiceBtn() {

		Intent intent = new Intent(PayChart.this, AddPay.class);// ����Intent����
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.voice", "");// ���ô�������
		startActivity(intent);
	}

	private void clickPop_quickBtn() {

		Intent intent = new Intent(PayChart.this, AddPay.class);// ����Intent����
		intent.putExtra("cwp.id", userid);
		startActivity(intent);
	}

	private void initData() {
		// �����ֶ������ü���
		friendfeedFl.setOnClickListener(this);
		myfeedFl.setOnClickListener(this);
		homeFl.setOnClickListener(this);
		moreFl.setOnClickListener(this);

		// ����ťͼƬ���ü���
		toggleImageView.setOnClickListener(this);

		myfeedFl.setSelected(true);
		myfeedIv.setSelected(true);
	}

	public void getAnyDate() {// ��������ʱ��
		date1 = year.getSelectedItem().toString() + "-"
				+ month.getSelectedItem().toString() + "-"
				+ day.getSelectedItem().toString();
		date2 = yeare.getSelectedItem().toString() + "-"
				+ monthe.getSelectedItem().toString() + "-"
				+ daye.getSelectedItem().toString();
		if (Integer.parseInt(year.getSelectedItem().toString()) > Integer
				.parseInt(yeare.getSelectedItem().toString())) {
			change();
		} else {
			if (Integer.parseInt(month.getSelectedItem().toString()) > Integer
					.parseInt(monthe.getSelectedItem().toString())) {
				change();
			} else {
				if (Integer.parseInt(day.getSelectedItem().toString()) > Integer
						.parseInt(daye.getSelectedItem().toString())) {
					change();
				}
			}
		}
	}

	private void change() {
		String temp = date1;
		date1 = date2;
		date2 = temp;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			dialogShowUtil.dialogShow("shake", "quit", "", "");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}