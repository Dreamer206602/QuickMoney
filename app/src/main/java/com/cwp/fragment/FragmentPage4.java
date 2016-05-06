package com.cwp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.activity.AboutActivity;
import com.cwp.cmoneycharge.activity.InPtypeManagerActivity;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.widget.CustomDialog;

/**
 * 更多的界面
 */
public class FragmentPage4 extends BaseFragment {

	int userid;
	Intent intentr;
	private ListView listview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_4, container,false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listview = (ListView) getView().findViewById(R.id.settinglisv);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getView().getContext(), R.array.settingtype,
				android.R.layout.simple_expandable_list_item_1);

		listview.setAdapter(adapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		intentr = getActivity().getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		listview.setOnItemClickListener(new OnItemClickListener() {// 为GridView设置项单击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
									long arg3) {
				String result = arg0.getItemAtPosition(pos).toString();
				Intent intent = getActivity().getIntent();// 创建Intent对象
				userid = intent.getIntExtra("cwp.id", 100000001);
				switch (pos) {
					case 0:
						alarmDialog(pos);// 清空收入数据
						break;
					case 1:
						alarmDialog(pos); // 清空支出数据
						break;
//					case 2:
//						intentr = new Intent(getActivity(), SettingActivity.class); // 设置百度语音
//						startActivity(intentr);
//						break;
					case 3://收入类型的管理
						intentr = new Intent(getActivity(), InPtypeManagerActivity.class);
						intentr.putExtra("cwp.id", userid);
						intentr.putExtra("type", 0);
						startActivity(intentr);
						break;
					case 4://支出类型的管理
						intentr = new Intent(getActivity(), InPtypeManagerActivity.class);
						intentr.putExtra("cwp.id", userid);
						intentr.putExtra("type", 1);
						startActivity(intentr);
						break;
					case 5:
						alarmDialog(pos); // 数据初始化
						break;
					case 6:
						// 关于系统
						intentr = new Intent(getActivity(), AboutActivity.class);
						intentr.putExtra("cwp.id", userid);
						startActivity(intentr);
						break;
				}
			}

		});
	}

	private void alarmDialog(int type) { // 退出程序的方法
		Dialog dialog = null;
		String ps = "收入数据", is = "支出数据";
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(getView()
				.getContext());

		customBuilder.setTitle("警告"); // 创建标题
		switch (type) {
			case 0://清除收入的数据
				customBuilder
						.setMessage("将删除当前的用户所有" + ps)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										IncomeDAO incomeDAO = new IncomeDAO(
												getActivity());
										incomeDAO.deleteUserData(userid);
										Toast.makeText(getActivity(), "已清空~！！",
												Toast.LENGTH_LONG).show();
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();

									}
								});
				break;

			case 1://清除支出的数据
				customBuilder
						.setMessage("将删除当前的用户所有" + is)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										PayDAO payDAO = new PayDAO(getActivity());
										payDAO.deleteUserData(userid);
										Toast.makeText(getActivity(), "已清空~！！",
												Toast.LENGTH_LONG).show();
										dialog.dismiss();
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();

									}
								});
				break;
			case 5://重置数据
				customBuilder
						.setMessage("此操作将重置当前用户的收入、支出类型，确定还原吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										ItypeDAO itypedao = new ItypeDAO(
												getActivity());
										PtypeDAO ptypedao = new PtypeDAO(
												getActivity());
										itypedao.initData(userid);
										ptypedao.initData(userid);
										Toast.makeText(getActivity(), "已还原~！！",
												Toast.LENGTH_LONG).show();
										dialog.dismiss();
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();

									}
								});
				break;

		}

		dialog = customBuilder.create();// 创建对话框
		dialog.show(); // 显示对话框

	}

	@Override
	public void filngtonext() {


	}

	@Override
	public void filngtonpre() {
		FragmentPage2.clickHomeBtn();
	}
}
