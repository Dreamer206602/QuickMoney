package com.cwp.cmoneycharge.activity;

import com.cwp.chart.manager.SystemBarTintManager;
import com.cwp.cmoneycharge.app.CrashApplication;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.app.SysApplication;
import com.cwp.pattern.activity.UnlockGesturePasswordActivity;
import com.cwp.pattern.service.UpdateManager;
import com.umeng.fb.example.CustomActivity;

import cwp.moneycharge.dao.AccountDAO;
import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.NoteDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.model.CustomDialog;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class About extends Activity {
	TextView usernow, countuser, countpay, countincome, sendlog, gesturepw,
			gesturepwmdtext, gesturepwcleartext;
	TableRow author, description, gesturepwmd;
	Intent intentr;
	int userid;
	AccountDAO accountDAO = new AccountDAO(About.this);
	PayDAO payDAO = new PayDAO(About.this);
	IncomeDAO incomeDAO = new IncomeDAO(About.this);
	NoteDAO noteDAO = new NoteDAO(About.this);
	static SharedPreferences sp;
	Editor edit;
	private final int REQUESTCODE = 1; // ���صĽ����
	private TextView app_version;
	private TextView updateapp;
	private TextView feedback;

	public About() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);

		SysApplication.getInstance().addActivity(this); // ����ٶ��������this

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			findViewById(R.id.about_top).setVisibility(View.VISIBLE);
		}
		SystemBarTintManager mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		usernow = (TextView) findViewById(R.id.useracc);
		countpay = (TextView) findViewById(R.id.countpay);
		countuser = (TextView) findViewById(R.id.countuser);
		countincome = (TextView) findViewById(R.id.countincome);
		description = (TableRow) findViewById(R.id.description);
		sendlog = (TextView) findViewById(R.id.sendlog);
		gesturepw = (TextView) findViewById(R.id.gesturepw);
		gesturepwmd = (TableRow) findViewById(R.id.gesturepwmd);
		gesturepwmdtext = (TextView) findViewById(R.id.gesturepwmdtext);
		app_version = (TextView) findViewById(R.id.app_version);
		updateapp = (TextView) findViewById(R.id.updateapp);
		feedback = (TextView) findViewById(R.id.feedback);
		sp = this.getSharedPreferences("preferences", MODE_WORLD_READABLE);
		edit = sp.edit();
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
		intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		if (userid == 100000001)
			usernow.setText("Ĭ���û�");
		else {
			usernow.setText(accountDAO.find(userid));
		}
		app_version.setText(getVersion(this));

		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(About.this, CustomActivity.class);
				// intent.putExtra("cwp.md", "md");
				startActivity(intent);
			}
		});

		updateapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateManager manager = new UpdateManager(About.this);
				// ����������
				manager.checkUpdate("show");
			}
		});

		class OnClickListenermd implements OnClickListener { // �޸�����

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(About.this,
						UnlockGesturePasswordActivity.class);
				intent.putExtra("cwp.md", "md");
				startActivity(intent);
			}
		}

		if (sp.getString("gesturepw", "").equals("��")
				|| sp.getString("gesturepw", "").equals("")) {
			gesturepw.setText("��");
		} else {
			gesturepw.setText("��");
			gesturepwmd.setVisibility(View.VISIBLE);
			if (CrashApplication.getInstance().getLockPatternUtils()
					.savedPatternExists()) {
				gesturepwmd.setOnClickListener(new OnClickListenermd()); // �޸�����
			}
		}

		if (sp.getString("sendlog", "").equals("��")
				|| sp.getString("sendlog", "").equals("")) {
			sendlog.setText("��");
		} else {
			sendlog.setText("��");
		}
		countuser.setText(String.valueOf(accountDAO.getCount()));
		countpay.setText(String.valueOf(payDAO.getCount(userid)));
		countincome.setText(String.valueOf(incomeDAO.getCount(userid)));

		gesturepw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(
						About.this);

				customBuilder.setTitle("��ʾ"); // ��������
				customBuilder
						.setMessage("�Ƿ���/�ر��������룿\nע�⣺�ر���������������ԭ���룡")
						.setPositiveButton("����",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										if (!sp.getString("gesturepw", "")
												.equals("��")) {
											Intent intent = new Intent(
													About.this,
													UnlockGesturePasswordActivity.class);
											intent.putExtra("cwp.pwenable",
													"enable");
											startActivityForResult(intent,
													REQUESTCODE); // ��ʾ���Է��ؽ��
										}
									}
								})
						.setNegativeButton("�ر�",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										Intent intent = new Intent(
												About.this,
												UnlockGesturePasswordActivity.class);
										intent.putExtra("cwp.pwclear", "clear");
										startActivityForResult(intent,
												REQUESTCODE); // ��ʾ���Է��ؽ��
									}
								});
				Dialog dialog = customBuilder.create();// �����Ի���
				dialog.show(); // ��ʾ�Ի���
			}
		});

		sendlog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(
						About.this);

				customBuilder.setTitle("��ʾ"); // ��������
				customBuilder
						.setMessage("�Ƿ���/�رշ��ʹ�����־���ܣ�\nע�⣺���˳��������½�����ɳ�ʼ����")
						.setPositiveButton("����",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										edit.putString("sendlog", "��");
										edit.commit();
										sendlog.setText("��");
										dialog.dismiss();
									}
								})
						.setNegativeButton("�ر�",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										edit.putString("sendlog", "��");
										edit.commit();
										sendlog.setText("��");
										dialog.dismiss();

									}
								});
				Dialog dialog = customBuilder.create();// �����Ի���
				dialog.show(); // ��ʾ�Ի���
			}
		});

		description.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(About.this, Description.class);
				intent.putExtra("cwp.id", userid);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUESTCODE) {
			if (resultCode == 2) {
				edit.putString("gesturepw", "��");
				edit.commit();
				gesturepw.setText("��");
				gesturepwmd.setVisibility(View.GONE);
			}
			if (resultCode == 3) {
				edit.putString("gesturepw", "��");
				edit.commit();
				gesturepw.setText("��");
				gesturepwmd.setVisibility(View.VISIBLE);
			}
		} else {
			Toast.makeText(About.this, "����ʧ�ܣ�", Toast.LENGTH_LONG).show();
		}
	}

	public static String getVersion(Context context)// ��ȡ�汾��
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "beta 1.0";
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			Intent intent = new Intent(About.this, MainActivity.class);
			intent.putExtra("cwp.id", userid);
			intent.putExtra("cwp.Fragment", "4");// ���ô������
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
