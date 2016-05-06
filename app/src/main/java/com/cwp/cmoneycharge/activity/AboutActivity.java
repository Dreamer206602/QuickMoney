package com.cwp.cmoneycharge.activity;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cwp.chart.manager.SystemBarTintManager;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.app.CrashApplication;
import com.cwp.cmoneycharge.app.SysApplication;
import com.cwp.pattern.activity.UnlockGesturePasswordActivity;
import com.cwp.pattern.service.UpdateManager;
import com.umeng.fb.example.FeedBackActivity;

import cwp.moneycharge.dao.AccountDAO;
import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.NoteDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.widget.CustomDialog;

/**
 * 关于系统的界面
 */
public class AboutActivity extends Activity {
	TextView usernow, countuser, countpay, countincome, sendlog, gesturepw,
			gesturepwmdtext, gesturepwcleartext;
	TableRow author, description, gesturepwmd;
	Intent intentr;
	int userid;
	AccountDAO accountDAO = new AccountDAO(AboutActivity.this);
	PayDAO payDAO = new PayDAO(AboutActivity.this);
	IncomeDAO incomeDAO = new IncomeDAO(AboutActivity.this);
	NoteDAO noteDAO = new NoteDAO(AboutActivity.this);
	static SharedPreferences sp;
	Editor edit;
	private final int REQUESTCODE = 1;  //返回的结果码
	private TextView app_version;
	private TextView updateapp;
	private TextView feedback;

	public AboutActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		SysApplication.getInstance().addActivity(this); // 在销毁队列中添加this

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
			usernow.setText("默认用户");
		else {
			usernow.setText(accountDAO.find(userid));
		}
		app_version.setText(getVersion(this));

		//用户反馈的点击事件
		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this, FeedBackActivity.class);
				// intent.putExtra("cwp.md", "md");
				startActivity(intent);
			}
		});

		//检查更新的点击事件
		updateapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateManager manager = new UpdateManager(AboutActivity.this);
				// 检查软件更新
				manager.checkUpdate("show");
			}
		});

		class OnClickListenermd implements OnClickListener { // 修改密码

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this,
						UnlockGesturePasswordActivity.class);
				intent.putExtra("cwp.md", "md");
				startActivity(intent);
			}
		}

		if (sp.getString("gesturepw", "").equals("关")
				|| sp.getString("gesturepw", "").equals("")) {
			gesturepw.setText("关");
		} else {
			gesturepw.setText("开");
			gesturepwmd.setVisibility(View.VISIBLE);
			if (CrashApplication.getInstance().getLockPatternUtils()
					.savedPatternExists()) {
				gesturepwmd.setOnClickListener(new OnClickListenermd()); // 修改密码
			}
		}

		if (sp.getString("sendlog", "").equals("关")
				|| sp.getString("sendlog", "").equals("")) {
			sendlog.setText("关");
		} else {
			sendlog.setText("开");
		}
		countuser.setText(String.valueOf(accountDAO.getCount()));
		countpay.setText(String.valueOf(payDAO.getCount(userid)));
		countincome.setText(String.valueOf(incomeDAO.getCount(userid)));

		gesturepw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(
						AboutActivity.this);

				customBuilder.setTitle("提示"); // 创建标题
				customBuilder
						.setMessage("是否开启/关闭手势密码？\n注意：关闭手势密码需输入原密码！")
						.setPositiveButton("开启",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();
										if (!sp.getString("gesturepw", "")
												.equals("开")) {
											Intent intent = new Intent(
													AboutActivity.this,
													UnlockGesturePasswordActivity.class);
											intent.putExtra("cwp.pwenable",
													"enable");
											startActivityForResult(intent,
													REQUESTCODE); // 表示可以返回结果
										}
									}
								})
						.setNegativeButton("关闭",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();
										Intent intent = new Intent(
												AboutActivity.this,
												UnlockGesturePasswordActivity.class);
										intent.putExtra("cwp.pwclear", "clear");
										startActivityForResult(intent,
												REQUESTCODE); // 表示可以返回结果
									}
								});
				Dialog dialog = customBuilder.create();// 创建对话框
				dialog.show(); // 显示对话框
			}
		});

		sendlog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(
						AboutActivity.this);

				customBuilder.setTitle("提示"); // 创建标题
				customBuilder
						.setMessage("是否开启/关闭发送错误日志功能？\n注意：需退出程序重新进入完成初始化！")
						.setPositiveButton("开启",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										edit.putString("sendlog", "开");
										edit.commit();
										sendlog.setText("开");
										dialog.dismiss();
									}
								})
						.setNegativeButton("关闭",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										edit.putString("sendlog", "关");
										edit.commit();
										sendlog.setText("关");
										dialog.dismiss();

									}
								});
				Dialog dialog = customBuilder.create();// 创建对话框
				dialog.show(); // 显示对话框
			}
		});

		description.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AboutActivity.this, DescriptionActivity.class);
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
				edit.putString("gesturepw", "关");
				edit.commit();
				gesturepw.setText("关");
				gesturepwmd.setVisibility(View.GONE);
			}
			if (resultCode == 3) {
				edit.putString("gesturepw", "开");
				edit.commit();
				gesturepw.setText("开");
				gesturepwmd.setVisibility(View.VISIBLE);
			}
		} else {
			Toast.makeText(AboutActivity.this, "操作失败！", Toast.LENGTH_LONG).show();
		}
	}

	public static String getVersion(Context context)// 获取版本号
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
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			Intent intent = new Intent(AboutActivity.this, MainActivity.class);
			intent.putExtra("cwp.id", userid);
			intent.putExtra("cwp.Fragment", "4");// 设置传递数据
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
