package com.cwp.fragment;

import com.cwp.cmoneycharge.activity.About;
import com.cwp.cmoneycharge.activity.InPtypeManager;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.activity.SettingActivity;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.CustomDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentPage4 extends BaseFragment {

	int userid;
	Intent intentr;
	private ListView listview;

	public void Setting() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_4, null);
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
		listview.setOnItemClickListener(new OnItemClickListener() {// ΪGridView��������¼�
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				String result = arg0.getItemAtPosition(pos).toString();
				Intent intent = getActivity().getIntent();// ����Intent����
				userid = intent.getIntExtra("cwp.id", 100000001);
				switch (pos) {
				case 0:
					alarmDialog(pos);// �����������
					break;
				case 1:
					alarmDialog(pos); // ���֧������
					break;
				case 2:
					intentr = new Intent(getActivity(), SettingActivity.class); // ���ðٶ�����
					startActivity(intentr);
					break;
				case 3:
					intentr = new Intent(getActivity(), InPtypeManager.class);
					intentr.putExtra("cwp.id", userid);
					intentr.putExtra("type", 0);
					startActivity(intentr);
					break;
				case 4:
					intentr = new Intent(getActivity(), InPtypeManager.class);
					intentr.putExtra("cwp.id", userid);
					intentr.putExtra("type", 1);
					startActivity(intentr);
					break;
				case 5:
					alarmDialog(pos); // ���ݳ�ʼ��
					break;
				case 6:
					// ����ϵͳ
					intentr = new Intent(getActivity(), About.class);
					intentr.putExtra("cwp.id", userid);
					startActivity(intentr);
					break;
				}
			}

		});
	}

	private void alarmDialog(int type) { // �˳�����ķ���
		Dialog dialog = null;
		String ps = "��������", is = "֧������";
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(getView()
				.getContext());

		customBuilder.setTitle("����"); // ��������
		switch (type) {
		case 0:
			customBuilder
					.setMessage("��ɾ����ǰ���û�����" + ps)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									IncomeDAO incomeDAO = new IncomeDAO(
											getActivity());
									incomeDAO.deleteUserData(userid);
									Toast.makeText(getActivity(), "�����~����",
											Toast.LENGTH_LONG).show();
								}

							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});
			break;

		case 1:
			customBuilder
					.setMessage("��ɾ����ǰ���û�����" + is)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									PayDAO payDAO = new PayDAO(getActivity());
									payDAO.deleteUserData(userid);
									Toast.makeText(getActivity(), "�����~����",
											Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}

							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});
			break;
		case 5:
			customBuilder
					.setMessage("�˲��������õ�ǰ�û������롢֧�����ͣ�ȷ����ԭ��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									ItypeDAO itypedao = new ItypeDAO(
											getActivity());
									PtypeDAO ptypedao = new PtypeDAO(
											getActivity());
									itypedao.initData(userid);
									ptypedao.initData(userid);
									Toast.makeText(getActivity(), "�ѻ�ԭ~����",
											Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}

							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});
			break;

		}

		dialog = customBuilder.create();// �����Ի���
		dialog.show(); // ��ʾ�Ի���

	}

	@Override
	public void filngtonext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filngtonpre() {
		FragmentPage2.clickHomeBtn();
	}
}
