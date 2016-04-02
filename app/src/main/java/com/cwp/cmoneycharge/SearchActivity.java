package com.cwp.cmoneycharge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cwp.chart.widget.CustomMultiChoiceDialog;
import com.cwp.chart.adapter.MyAdspter;
import com.cwp.chart.manager.SystemBarTintManager;
import com.cwp.fragment.FragmentPage3;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.Tb_income;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchActivity extends Activity {
	private String arrs[] = { "�Զ���ʱ���", "����", "����", "����", "����", "����", "����",
			"����", "�ϼ�", "����" };
	private boolean boos[] = { false, false, false, false, false, false, false,
			false, false, false };
	private CustomMultiChoiceDialog.Builder multiChoiceDialogBuilder;

	public String contact_name;

	private CustomMultiChoiceDialog multiChoiceDialog;
	protected static final int DATE_DIALOG_ID = 0;// �������ڶԻ�����
	EditText query_dialog;
	ListView mListView;
	LinearLayout mlayout;
	private int userid;

	IncomeDAO incomeDAO = new IncomeDAO(this);
	PtypeDAO ptypeDAO = new PtypeDAO(this);
	ItypeDAO itypeDAO = new ItypeDAO(this);
	PayDAO payDAO = new PayDAO(this);// ����PayDAO����
	private String[] strInfos;
	private TextView searchincome;
	private TextView searchpay;
	private TextView seachbalance;
	private ArrayAdapter<String> arrayAdapter;
	private Editable text;
	private MyAdspter adapter;
	private TextView search_quit;
	private RelativeLayout search_more;
	private LinearLayout search_more_list;
	private RelativeLayout search_list_time;
	private TextView search_list_time_text;
	private RelativeLayout search_list_starttime;
	private static TextView search_starttime;
	private RelativeLayout search_list_endtime;
	private static TextView search_endtime;
	private RelativeLayout search_list_paytype;
	private TextView search_paytype;
	private RelativeLayout search_list_incometype;
	private TextView search_incometype;
	private List<String> spdatalistpay;
	private List<String> spdatalistincome;
	private String[] spdatapay;
	private String[] spdataincome;
	private boolean[] paychoice;
	private boolean[] incomechoice;
	protected String searchtype;
	public String[] resultpay;
	public String[] resultincome;
	private int mYear;
	private int mMonth;
	private int mDay;
	protected String timetype;
	private LinearLayout search_list_timeall;
	private RelativeLayout search_more_close;
	public int defaultMonth;
	public String dmonth;
	public int defaultDay;
	public String dday;
	private Calendar c;
	protected String searchstate = "quit";
	public boolean timeselect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_activity);

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		init();

	}

	public void init() {
		Intent intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);

		mlayout = (LinearLayout) findViewById(R.id.mlayout);// ��ȡ����ı���
		search_list_timeall = (LinearLayout) findViewById(R.id.search_list_timeall);// ��ȡ����ı���
		search_more_list = (LinearLayout) findViewById(R.id.search_more_list);// ��ȡ����ı���
		search_more = (RelativeLayout) findViewById(R.id.search_more);// ��ȡ����ı���
		search_more_close = (RelativeLayout) findViewById(R.id.search_more_close);// ��ȡ����ı���

		search_list_time = (RelativeLayout) findViewById(R.id.search_list_time);// ��ȡ����ı���
		search_list_starttime = (RelativeLayout) findViewById(R.id.search_list_starttime);// ��ȡ����ı���
		search_list_endtime = (RelativeLayout) findViewById(R.id.search_list_endtime);// ��ȡ����ı���
		search_list_paytype = (RelativeLayout) findViewById(R.id.search_list_paytype);// ��ȡ����ı���
		search_list_incometype = (RelativeLayout) findViewById(R.id.search_list_incometype);// ��ȡ����ı���

		mListView = (ListView) findViewById(R.id.mListView);// ��ȡ����ı���
		query_dialog = (EditText) findViewById(R.id.query_dialog);// ��ȡ����ı���
		searchincome = (TextView) findViewById(R.id.searchincome);// ��ȡ����ı���
		seachbalance = (TextView) findViewById(R.id.seachbalance);// ��ȡ����ı���
		searchpay = (TextView) findViewById(R.id.searchpay);// ��ȡ����ı���
		search_quit = (TextView) findViewById(R.id.search_quit);// ��ȡ����ı���

		search_list_time_text = (TextView) findViewById(R.id.search_list_time_text);// ��ȡ����ı���
		search_starttime = (TextView) findViewById(R.id.search_starttime);// ��ȡ����ı���
		search_endtime = (TextView) findViewById(R.id.search_endtime);// ��ȡ����ı���
		search_paytype = (TextView) findViewById(R.id.search_paytype);// ��ȡ����ı���
		search_incometype = (TextView) findViewById(R.id.search_incometype);// ��ȡ����ı���

		c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		mYear = c.get(Calendar.YEAR);// ��ȡ���
		mMonth = c.get(Calendar.MONTH) + 1;// ��ȡ�·�
		mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ����

		search_list_endtime.setOnTouchListener(new OnTouchListener() { // Ϊʱ���ı������õ��������¼�
					@Override
					public boolean onTouch(View v, MotionEvent event) {

						showDialog(DATE_DIALOG_ID);// ��ʾ����ѡ��Ի���
						timetype = "end";
						return false;
					}
				});

		search_list_starttime.setOnTouchListener(new OnTouchListener() { // Ϊʱ���ı������õ��������¼�
					@Override
					public boolean onTouch(View v, MotionEvent event) {

						showDialog(DATE_DIALOG_ID);// ��ʾ����ѡ��Ի���
						timetype = "start";
						return false;
					}
				});

		spdatalistpay = ptypeDAO.getPtypeName(userid);
		spdatalistincome = itypeDAO.getItypeName(userid);
		spdatapay = spdatalistpay.toArray(new String[spdatalistpay.size()]);// ��tb_itype�а��û�id��ȡ
		spdataincome = spdatalistincome.toArray(new String[spdatalistincome
				.size()]);// ��tb_itype�а��û�id��ȡ

		search_list_time.setOnClickListener(new OnClickListener() { // �߼�����ʱ���б�

					@Override
					public void onClick(View v) {
						showMultiChoiceDialog(v, null);
					}
				});

		search_list_paytype.setOnClickListener(new OnClickListener() {// �߼�����֧���б�

					@Override
					public void onClick(View v) {
						showMultiChoiceDialog(v, "pay");
						searchtype = "pay";
					}
				});

		search_list_incometype.setOnClickListener(new OnClickListener() {// �߼����������б�

					@Override
					public void onClick(View v) {
						showMultiChoiceDialog(v, "income");
						searchtype = "income";
					}
				});

		search_more_close.setOnClickListener(new OnClickListener() { // ���˸���ѡ��
					@Override
					public void onClick(View v) {
						closemore();
					}
				});

		search_more.setOnClickListener(new OnClickListener() { // ���˸���ѡ��
					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getApplicationContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // ǿ�ƹر������
						search_more.setVisibility(View.GONE);
						mlayout.setVisibility(View.GONE);
						search_more_list.setVisibility(View.VISIBLE);
						search_quit.setText("��ʼ��ѯ");
						searchstate = "search";
						search_starttime.setText(mYear + "-01-01");
						search_endtime.setText(mYear + "-12-31");
						search_paytype.setText("");
						search_incometype.setText("");
						resultincome = null;
						resultpay = null;
						paychoice = null;
						incomechoice = null;

					}
				});

		class OnClickListenernormal implements OnClickListener { // ִ�в�ѯ
			private String String;

			@Override
			public void onClick(View v) {
				if (searchstate == "quit") {
					SearchActivity.this.finish();
				} else {
					if (timeselect) {
						try {
							DateCompare(search_starttime.getText(),
									search_endtime.getText());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// System.out.println("timeselect "
						// + search_starttime.getText() + " "
						// + search_endtime.getText());
					} else {
						// System.out.println("2 " + search_starttime.getText()
						// + " " + search_endtime.getText());
					}
					if (resultincome == null) {
						resultincome = new String[1];
						resultincome[0] = (String) search_incometype.getText();
					}
					if (resultpay == null) {
						resultpay = new String[1];
						resultpay[0] = (String) search_paytype.getText();
					}
					// for (int i = 0; i < resultincome.length; i++) {
					// System.out.println("���� " + resultincome[i]);
					// }
					// for (int i = 0; i < resultpay.length; i++) {
					// System.out.println("֧�� " + resultpay[i]);
					// }
					if (text == null) {
						String = "";
					} else {
						String = text.toString();
					}
					List<Tb_income> listinfosall = incomeDAO.searchALL(userid,
							search_starttime.getText() + "",
							search_endtime.getText() + "", resultpay,
							resultincome, String);
					closemore();
					dispay(listinfosall);
				}
			}
		}

		search_quit.setOnClickListener(new OnClickListenernormal());

		mListView.setOnItemClickListener(new OnItemClickListener()// ΪListView�������¼�
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView txno = (TextView) view.findViewById(R.id.no);
						TextView txkind = (TextView) view
								.findViewById(R.id.kind);
						String strtype = ((String) txkind.getText()).substring(
								1, ((String) txkind.getText()).indexOf(']'))
								.trim();// ��������Ϣ�н�ȡ��֧����
						String strno = (String) txno.getText(); // ����Ϣ�н�ȡ��֧���
						Intent intent = new Intent(SearchActivity.this,
								AddPay.class);// ����Intent����
						if (strtype.equals("����")) {
							intent.putExtra("cwp.message", new String[] {
									strno, "btnininfo" });// ���ô�������
						}
						if (strtype.equals("֧��")) {
							intent.putExtra("cwp.message", new String[] {
									strno, "btnoutinfo" });// ���ô�������
						}
						intent.putExtra("cwp.id", userid);
						intent.putExtra("cwp.search", "search");
						startActivityForResult(intent, 101);// ִ��Intent����
					}
				});
		query_dialog.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// text ������иı�ǰ���ַ�����Ϣ
				// start ������иı�ǰ���ַ�������ʼλ��
				// count ������иı�ǰ����ַ����ı�����һ��Ϊ0
				// after ������иı����ַ�������ʼλ�õ�ƫ����

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// text ������иı����ַ�����Ϣ
				// start ������иı����ַ�������ʼλ��
				// before ������иı�ǰ���ַ�����λ�� Ĭ��Ϊ0
				// count ������иı���һ�������ַ���������
			}

			@Override
			public void afterTextChanged(Editable s) {
				// edit �������������������е���Ϣ
				text = s;
				update(text.toString());
			}
		});
	}

	protected void closemore() {
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		search_more.setVisibility(View.VISIBLE);
		search_more_list.setVisibility(View.GONE);
		search_quit.setText("ȡ��");
		searchstate = "quit";
		search_more.setBackgroundColor(Color.parseColor("#00000000"));
	}

	int getQuarterInMonth(int month, boolean isQuarterStart) {
		int months[] = { 1, 4, 7, 10 };
		if (!isQuarterStart) {
			months = new int[] { 3, 6, 9, 12 };
		}
		if (month >= 2 && month <= 4)
			return months[0];
		else if (month >= 5 && month <= 7)
			return months[1];
		else if (month >= 8 && month <= 10)
			return months[2];
		else
			return months[3];
	}

	public void showMultiChoiceDialog(View view, String type) { // ����ѡ��
		paychoice = new boolean[spdatalistpay.size()];
		incomechoice = new boolean[spdatalistincome.size()];
		for (int i = 0; i < paychoice.length; i++) {
			paychoice[i] = false;
		}
		for (int i = 0; i < incomechoice.length; i++) {
			incomechoice[i] = false;
		}
		multiChoiceDialogBuilder = new CustomMultiChoiceDialog.Builder(this);
		if (type == "pay") {
			multiChoiceDialog = multiChoiceDialogBuilder
					.setTitle("�������")
					.setMultiChoiceItems(true, spdatapay, paychoice, null, true)
					.setPositiveButton("ȷ��", new PositiveClickListener())
					.setNegativeButton("ȡ��", null).create();
		} else if (type == "income") {
			multiChoiceDialog = multiChoiceDialogBuilder
					.setTitle("֧�����")
					.setMultiChoiceItems(true, spdataincome, incomechoice,
							null, true)
					.setPositiveButton("ȷ��", new PositiveClickListener())
					.setNegativeButton("ȡ��", null).create();
		} else {
			multiChoiceDialog = multiChoiceDialogBuilder.setTitle("ʱ�䷶Χ")
					.setMultiChoiceItems(false, arrs, boos, new onitem(), true)
					.setPositiveButton(null, null)
					.setNegativeButton(null, null).create();
		}
		multiChoiceDialog.getWindow().setBackgroundDrawable(
				new BitmapDrawable());
		multiChoiceDialog.show();
	}

	class PositiveClickListener implements DialogInterface.OnClickListener { // ��ȷ����ť
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (CustomMultiChoiceDialog.Builder.getisMultiChoice()) {
				if (searchtype == "pay") {
					boos = multiChoiceDialogBuilder.getCheckedItems();
					int j1 = 0, j2 = 0;
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							j1++;
						}
					}
					resultpay = new String[j1];
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							resultpay[j2] = spdatapay[i];
							j2++;
						}
					}
					if (j1 > 1) {
						search_paytype.setText(resultpay[0] + ","
								+ resultpay[1] + "...");
					} else {
						if (resultpay.length != 0) {
							search_paytype.setText(resultpay[0]);
						} else {
							search_paytype.setText("���");
						}
					}
				} else if (searchtype == "income") {
					boos = multiChoiceDialogBuilder.getCheckedItems();
					int j1 = 0, j2 = 0;
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							j1++;
						}
					}
					resultincome = new String[j1];
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							resultincome[j2] = spdataincome[i];
							j2++;
						}
					}
					if (j1 > 1) {
						search_incometype.setText(resultincome[0] + ","
								+ resultincome[1] + "...");
					} else {
						if (resultincome.length != 0) {
							search_incometype.setText(resultincome[0]);
						} else {
							search_incometype.setText("���");
						}
					}
				}

			} else {
				String s = CustomMultiChoiceDialog.Builder.getcontact_name();
				// alert(MainActivity.this, s);
			}

		}
	}

	class onitem implements OnItemClickListener { // ʱ�䷶Χ��Ӧ�¼�

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView cn = (TextView) view.findViewById(R.id.contact_name);
			search_list_time_text.setText(cn.getText());
			switch (position) {
			case 0: // �Զ���ʱ��
				search_starttime.setText(mYear + "-01-01");
				search_endtime.setText(mYear + "-12-31");
				search_list_timeall.setVisibility(view.VISIBLE);
				timeselect = true;
				break;
			case 1:// ����
				search_list_timeall.setVisibility(view.GONE);
				timeselect = false;
				search_starttime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						false, false));
				search_endtime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						false, false));
				break;
			case 2:// ����
				search_list_timeall.setVisibility(view.GONE);
				timeselect = false;
				if (mDay == 1) {
					search_starttime.setText(incomeDAO.gettime(mYear,
							mMonth - 1, mDay, false, true));
					search_endtime.setText(incomeDAO.gettime(mYear, mMonth - 1,
							mDay, false, true));
				} else {
					search_starttime.setText(incomeDAO.gettime(mYear, mMonth,
							mDay - 1, false, false));
					search_endtime.setText(incomeDAO.gettime(mYear, mMonth,
							mDay - 1, false, false));
				}
				// System.out.println(search_starttime.getText() + "  "
				// + search_endtime.getText());
				break;
			case 3:// ����
				search_list_timeall.setVisibility(view.GONE);
				timeselect = false;
				int n = 0;
				// nΪ�Ƴٵ�������0���ܣ�-1��ǰ�Ƴ�һ�ܣ�1���ܣ���������
				c.add(Calendar.DATE, n * 7);
				// ���ܼ�������ʹ���Calendar.MONDAY��TUESDAY...��
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				search_starttime.setText(incomeDAO.gettime(
						c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				c.add(Calendar.DATE, 6);
				search_endtime.setText(incomeDAO.gettime(c.get(Calendar.YEAR),
						(c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				break;
			case 4:// ����
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				// nΪ�Ƴٵ�������0���ܣ�-1��ǰ�Ƴ�һ�ܣ�1���ܣ���������
				c.add(Calendar.DATE, (-1) * 7);
				// ���ܼ�������ʹ���Calendar.MONDAY��TUESDAY...��
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				search_starttime.setText(incomeDAO.gettime(
						c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				c.add(Calendar.DATE, 6);
				search_endtime.setText(incomeDAO.gettime(c.get(Calendar.YEAR),
						(c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				break;
			case 5:// ����
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				search_starttime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						true, false));
				search_endtime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						false, true));
				break;
			case 6:// ����
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				search_starttime.setText(incomeDAO.gettime(mYear, mMonth - 1,
						mDay, true, false));
				search_endtime.setText(incomeDAO.gettime(mYear, mMonth - 1,
						mDay, false, true));
				break;
			case 7:// ����
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				int month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1,
						true);
				calendar.set(Calendar.MONTH, month);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				search_starttime.setText(incomeDAO.gettime(
						calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1),
						calendar.get(Calendar.DAY_OF_MONTH), false, false));
				// ����ĩ
				calendar.setTime(new Date());
				month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1,
						false);
				calendar.set(Calendar.MONTH, month + 1);
				calendar.set(Calendar.DAY_OF_MONTH, 0);
				search_endtime.setText(incomeDAO.gettime(
						calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1),
						calendar.get(Calendar.DAY_OF_MONTH), false, false));
				break;
			case 8:// �ϼ�
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				Calendar calendar2 = new GregorianCalendar();
				calendar2.setTime(new Date());
				int month2 = getQuarterInMonth(
						calendar2.get(calendar2.MONTH) + 1, true);
				calendar2.set(calendar2.MONTH, month2 - 3);
				calendar2.set(calendar2.DAY_OF_MONTH, 1);
				search_starttime.setText(incomeDAO.gettime(
						calendar2.get(calendar2.YEAR),
						(calendar2.get(calendar2.MONTH) + 1),
						calendar2.get(calendar2.DAY_OF_MONTH), false, false));
				calendar2.setTime(new Date());
				month2 = getQuarterInMonth(calendar2.get(calendar2.MONTH) + 1,
						false);
				calendar2.set(calendar2.MONTH, month2 - 2);
				calendar2.set(calendar2.DAY_OF_MONTH, 0);
				search_endtime.setText(incomeDAO.gettime(
						calendar2.get(calendar2.YEAR),
						(calendar2.get(calendar2.MONTH) + 1),
						calendar2.get(calendar2.DAY_OF_MONTH), false, false));
				break;
			case 9:// ����
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				search_starttime.setText(incomeDAO.gettime(mYear - 1, 1, 1,
						false, false));
				search_endtime.setText(incomeDAO.gettime(mYear - 1, 12, 31,
						false, false));
				break;
			}
			multiChoiceDialog.dismiss();
		}
	}

	protected void update(String string) {
		List<Tb_income> listinfos2 = incomeDAO.search(userid, string);
		dispay(listinfos2);
	}

	private void dispay(List<Tb_income> listinfos2) {
		mlayout.setVisibility(View.VISIBLE);
		search_more.setBackgroundColor(Color.parseColor("#e8e8e8"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int m = 0, income = 0, pay = 0;// ����һ����ʼ��ʶ
		strInfos = new String[listinfos2.size()];
		if (listinfos2.size() == 0) { // ����listview
			seachbalance.setText("�� 0.0");
			searchpay.setText("�� 0.0");
			searchincome.setText("�� 0.0");
			mListView.setVisibility(View.GONE);
		} else {
			mListView.setVisibility(View.VISIBLE);
			for (Tb_income tb_income : listinfos2) {// ����List���ͼ���
				// �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				if (tb_income.getKind().equals("����")) { // ����
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img",
							itypeDAO.getOneImg(userid, tb_income.getType()));
					map.put("no", tb_income.getNo() + "");
					map.put("kind", "[" + tb_income.getKind() + "]");
					map.put("address", tb_income.getHandler());
					map.put("money", "�� " + tb_income.getMoney2() + "Ԫ");
					map.put("title",
							itypeDAO.getOneName(userid, tb_income.getType()));
					map.put("info", tb_income.getTime());
					map.put("date",
							FragmentPage3.gofordate(tb_income.getTime()));
					list.add(map);
					income += tb_income.getMoney();
					m++;// ��ʶ��1
				} else { // ֧��
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img",
							ptypeDAO.getOneImg(userid, tb_income.getType()));
					map.put("no", tb_income.getNo() + "");
					map.put("kind", "[" + tb_income.getKind() + "]");
					map.put("address", tb_income.getHandler());
					map.put("money", "�� " + tb_income.getMoney2() + "Ԫ");
					map.put("title",
							ptypeDAO.getOneName(userid, tb_income.getType()));
					map.put("info", tb_income.getTime());
					map.put("date",
							FragmentPage3.gofordate(tb_income.getTime()));
					list.add(map);
					pay += tb_income.getMoney();
					m++;// ��ʶ��1
				}
			}
			seachbalance.setText("�� " + String.valueOf(income - pay));
			searchpay.setText("�� " + String.valueOf(-pay));
			searchincome.setText("�� " + String.valueOf(income));
			adapter = new MyAdspter(this, list, true);
			mListView.setAdapter(adapter);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 3) {
			if (text != null) {
				update(text.toString());
			}
		}
	}

	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;// Ϊ��ݸ�ֵ
			mMonth = monthOfYear;// Ϊ�·ݸ�ֵ
			mDay = dayOfMonth;// Ϊ�츳ֵ
			updateDisplay();// ��ʾ���õ�����
		}
	};

	@Override
	protected Dialog onCreateDialog(int id)// ��дonCreateDialog����
	{
		switch (id) {
		case DATE_DIALOG_ID:// ��������ѡ��Ի���
			return new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
					mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private void updateDisplay() {
		if (timetype == "start") {
			search_starttime.setText(incomeDAO.gettime(mYear, mMonth + 1, mDay,
					false, false));
		} else {
			search_endtime.setText(incomeDAO.gettime(mYear, mMonth + 1, mDay,
					false, false));
		}
	}

	public static void DateCompare(CharSequence s1, CharSequence s2)
			throws Exception {
		// �趨ʱ���ģ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// �õ�ָ��ģ����ʱ��
		Date d1 = sdf.parse(s1 + "");
		Date d2 = sdf.parse(s2 + "");
		// �Ƚ�
		if (d1.getTime() - d2.getTime() > 0) {
			CharSequence a = search_starttime.getText();
			search_starttime.setText(search_endtime.getText());
			search_endtime.setText(a);
		}
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

}
