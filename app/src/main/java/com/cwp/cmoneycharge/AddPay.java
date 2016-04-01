package com.cwp.cmoneycharge;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.cwp.chart.SystemBarTintManager;
import com.cwp.cmoneycharge.Config;
import com.cwp.cmoneycharge.Constants;
import com.cwp.cmoneycharge.DigitUtil;
import com.cwp.cmoneycharge.Effectstype;
import com.cwp.cmoneycharge.KeyboardUtil;
import com.cwp.cmoneycharge.MainActivity;
import com.cwp.cmoneycharge.NiftyDialogBuilder;
import com.cwp.pattern.UnlockGesturePasswordActivity;
import com.example.testpic.Bimp;
import com.example.testpic.PublishedActivity;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.Datapicker;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPay extends Activity implements AMapLocationListener,
		OnClickListener {
	protected static final int DATE_DIALOG_ID = 0;// �������ڶԻ�����
	static String type = "pay";
	String VoiceDefault = "";
	protected static String typemode = "add";
	EditText txtMoney, txtTime, txtAddress, txtMark;// ����EditText����
	Spinner spType;// ����Spinner����
	Button btnSaveButton;// ����Button���󡰱��桱
	Button btnCancelButton;// ����Button����ȡ����
	Button btnVoice;// ����Button��������ʶ��
	int userid;
	int Selection = 0;
	Bundle bundle = null;
	String[] strInfos = null;// �����ַ�������
	String strno, strType;// ���������ַ����������ֱ�������¼��Ϣ��ź͹�������
	private FrameLayout corporation_fl, address_fl = null;
	private RadioButton rb1 = null;
	private RadioButton rb2 = null;
	ImageView left_back;

	private BaiduASRDigitalDialog mDialog = null; // �ٶ���������
	private DialogRecognitionListener mRecognitionListener;
	private int mCurrentTheme = Config.DIALOG_THEME;
	private Effectstype effect; // �Զ���Dialog
	NiftyDialogBuilder dialogBuilder = null;
	Boolean firstin = true;

	private int mYear;// ��
	private int mMonth;// ��
	private int mDay;// ��

	private ArrayAdapter<String> adapter;
	private String[] spdata;

	private LocationManagerProxy mLocationManagerProxy;// �ߵµ�ͼapi
	private Random mRandom = new Random();

	String[] number = { "һ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "ʮ" };
	String[] money = { "Ԫ", "��", "Ǯ" };
	String[] money2 = { "ʮ", "��", "ǧ", "��", "��" };
	String[] voice_pay = { "��", "��" };
	String[] voice_income = { "��", "��" };

	String[] VoiceSave = new String[6];
	static DialogShowUtil dialogShowUtil;
	PtypeDAO ptypeDAO = new PtypeDAO(AddPay.this);
	ItypeDAO itypeDAO = new ItypeDAO(AddPay.this);
	PayDAO payDAO = new PayDAO(AddPay.this);// ����PayDAO����
	IncomeDAO incomeDAO = new IncomeDAO(AddPay.this);// ����IncomeDAO����
	List<String> spdatalist, spdatalist2;
	private SystemBarTintManager mTintManager;
	private ImageView btn_loacte;
	private ImageView addphoto;
	protected String textphoto = "";
	private int incount = 0;
	private boolean keycount = true;
	private FrameLayout bottom_empty;
	private LinearLayout bottom_full;
	private KeyboardUtil keyBoard;

	public AddPay() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add);// ���ò����ļ�

		SysApplication.getInstance().addActivity(this); // �����ٶ��������this
		super.onStart();// ʵ�ֻ����еķ���

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			findViewById(R.id.top).setVisibility(View.VISIBLE);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		txtMoney = (EditText) findViewById(R.id.txtMoney);// ��ȡ����ı���
		txtTime = (EditText) findViewById(R.id.txtTime);// ��ȡʱ���ı���
		txtAddress = (EditText) findViewById(R.id.txtAddress);// ��ȡ�ص��ı���
		txtMark = (EditText) findViewById(R.id.txtMark);// ��ȡ��ע�ı���
		spType = (Spinner) findViewById(R.id.spType);// ��ȡ��������б�
		btnSaveButton = (Button) findViewById(R.id.btnSave);// ��ȡ���水ť
		btnCancelButton = (Button) findViewById(R.id.btnCancel);// ��ȡȡ����ť
		btnVoice = (Button) findViewById(R.id.btnVoice);// ��ȡ����ʶ��ť
		rb1 = (RadioButton) findViewById(R.id.payout_tab_rb);
		rb2 = (RadioButton) findViewById(R.id.income_tab_rb);
		left_back = (ImageView) findViewById(R.id.example_left3);
		btn_loacte = (ImageView) findViewById(R.id.btn_loacte);
		addphoto = (ImageView) findViewById(R.id.addphoto);
		bottom_empty = (FrameLayout) findViewById(R.id.bottom_empty);
		bottom_full = (LinearLayout) findViewById(R.id.bottom_full);

		dialogShowUtil = new DialogShowUtil(this, this, VoiceSave, type, // ��ʼ��dialog
				VoiceDefault);
		btn_loacte.setOnClickListener(this); // ��λ

		// ���ز˵�
		bottom_empty.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				keyBoard.hideKeyboard();
				bottom_empty.setVisibility(View.GONE);
				bottom_full.setVisibility(View.VISIBLE);
			}
		});

		// ���ͼƬ
		addphoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AddPay.this, PublishedActivity.class);
				intent.putExtra("cwp.id", userid);
				startActivityForResult(intent, 102);
			}
		});

		// ��ʼ����λ��ֻ�������綨λ
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
		// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
		// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
		// ����������ʱ��Ϊ-1����λֻ��һ��,
		// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�

		left_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = null;
				type = "pay";
				typemode = "add";
				if (bundle.containsKey("cwp.frament3")) {
					intent = new Intent(AddPay.this, MainActivity.class);
					intent.putExtra("cwp.Fragment", "3");// ���ô�������
				} else {
					intent = new Intent(AddPay.this, MainActivity.class);
				}
				intent.putExtra("cwp.id", userid);
				startActivity(intent);
				finish();// ����ǹؼ�
			}
		});

		mRecognitionListener = new DialogRecognitionListener() { // �ٶ�ʶ�𷵻�����

			@Override
			public void onResults(Bundle results) {
				ArrayList<String> rs = results != null ? results
						.getStringArrayList(RESULTS_RECOGNITION) : null;
				if (rs != null && rs.size() > 0) {
					Recognition(rs.get(0)); // ��ʶ�����ݴ���ʶ�𷽷�
					// Toast.makeText(AddPay.this, rs.get(0),
					// Toast.LENGTH_SHORT).show();
				}
			}
		};

		corporation_fl = (FrameLayout) findViewById(R.id.corporation_fl);
		address_fl = (FrameLayout) findViewById(R.id.address_fl);

		rb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (rb1.isChecked()) { // ֧��
					type = "pay";
				} else // ����
				{
					type = "income";
				}
				updatetype();
			}
		});

		final Calendar c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		mYear = c.get(Calendar.YEAR);// ��ȡ���
		mMonth = c.get(Calendar.MONTH);// ��ȡ�·�
		mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ����
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

	private void initData(int userid) { // ��ʼ������
		if (typemode == "add") { // ���ģʽ
			if (type == "pay") { // ֧��
				rb1.setChecked(true);
				spdatalist = ptypeDAO.getPtypeName(userid);
				txtMoney.setTextColor(Color.parseColor("#5ea98d"));
			} else { // ����
				rb2.setChecked(true);
				spdatalist = itypeDAO.getItypeName(userid);
				txtMoney.setTextColor(Color.parseColor("#ffff0000"));
			}
		} else { // �޸�ģʽ
			bottom_empty.setVisibility(View.GONE);
			bottom_full.setVisibility(View.VISIBLE);
			rb1.setOnCheckedChangeListener(null);
			btnSaveButton.setText("�޸�"); // �滻�޸İ�ť
			btnCancelButton.setText("ɾ��"); // �滻ɾ����ť
			CharSequence textreAddres;
			String textreMark;
			if (type == "pay") { // ֧��
				rb1.setChecked(true);
				rb1.setClickable(false);
				rb2.setClickable(false);
				// ѡ���б��ʼ��
				spdatalist = ptypeDAO.getPtypeName(userid);
				// ���ݱ�Ų���֧����Ϣ�����洢��Tb_pay������
				Tb_pay tb_pay = payDAO.find(userid, Integer.parseInt(strno));
				txtMoney.setText(tb_pay.getMoney2());// ��ʾ���
				txtMoney.setTextColor(Color.parseColor("#5ea98d"));
				txtTime.setText(tb_pay.getTime());// ��ʾʱ��
				Selection = tb_pay.getType() - 1;
				initphotodata(tb_pay.getPhoto());
				textreAddres = tb_pay.getAddress();
				textreMark = tb_pay.getMark();
				txtAddress.setText(textreAddres);// ��ʾ�ص�
				txtMark.setText(textreMark);// ��ʾ��ע
			} else { // ����
				// ѡ���б��ʼ��
				rb2.setChecked(true);
				rb1.setClickable(false);
				rb2.setClickable(false);
				spdatalist = itypeDAO.getItypeName(userid);
				// ���ݱ�Ų���������Ϣ�����洢��Tb_pay������
				Tb_income tb_income = incomeDAO.find(userid,
						Integer.parseInt(strno));
				txtMoney.setText(tb_income.getMoney2());// ��ʾ���
				txtMoney.setTextColor(Color.parseColor("#ffff0000"));
				txtTime.setText(tb_income.getTime());// ��ʾʱ��
				Selection = tb_income.getType() - 1; // ��ʾ���
				initphotodata(tb_income.getPhoto());
				textreAddres = tb_income.getHandler();
				textreMark = tb_income.getMark();
				txtAddress.setText(textreAddres);// ��ʾ�ص�
				txtMark.setText(textreMark);// ��ʾ��ע
			}
		}
	}

	private void initphotodata(String photo) { // ��ʼ��ͼƬ����
		if ((incount == 0) && (!photo.equals(""))) {
			String[] photoall = photo.split(",");
			for (int i = 0; i < photoall.length / 2; i++) {
				if (Bimp.drr.size() < 9) {
					Bimp.drr.add(photoall[i]);
				}
			}
			for (int i = photoall.length / 2; i < photoall.length; i++) {
				if (Bimp.smdrr.size() < 9) {
					Bimp.smdrr.add(photoall[i]);
				}
			}
			textphoto = photo;
			initphoto();
			incount++;
		}
	}

	@SuppressWarnings("deprecation")
	private void initphoto() {// ��ʼ��ͼƬ
		try {
			if (textphoto.equals("")) {
				addphoto.setImageResource(R.drawable.addphoto_btn);
			} else if (Bimp.getbitmap(Bimp.smdrr.get(0)) == null) {
				Toast.makeText(AddPay.this, "ͼƬ������", Toast.LENGTH_SHORT).show();
			} else {
				addphoto.setImageBitmap(Bimp.getbitmap(Bimp.smdrr.get(0)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updatetype() { // �������
		initData(userid);
		spdata = spdatalist.toArray(new String[spdatalist.size()]);// ��tb_itype�а��û�id��ȡ
		adapter = new ArrayAdapter<String>(AddPay.this, R.layout.spinner,
				spdata); // ��̬�������������б�
		spType.setAdapter(adapter);
		if (Selection > 0) {
			spType.setSelection(Selection);// ��ʾ���
		}
	}

	@Override
	protected void onStart() { // ��дonstart
		super.onStart();// ʵ�ֻ����еķ���
		updateDisplay();// ��ʾ��ǰϵͳʱ��

		Intent intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		bundle = intentr.getExtras();// ��ȡ��������ݣ���ʹ��Bundle��¼
		if (bundle.containsKey("cwp.message")) {
			strInfos = bundle.getStringArray("cwp.message");// ��ȡBundle�м�¼����Ϣ
			strno = strInfos[0];// ��¼id
			strType = strInfos[1];// ��¼����
			typemode = "ModifyInP";
			if (strType.equals("btnininfo")) { // ����
				type = "income";
			} else {
				type = "pay";
			}
		}
		keyBoard = new KeyboardUtil(this, this, txtMoney, typemode); // ���������
		if (bundle.containsKey("cwp.voice")) { // ����������������
			if (firstin) {
				bottom_empty.setVisibility(View.GONE);
				bottom_full.setVisibility(View.VISIBLE);
				dialogShowUtil.dialogShow("rotatebottom", "first", "", "");
				firstin = false;
			}
		}
		if (bundle.containsKey("cwp.photo")) {// ������������
			if (firstin) {
				bottom_empty.setVisibility(View.GONE);
				bottom_full.setVisibility(View.VISIBLE);
				Intent intent = new Intent(AddPay.this, PublishedActivity.class);
				intent.putExtra("cwp.id", userid);
				intent.putExtra("cwp.photo", "photo");
				startActivityForResult(intent, 102);
				firstin = false;
			}
		}
		if (bundle.containsKey("keyboard")) { // ������ʾ����
			if (keycount) {
				InputMethodManager imm = (InputMethodManager) getSystemService(AddPay.this.INPUT_METHOD_SERVICE); // ��ʾ����
				imm.hideSoftInputFromWindow(txtMoney.getWindowToken(), 0); // ���ؼ��̡�
				keyBoard.showKeyboard();
				keycount = false;
			}
		}
		updatetype();
		txtTime.setOnTouchListener(new OnTouchListener() { // Ϊʱ���ı������õ��������¼�
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				showDialog(DATE_DIALOG_ID);// ��ʾ����ѡ��Ի���
				return false;
			}
		});

		txtMoney.setOnTouchListener(new OnTouchListener() { // ��������̼���
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(AddPay.this.INPUT_METHOD_SERVICE); // ��ʾ����
				imm.hideSoftInputFromWindow(txtMoney.getWindowToken(), 0); // ���ؼ��̡�
				keyBoard.showKeyboard();
				return false;
			}
		});

		btnVoice.setOnClickListener(new OnClickListener() {// ����ʶ�����

			@Override
			public void onClick(View v) {
				dialogShowUtil.dialogShow("rotatebottom", "first", "", "");
			}
		});

		btnSaveButton.setOnClickListener(new OnClickListener() {// Ϊ���水ť���ü����¼�
					private String textreAddres;
					private String textreMark;

					@SuppressLint("NewApi")
					@Override
					public void onClick(View arg0) {
						textreAddres = txtAddress.getText().toString();
						textreMark = txtMark.getText().toString();
						if (textphoto == null) {
							textphoto = "";
						}
						if (typemode == "add") { // ���ģʽ
							String strMoney = txtMoney.getText().toString();// ��ȡ����ı����ֵ
							if (type == "pay") { // ֧��
								if (!strMoney.isEmpty()) {// �жϽ�Ϊ��
									// ����InaccountDAO����
									PayDAO payDAO = new PayDAO(AddPay.this);
									// ����Tb_inaccount����
									Tb_pay tb_pay = new Tb_pay(
											userid,
											payDAO.getMaxNo(userid) + 1,
											get2Double(strMoney),
											setTimeFormat(null),
											(spType.getSelectedItemPosition() + 1),
											textreAddres, textreMark, textphoto);
									payDAO.add(tb_pay);// ���������Ϣ
									Toast.makeText(AddPay.this,
											"���������롽������ӳɹ���", Toast.LENGTH_SHORT)
											.show();
									gotoback();
								} else {
									Toast.makeText(AddPay.this, "�����������",
											Toast.LENGTH_SHORT).show();
								}
							} else { // ����
								if (!strMoney.isEmpty()) {// �жϽ�Ϊ��
									// ����InaccountDAO����
									IncomeDAO incomeDAO = new IncomeDAO(
											AddPay.this);
									// ����Tb_inaccount����
									Tb_income tb_income = new Tb_income(
											userid,
											payDAO.getMaxNo(userid) + 1,
											get2Double(strMoney),
											setTimeFormat(null),
											(spType.getSelectedItemPosition() + 1),
											// txtInhandler.getText().toString(),
											textreAddres, textreMark,
											textphoto, "֧��");
									incomeDAO.add(tb_income);// ���������Ϣ
									// ������Ϣ��ʾ
									Toast.makeText(AddPay.this,
											"���������롽������ӳɹ���", Toast.LENGTH_SHORT)
											.show();
									gotoback();
								} else {
									Toast.makeText(AddPay.this, "�����������",
											Toast.LENGTH_SHORT).show();
								}
							}
						} else { // �޸�ģʽ
							if (type == "pay") { // ֧��
								if (!txtMoney.getText().toString().isEmpty()) {// �жϽ�Ϊ��
									Tb_pay tb_pay = new Tb_pay(); // ����Tb_pay����
									tb_pay.set_id(userid); // ����userid
									tb_pay.setNo(Integer.parseInt(strno)); // ���ñ��
									tb_pay.setMoney(get2Double(txtMoney
											.getText().toString()));// ���ý��
									tb_pay.setTime(setTimeFormat(txtTime
											.getText().toString()));// ����ʱ��
									tb_pay.setType(spType
											.getSelectedItemPosition() + 1);// �������
									tb_pay.setAddress(textreAddres);// ���õص�
									tb_pay.setMark(textreMark);// ���ñ�ע
									tb_pay.setPhoto(textphoto);// ���ñ�ע
									payDAO.update(tb_pay);// ����֧����Ϣ
									Toast.makeText(AddPay.this, "�����ݡ��޸ĳɹ���",
											Toast.LENGTH_SHORT).show();
									gotoback();
								} else {
									Toast.makeText(AddPay.this, "�����������",
											Toast.LENGTH_SHORT).show();
								}
							} else { // ����
								if (!txtMoney.getText().toString().isEmpty()) {// �жϽ�Ϊ��
									Tb_income tb_income = new Tb_income();// ����Tb_income����
									tb_income.set_id(userid);// ���ñ��
									tb_income.setNo(Integer.parseInt(strno));// ���ñ��
									tb_income.setMoney(get2Double(txtMoney
											.getText().toString()));// ���ý��
									tb_income.setTime(setTimeFormat(txtTime
											.getText().toString()));// ����ʱ��
									tb_income.setType(spType
											.getSelectedItemPosition() + 1);// �������
									tb_income.setHandler(textreAddres);// ���ø��
									tb_income.setMark(textreMark);// ���ñ�ע
									tb_income.setPhoto(textphoto);// ���ñ�ע
									incomeDAO.update(tb_income);// ����������Ϣ
									Toast.makeText(AddPay.this, "�����ݡ��޸ĳɹ���",
											Toast.LENGTH_SHORT).show();
									gotoback();
								} else {
									Toast.makeText(AddPay.this, "�����������",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					}
				});
		btnCancelButton.setOnClickListener(new OnClickListener() {// Ϊȡ����ť���õ��������¼�
					@Override
					public void onClick(View arg0) {

						if (typemode == "add") { // ���ģʽִ�з���
							txtMoney.setText("");// ���ý���ı���Ϊ��
							txtMoney.setHint("0.00");// Ϊ����ı���������ʾ
							txtTime.setText("");// ����ʱ���ı���Ϊ��
							txtAddress.setText("");// ���õ�ַ�ı���Ϊ��
							txtMark.setText("");// ���ñ�ע�ı���Ϊ��
							// txtInhandler.setText("");// ���ñ�ע�ı���Ϊ��
							spType.setSelection(0);// ������������б�Ĭ��ѡ���һ��
							gotoback();
						} else { // �޸�ģʽִ��ɾ��
							if (type == "pay") { // ֧��
								payDAO.detele(userid, Integer.parseInt(strno));// ���ݱ��ɾ��֧����Ϣ
								gotoback();
							} else { // ����
								incomeDAO.detele(userid,
										Integer.parseInt(strno));// ���ݱ��ɾ��������Ϣ
								gotoback();
							}
							Toast.makeText(AddPay.this, "�����ݡ�ɾ���ɹ���",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	// ����2λС��
	public static double get2Double(String strMoney) { // ����С����
		Double a = Double.parseDouble(strMoney);
		DecimalFormat df = new DecimalFormat("0.00");
		return new Double(df.format(a));
	}

	@Override
	protected Dialog onCreateDialog(int id)// ��дonCreateDialog����
	{
		switch (id) {
		case DATE_DIALOG_ID:// ��������ѡ��Ի���
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;// Ϊ��ݸ�ֵ
			mMonth = monthOfYear;// Ϊ�·ݸ�ֵ
			mDay = dayOfMonth;// Ϊ�츳ֵ
			updateDisplay();// ��ʾ���õ�����
		}
	};

	private void updateDisplay() {
		// ��ʾ���õ�ʱ��

		txtTime.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay));

	}

	private String setTimeFormat(String newtxtTime) { // �������ڸ�ʽ
		String date;
		if (typemode == "add") {
			date = txtTime.getText().toString();
		} else {
			date = newtxtTime;
		}

		int y, m, d;
		String sm, sd;
		int i = 0, j = 0, k = 0;

		for (i = 0; i < date.length(); i++) {
			if (date.substring(i, i + 1).equals("-") && j == 0)
				j = i;
			else if (date.substring(i, i + 1).equals("-"))
				k = i;
		}
		y = Integer.valueOf(date.substring(0, j));
		m = Integer.valueOf(date.substring(j + 1, k));
		d = Integer.valueOf(date.substring(k + 1));
		if (m < 10) {
			sm = "0" + String.valueOf(m);
		} else
			sm = String.valueOf(m);
		if (d < 10) {
			sd = "0" + String.valueOf(d);
		} else
			sd = String.valueOf(d);

		return String.valueOf(y) + "-" + sm + "-" + sd;

	}

	void VoiceRecognition() { // ���ðٶ�����ʶ��
		// mResult.setText(null);
		mCurrentTheme = Config.DIALOG_THEME;
		if (mDialog != null) {
			mDialog.dismiss();
		}
		Bundle params = new Bundle();
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, Constants.API_KEY); // �ٶ�����api_key
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY,
				Constants.SECRET_KEY);
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, // �ٶ���������
				Config.DIALOG_THEME);
		mDialog = new BaiduASRDigitalDialog(this, params);
		mDialog.setDialogRecognitionListener(mRecognitionListener);
		mDialog.getParams().putInt(BaiduASRDigitalDialog.PARAM_PROP, // �ٶ�ʶ�����
				Config.CURRENT_PROP);
		mDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE, // �ٶ�ʶ������
				Config.getCurrentLanguage());
		mDialog.getParams().putBoolean(
				// �ٶ�ʶ����Ч���
				BaiduASRDigitalDialog.PARAM_START_TONE_ENABLE,
				Config.PLAY_START_SOUND);
		mDialog.getParams().putBoolean(
				BaiduASRDigitalDialog.PARAM_END_TONE_ENABLE,
				Config.PLAY_END_SOUND);
		mDialog.getParams().putBoolean(
				BaiduASRDigitalDialog.PARAM_TIPS_TONE_ENABLE,
				Config.DIALOG_TIPS_SOUND);
		mDialog.show();
	}

	void VoiceSuccess() { // ʶ��ɹ�¼������
		if (DialogShowUtil.dialoggettype() != null) {
			type = DialogShowUtil.dialoggettype();
		}
		VoiceDefault = DialogShowUtil.dialogVoiceDefault();
		String textreMark = txtMark.getText().toString();

		if (typemode == "add") { // ���ģʽ
			if (type == "pay") { // ֧��
				rb1.setChecked(true);
				// corporation_fl.setVisibility(View.GONE);
				// address_fl.setVisibility(View.VISIBLE);
				spdatalist = ptypeDAO.getPtypeName(userid);
				txtMoney.setText(VoiceSave[1]);// ��ʾ���
				txtMoney.setTextColor(Color.parseColor("#5ea98d"));
				if (VoiceDefault == "notype") { // ���û��Ĭ�����
					spType.setSelection(Integer.parseInt(VoiceSave[5]));// ��ʾ����ʶ�����
				} else {
					spType.setSelection(Integer.parseInt(VoiceSave[0]));// ��ʾ���
				}
				txtMark.setText(textreMark + " " + VoiceSave[2]);// ��ʾ��ע
			} else { // ����
				rb2.setChecked(true);
				// corporation_fl.setVisibility(View.VISIBLE);
				// address_fl.setVisibility(View.GONE);
				spdatalist = ptypeDAO.getPtypeName(userid);
				txtMoney.setText(VoiceSave[1]);// ��ʾ���
				txtMoney.setTextColor(Color.parseColor("#ffff0000"));
				if (VoiceDefault == "notype") { // ���û��Ĭ�����
					spType.setSelection(Integer.parseInt(VoiceSave[5]));// ��ʾ���
				} else {
					spType.setSelection(Integer.parseInt(VoiceSave[4]));// ��ʾ���
				}
				txtMark.setText(textreMark + " " + VoiceSave[2]);// ��ʾ��ע
			}
		} else { // �޸�ģʽ
			if (type == "pay") { // ֧��
				rb1.setChecked(true);
				// ѡ���б��ʼ��
				spdatalist = ptypeDAO.getPtypeName(userid);
				spdata = spdatalist.toArray(new String[spdatalist.size()]);// ��tb_itype�а��û�id��ȡ
				adapter = new ArrayAdapter<String>(AddPay.this,
						R.layout.spinner, spdata); // ��̬�������������б�
				spType.setAdapter(adapter);
				txtMoney.setText(VoiceSave[1]);// ��ʾ���
				txtMoney.setTextColor(Color.parseColor("#5ea98d"));
				if (VoiceDefault == "notype") { // ���û��Ĭ�����
					spType.setSelection(Integer.parseInt(VoiceSave[5]));// ��ʾ����ʶ�����
				} else {
					spType.setSelection(Integer.parseInt(VoiceSave[0]));// ��ʾ���
				}
				txtMark.setText(textreMark + " " + VoiceSave[2]);// ��ʾ��ע
			} else { // ����
				// ѡ���б��ʼ��
				rb2.setChecked(true);
				spdatalist = itypeDAO.getItypeName(userid);
				spdata = spdatalist.toArray(new String[spdatalist.size()]);// ��tb_itype�а��û�id��ȡ
				adapter = new ArrayAdapter<String>(AddPay.this,
						R.layout.spinner, spdata); // ��̬�������������б�
				spType.setAdapter(adapter);
				txtMoney.setText(VoiceSave[1]);// ��ʾ���
				txtMoney.setTextColor(Color.parseColor("#ffff0000"));
				if (VoiceDefault == "notype") { // ���û��Ĭ�����
					spType.setSelection(Integer.parseInt(VoiceSave[5]));// ��ʾ���
				} else {
					spType.setSelection(Integer.parseInt(VoiceSave[4]));// ��ʾ���
				}
				txtMark.setText(textreMark + " " + VoiceSave[2]);// ��ʾ��ע
			}
		}
	}

	/*
	 * ʶ����������
	 * 
	 * @param VoiceSave[0] ��������ֵ
	 * 
	 * @param VoiceSave[1] ����ֵ
	 * 
	 * @param VoiceSave[3] �ظ�����ֵ����������ʾ����
	 * 
	 * @param VoiceSave[4] ֧������ֵ
	 * 
	 * @param VoiceSave[5] "����ʶ��"����ֵ
	 */
	private void Recognition(String t) {
		int mfirst = 100, mend = 0, temp = 0;
		Boolean ismoney = false, intype = false, outtype = false;
		Boolean voice_ptype = false, voice_intype = false;
		String w = "", strmoney = "", inname = "1", outname = "2";
		spdatalist = ptypeDAO.getPtypeName(userid);
		spdatalist2 = itypeDAO.getItypeName(userid);
		VoiceSave[2] = t;
		for (int i = 0; i < spdatalist.size(); i++) { // �ж��Ƿ����֧��
			if (t.indexOf(spdatalist.get(i).toString()) > -1) {
				type = "pay";
				intype = true;
				inname = spdatalist.get(i).toString();
				VoiceSave[0] = Integer.toString(i); // VoiceSave[0]Ϊ��������ֵ
			}
		}
		for (int i = 0; i < voice_pay.length; i++) { // �ж��Ƿ����֧���Ķ���
			if (t.indexOf(voice_pay[i]) > -1) {
				voice_ptype = true;
			}
		}
		for (int i = 0; i < voice_income.length; i++) { // �ж��Ƿ����֧���Ķ���
			if (t.indexOf(voice_income[i]) > -1) {
				voice_intype = true;
			}
		}
		for (int i = 0; i < spdatalist2.size(); i++) { // �ж��Ƿ��������
			if (t.indexOf(spdatalist2.get(i).toString()) > -1) {
				type = "income";
				outtype = true;
				outname = spdatalist2.get(i).toString();
				VoiceSave[4] = Integer.toString(i); // VoiceSave[4]Ϊ֧������ֵ
			}
		}
		for (int i = 0; i < number.length; i++) { // �ж��Ƿ��������ÿ�ͷ
			if (t.indexOf(number[i]) > -1) {
				temp = t.indexOf(number[i]);
				if (temp < mfirst) {
					mfirst = temp;
				}
			}
		}
		for (int i = 0; i < money.length; i++) { // �ж��Ƿ��������ý�β
			if (t.indexOf(money[i]) > -1) {
				temp = t.indexOf(money[i]);
				if (temp > -1 && temp >= mend) {
					mend = temp;
				}
			}
		}
		for (int i = 0; i < money2.length; i++) { // �ж��Ƿ��������ý�β
			if (t.indexOf(money2[i]) > -1) {
				temp = t.indexOf(money2[i]);
				if (temp > -1 && temp >= mend) {
					mend = temp;
				}
				mend = mend + 1;
			}
		}
		if (!(mfirst == 100 || mend == 0)) { // ת��Ϊ����������
			ismoney = true;
			strmoney = t.substring(mfirst, mend);
			// �ж�����Ƿ����������
			char[] chs = strmoney.toCharArray();
			List<String> num = Arrays.asList(number);
			List<String> mon = Arrays.asList(money);
			List<String> mon2 = Arrays.asList(money2);
			for (int l = 0; l < chs.length; l++)
				if (!num.contains(String.valueOf(chs[l])))
					if (!mon.contains(String.valueOf(chs[l])))
						if (!mon2.contains(String.valueOf(chs[l])))
							ismoney = false;
			if (ismoney) {
				DigitUtil Util = new DigitUtil();
				VoiceSave[1] = Integer.toString(Util.parse(strmoney)); // ���ù����ദ���ֵĽ��
			}
		}
		if (intype && outtype) { // ���ͬʱ��������/֧�������
			if (outname.equals(inname)) {
				if (ismoney) {
					if (voice_intype) {
						type = "income";
						dialogShowUtil.dialogShow("rotatebottom", "OK", t, w);
					} else if (voice_ptype) {
						type = "pay";
						dialogShowUtil.dialogShow("rotatebottom", "OK", t, w);
					} else {
						VoiceSave[3] = outname; // VoiceSave[3]Ϊ�ظ�����ֵ����������ʾ����
						dialogShowUtil.dialogShow("shake", "judge", t, w); // ������н��
					}
				} else {
					w = "��ʾ��\n��Ļ���û�а������ѻ�֧��<���>\n";
					dialogShowUtil.dialogShow("shake", "wrong", t, w);
				}
			} else {
				w = "**��ʾ��\nһ��ֻ�ܼ�¼һ����¼Ŷ\n"; // ����������벢��֧�������
				dialogShowUtil.dialogShow("shake", "wrong", t, w);
			}
		} else {
			if (!((intype || outtype) || ismoney)) { // ����������
				w = "**��ʾ��\n��Ļ���û�а���<���>��" + listToString(spdatalist, '��')
						+ "��" + listToString(spdatalist2, '��')
						+ "��\n\n**��ʾ��\n��Ļ���û�а������ѻ�֧��<���>";
				dialogShowUtil.dialogShow("shake", "wrong", t, w);
			} else if ((intype || outtype) && (!ismoney)) {
				w = "��ʾ��\n��Ļ���û�а������ѻ�֧��<���>\n���߳��ֶ�ν��";
				dialogShowUtil.dialogShow("shake", "wrong", t, w);
			} else if ((!(intype || outtype)) && ismoney) {
				for (int i = 0; i < spdatalist.size(); i++) { // �ж��Ƿ����֧��
					if ("����ʶ��".indexOf(spdatalist.get(i).toString()) > -1) {
						VoiceSave[5] = Integer.toString(i);
						VoiceSave[3] = "����ʶ��";
					}
				}
				w = "**��ʾ��\n��Ļ���û�а���<��Ĭ�ϣ����>��" + listToString(spdatalist, '��')
						+ "��\n\n\n�����¼Ϊ<����ʶ��>����Ƿ���Ȼ��¼��\n";
				dialogShowUtil.dialogShow("shake", "notype", t, w);
			} else {
				dialogShowUtil.dialogShow("rotatebottom", "OK", t, w);
			}
		}
	}

	public String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if (i < list.size() - 1) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	private boolean gotoback() { // ����
		Intent intent = null;
		type = "pay";
		typemode = "add";
		Bimp.drr = new ArrayList<String>();
		Bimp.smdrr = new ArrayList<String>();
		Bimp.bmp = new ArrayList<Bitmap>();
		Bimp.max = 0;
		Bimp.flag = 0;
		if (bundle.containsKey("cwp.frament3")) {
			intent = new Intent(AddPay.this, MainActivity.class);
			intent.putExtra("cwp.Fragment", "3");// ���ô�������
		} else if (bundle.containsKey("cwp.search")) {
			this.setResult(3);
			this.finish();
			return true;
		} else {
			intent = new Intent(AddPay.this, MainActivity.class);
			intent.putExtra("cwp.Fragment", "1");
		}
		intent.putExtra("cwp.id", userid);
		startActivity(intent);
		finish();
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			gotoback();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		if (dialogBuilder != null) {
			dialogBuilder.dismiss();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// �Ƴ���λ����
		mLocationManagerProxy.removeUpdates(this);
		// ���ٶ�λ
		mLocationManagerProxy.destroy();
	}

	protected void onResume() {
		super.onResume();
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

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// ��λ�ɹ��ص���Ϣ�����������Ϣ
			txtAddress.setText(amapLocation.getPoiName() + " [ "
					+ amapLocation.getAddress() + " ���ţ�"
					+ amapLocation.getCityCode() + " ]");
		} else {
			Log.e("AmapErr", "Location ERR:"
					+ amapLocation.getAMapException().getErrorCode());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_loacte:
			// ע�������λʱ�����Ҫ�Ƚ���λ����ɾ�����ٽ��ж�λ����
			mLocationManagerProxy.removeUpdates(this);
			int randomTime = mRandom.nextInt(1000);
			mLocationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000 + randomTime,
					15, this);
			mLocationManagerProxy.setGpsEnable(false);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 102:
			if (resultCode == 3 || resultCode == 0) {
				// for (int i = 0; i < Bimp.drr.size(); i++) {
				// System.out.println("Bimp.drr" + i + " " + Bimp.drr.get(i));
				// System.out.println("list" + i + " " + Bimp.smdrr.get(i));
				// }

				if ((Bimp.drr.size() != 0) && (Bimp.smdrr.size() != 0)) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < Bimp.drr.size(); i++) {
						sb.append(Bimp.drr.get(i) + ",");
					}
					for (int i = 0; i < Bimp.drr.size(); i++) {
						sb.append(Bimp.smdrr.get(i) + ",");
					}
					textphoto = sb.toString().substring(0, sb.length() - 1);
					initphoto();
				} else {
					textphoto = "";
					initphoto();
				}
				break;
			}

		}
	}

	public static void showVoiveDialog() {
		dialogShowUtil.dialogShow("rotatebottom", "first", "", "");
	}
}
