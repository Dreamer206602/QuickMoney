package com.cwp.cmoneycharge;

import java.util.ArrayList;
import java.util.List;

import com.cwp.chart.manager.SystemBarTintManager;

import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.CustomDialog;
import cwp.moneycharge.model.Tb_itype;
import cwp.moneycharge.model.Tb_ptype;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InPtypeManager extends Activity {

	public InPtypeManager() {
		// TODO Auto-generated constructor stub
	}

	private List<String> typename;
	private ListView lv;
	int userid, type;
	Intent intentr;
	ItypeDAO itypeDAO;
	PtypeDAO ptypeDAO;
	TextView inptext;
	Button add, delete;
	String inputStr = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inptypemanager);

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			findViewById(R.id.inptext_top).setVisibility(View.VISIBLE);
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		SysApplication.getInstance().addActivity(this); // ����ٶ��������this
		inptext = (TextView) findViewById(R.id.inptext);
		lv = (ListView) findViewById(R.id.typelist);// �õ�ListView���������
		add = (Button) findViewById(R.id.addtype);
		delete = (Button) findViewById(R.id.deletetype);
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
	public void onStart() {
		super.onStart();
		// ��ȡ���
		intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		type = intentr.getIntExtra("type", 0);
		itypeDAO = new ItypeDAO(InPtypeManager.this);
		ptypeDAO = new PtypeDAO(InPtypeManager.this);
		if (type == 0) {
			typename = itypeDAO.getItypeName(userid);
			inptext.setText("�������͹���");
		} else {
			typename = ptypeDAO.getPtypeName(userid);
			inptext.setText("֧�����͹���");
		}

		/* ΪListView����Adapter������� */
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, typename));

		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inputTitleDialog();
			}

		});
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				deleteDialog();
			}
		});
	}

	private void inputTitleDialog() {

		final EditText inputServer = new EditText(InPtypeManager.this);
		inputServer.setFocusable(true);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�������").setView(inputServer)
				.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				inputStr = inputServer.getText().toString();
				int i = (int) itypeDAO.getCount(userid) + 1;
				if (inputStr.trim().equals("")) {
					Toast.makeText(InPtypeManager.this, "�������ݲ���Ϊ�գ�",
							Toast.LENGTH_LONG).show();
					refresh();
				} else if (type == 0) {
					itypeDAO.add(new Tb_itype(userid, i, inputStr));
				} else {
					ptypeDAO.add(new Tb_ptype(userid, i, inputStr));
				}
				refresh();
			}
		});
		builder.show();
	}

	private void deleteDialog() { // �˳�����ķ���
		Dialog dialog = null;

		CustomDialog.Builder customBuilder = new CustomDialog.Builder(
				InPtypeManager.this);

		customBuilder
				.setTitle("ɾ��")
				// ��������

				.setMessage("��ȷ��Ҫɾ����")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						onDeleteClick();
						Toast.makeText(InPtypeManager.this, "��ɾ��",
								Toast.LENGTH_LONG).show();
						dialog.dismiss();
						refresh();
					}

				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		dialog = customBuilder.create();// �����Ի���
		dialog.show(); // ��ʾ�Ի���

	}

	public void onDeleteClick() {
		// ��ȡѡ�е���
		SparseBooleanArray checked = lv.getCheckedItemPositions();
		List<String> checkList = new ArrayList<String>();
		for (int i = 0; i < lv.getCount(); i++) {
			if (checked.get(i) == true) {
				// ��ȡ��ѡ����е����
				checkList.add(typename.get(i).toString());
			}
		}
		if (checkList.size() > 0) {
			for (String lchecked : checkList) {
				if (type == 1)
					ptypeDAO.deleteByName(userid, lchecked);
				else
					itypeDAO.deleteByName(userid, lchecked);
			}
		} else {
			Toast.makeText(InPtypeManager.this, "��δѡ���κ���,��ѡ��",
					Toast.LENGTH_LONG).show();
		}

		lv.clearChoices();// ���listView��ѡ��״̬�������´�ѡ��
	}

	public void refresh() {
		finish();
		Intent intentf = new Intent(InPtypeManager.this, InPtypeManager.class);
		intentf.putExtra("cwp.id", userid);
		intentf.putExtra("type", type);
		startActivity(intentf);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
			Intent intent = new Intent(InPtypeManager.this, MainActivity.class);
			intent.putExtra("cwp.id", userid);
			intent.putExtra("cwp.Fragment", "4");// ���ô������
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
