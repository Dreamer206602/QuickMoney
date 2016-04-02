package com.cwp.cmoneycharge;

import java.util.Calendar;

import com.cwp.chart.manager.SystemBarTintManager;
import com.cwp.fragment.FragmentPage2;
import com.cwp.fragment.FragmentPage3;
import com.cwp.pattern.UnlockGesturePasswordActivity;
import com.cwp.pattern.UpdateManager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	// ����Fragmentҳ��
	private FragmentPage2 fragmentPage2;
	SharedPreferences sp;
	int userid;
	static int value = 0;
	DialogShowUtil dialogShowUtil = new DialogShowUtil(this, this, null, null,
			null);

	private Effectstype effect; // �Զ���Dialog
	private FragmentPage3 fragmentPage3;
	private String updatedate;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		sp = this.getSharedPreferences("preferences", MODE_WORLD_READABLE);
		edit = sp.edit();
		// initdefault();// ��ʼ������

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		fragmentPage2 = new FragmentPage2(this);
		// �õ�Fragment���������
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// �滻��ǰ��ҳ��
		fragmentTransaction.replace(R.id.frame_foot, fragmentPage2);
		fragmentTransaction.commit();

		SysApplication.getInstance().addActivity(this); // �����ٶ��������this
		Intent intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		if (intentr.getStringExtra("cwp.Fragment") != null) { // ȡ����ת��Ŀ��ҳ��
			value = Integer.parseInt(intentr.getStringExtra("cwp.Fragment"));
		}
		Calendar c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		int mYear = c.get(Calendar.YEAR);// ��ȡ���
		int mMonth = c.get(Calendar.MONTH);// ��ȡ�·�
		int mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ����
		updatedate = mYear + "-" + mMonth + 1 + "-" + mDay;

		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads().detectDiskWrites().detectNetwork()
		// .penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
		// .penaltyLog().penaltyDeath().build());
	}

	private void initdefault() { // ��ʼ������
		edit.putString("sendlog", "��"); // ��log
		edit.putString("gesturepw", "��"); // ���ƿ�
		edit.commit();
	}

	@Override
	public void onClick(View v) {

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			dialogShowUtil.dialogShow("shake", "quit", "", "");
		}
		return super.onKeyDown(keyCode, event);
	}

	public static int getValueFM() {
		return value;
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

	protected void onResume() {
		super.onResume();
		CrashApplication myApplaction = (CrashApplication) getApplication();
		if ((myApplaction.isLocked)
				&& (sp.getString("gesturepw", "").equals("��"))) {// �ж��Ƿ���Ҫ��ת���������
			Intent intent = new Intent(this,
					UnlockGesturePasswordActivity.class);
			startActivity(intent);
		}

		if (!updatedate.equals(sp.getString("updatedate", ""))) { // �����Ѿ������Ͳ��Զ������
			UpdateManager manager = new UpdateManager(MainActivity.this);
			manager.checkUpdate("noshow");
			edit.putString("updatedate", updatedate); // һ��ֻ���һ��
			edit.commit();
		}
	};
}
