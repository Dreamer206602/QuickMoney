package com.cwp.cmoneycharge;

import java.util.Calendar;
import java.util.List;
  
//import com.cwp.cmoneycharge.R;

import cwp.moneycharge.dao.IncomeDAO; 
import cwp.moneycharge.dao.ItypeDAO; 
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.ActivityManager;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.PayDAO;

public class ModifyInP extends Activity{
	protected static final int DATE_DIALOG_ID = 0;// �������ڶԻ�����
	TextView tvtitle, textView;// ��������TextView����
	EditText txtMoney, txtTime, txtHA, txtMark;// ����4��EditText����
	Spinner spType;// ����Spinner����
	Button btnEdit, btnDel;// ��������Button����
	String[] strInfos;// �����ַ�������
	String strno, strType;// ���������ַ����������ֱ�������¼��Ϣ��ź͹�������
	int userid;
	ItypeDAO itypeDAO=new ItypeDAO(ModifyInP.this);
	PtypeDAO ptypeDAO=new PtypeDAO(ModifyInP.this);
	List<String> spdatalist;

	private int mYear;// ��
	private int mMonth;// ��
	private int mDay;// ��

    private ArrayAdapter<String> adapter;
    private String[] spdata;
	PayDAO payDAO= new PayDAO(ModifyInP.this );// ����PayDAO����
	IncomeDAO incomeDAO  = new IncomeDAO(ModifyInP.this);// ����IncomeDAO����
	public ModifyInP() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifyinp);// ���ò����ļ� 
		ActivityManager.getInstance().addActivity(this); //����Activity,�˳���ť���ʱ����
		tvtitle = (TextView) findViewById(R.id.inouttitle);// ��ȡ�����ǩ����
		textView = (TextView) findViewById(R.id.tvInOut);// ��ȡ�ص�/�����ǩ����
		txtMoney = (EditText) findViewById(R.id.txtInOutMoney);// ��ȡ����ı���
		txtTime = (EditText) findViewById(R.id.txtInOutTime);// ��ȡʱ���ı���
		spType = (Spinner) findViewById(R.id.spInOutType);// ��ȡ��������б�
		txtHA = (EditText) findViewById(R.id.txtInOut);// ��ȡ�ص�/����ı���
		txtMark = (EditText) findViewById(R.id.txtInOutMark);// ��ȡ��ע�ı���
		btnEdit = (Button) findViewById(R.id.btnInOutEdit);// ��ȡ�޸İ�ť
		btnDel = (Button) findViewById(R.id.btnInOutDelete);// ��ȡɾ����ť
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		Intent intent = getIntent();// ����Intent����
		Bundle bundle = intent.getExtras();// ��ȡ��������ݣ���ʹ��Bundle��¼
		strInfos = bundle.getStringArray("cwp.message");// ��ȡBundle�м�¼����Ϣ
		strno = strInfos[0];// ��¼id
		strType = strInfos[1];// ��¼����
		userid=intent.getIntExtra("cwp.id",100000001);
		if (strType.equals("btnoutinfo"))// ���������btnoutinfo
		{
			//ѡ���б��ʼ��
			spdatalist=ptypeDAO.getPtypeName(userid);
			spdata=spdatalist.toArray(new String[spdatalist.size()]);//��tb_itype�а��û�id��ȡ 
			adapter =new ArrayAdapter<String>(ModifyInP.this,android.R.layout.simple_spinner_item,spdata); //��̬�������������б�
			spType.setAdapter(adapter);
			
			tvtitle.setText("֧������");// ���ñ���Ϊ��֧������
			textView.setText("��  �㣺");// ���á��ص�/�������ǩ�ı�Ϊ���� �㣺��
			// ���ݱ�Ų���֧����Ϣ�����洢��Tb_pay������
			Tb_pay tb_pay = payDAO.find(userid,Integer.parseInt(strno));
			txtMoney.setText(String.valueOf(tb_pay.getMoney()));// ��ʾ���
			txtTime.setText(tb_pay.getTime());// ��ʾʱ��
			spType.setSelection(tb_pay.getType()-1);// ��ʾ���
			txtHA.setText(tb_pay.getAddress());// ��ʾ�ص�
			txtMark.setText(tb_pay.getMark());// ��ʾ��ע
		} else if (strType.equals("btnininfo"))// ���������btnininfo
		{
			//ѡ���б��ʼ��
			spdatalist=itypeDAO.getItypeName(userid);
			spdata=spdatalist.toArray(new String[spdatalist.size()]);//��tb_itype�а��û�id��ȡ 
			adapter =new ArrayAdapter<String>(ModifyInP.this,android.R.layout.simple_spinner_item,spdata); //��̬�������������б�
			spType.setAdapter(adapter);
			
			
			tvtitle.setText("�������");// ���ñ���Ϊ���������
			textView.setText("�����");// ���á��ص�/�������ǩ�ı�Ϊ���������
			// ���ݱ�Ų���������Ϣ�����洢��Tb_pay������
			Tb_income tb_income = incomeDAO.find(userid,Integer.parseInt(strno));
			txtMoney.setText(String.valueOf(tb_income.getMoney()));// ��ʾ���
			txtTime.setText(tb_income.getTime());// ��ʾʱ��	
			spType.setSelection(tb_income.getType()-1);// ��ʾ���
			txtHA.setText(tb_income.getHandler());// ��ʾ���
			txtMark.setText(tb_income.getMark());// ��ʾ��ע
		}

		txtTime.setOnClickListener(new OnClickListener() {// Ϊʱ���ı������õ��������¼�
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);// ��ʾ����ѡ��Ի���
			}
		});

		btnEdit.setOnClickListener(new OnClickListener() {// Ϊ�޸İ�ť���ü����¼�
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (strType.equals("btnoutinfo"))// �ж����������btnoutinfo
				{
					Tb_pay tb_pay = new Tb_pay();// ����Tb_pay����
					tb_pay.set_id(userid);// ����userid
					tb_pay.setNo(Integer.parseInt(strno));// ���ñ��
					tb_pay.setMoney(Double.parseDouble(txtMoney.getText().toString()));// ���ý��
					tb_pay.setTime(setTimeFormat(txtTime.getText().toString()));// ����ʱ��
					tb_pay.setType(spType.getSelectedItemPosition()+1);// �������
					tb_pay.setAddress(txtHA.getText().toString());// ���õص�
					tb_pay.setMark(txtMark.getText().toString());// ���ñ�ע
					payDAO.update(tb_pay);// ����֧����Ϣ
					Toast.makeText(ModifyInP.this, "�����ݡ��޸ĳɹ���", Toast.LENGTH_SHORT).show();
					gotoback();
				} else if (strType.equals("btnininfo"))// �ж����������btnininfo
				{
					Tb_income tb_income = new Tb_income();// ����Tb_income����
					tb_income.set_id(userid);// ���ñ��
					tb_income.setNo(Integer.parseInt(strno));// ���ñ��
					tb_income.setMoney(Double.parseDouble(txtMoney.getText().toString()));// ���ý��
					tb_income.setTime(setTimeFormat(txtTime.getText().toString()));// ����ʱ��
					tb_income.setType(spType.getSelectedItemPosition()+1);// �������
					tb_income.setHandler(txtHA.getText().toString());// ���ø��
					tb_income.setMark(txtMark.getText().toString());// ���ñ�ע
					incomeDAO.update(tb_income);// ����������Ϣ
					Toast.makeText(ModifyInP.this, "�����ݡ��޸ĳɹ���", Toast.LENGTH_SHORT).show();
					gotoback();
				}
				// ������Ϣ��ʾ
				 
			}
		});

		btnDel.setOnClickListener(new OnClickListener() {// Ϊɾ����ť���ü����¼�
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (strType.equals("btnoutinfo"))// �ж����������btnoutinfo
				{
					payDAO.detele(userid,Integer.parseInt(strno));// ���ݱ��ɾ��֧����Ϣ
					gotoback();
				} else if (strType.equals("btnininfo"))// �ж����������btnininfo
				{
					incomeDAO.detele(userid,Integer.parseInt(strno));// ���ݱ��ɾ��������Ϣ
					gotoback();
				}
				Toast.makeText(ModifyInP.this, "�����ݡ�ɾ���ɹ���", Toast.LENGTH_SHORT)
						.show();
			}
		});

		final Calendar c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		mYear = c.get(Calendar.YEAR);// ��ȡ���
		mMonth = c.get(Calendar.MONTH);// ��ȡ�·�
		mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ����
		updateDisplay();// ��ʾ��ǰϵͳʱ��
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

	private void gotoback(){
		 
		Intent intent=new Intent(ModifyInP.this,MainActivity.class);
		intent.putExtra("cwp.id",userid);
		startActivity(intent);
	}
	
	private String setTimeFormat(String txtTime){
		String date=txtTime;

		int y,m,d;
		String sm,sd;
		int i=0,j=0,k=0; 
		
		for (i = 0; i < date.length(); i++)   
		  {   
		   if (date.substring(i, i + 1).equals("-") && j==0)   
			    j=i;
		   else if(date.substring(i, i + 1).equals("-"))
			    k=i;
		  } 
		y=Integer.valueOf(date.substring(0,j));
		m=Integer.valueOf(date.substring(j+1,k));
		d=Integer.valueOf(date.substring(k+1));
		if(m<10){
			sm="0"+String.valueOf(m);
		}
		else
			sm=String.valueOf(m);
		if(d<10){
			sd="0"+String.valueOf(d);
		}
		else
			sd=String.valueOf(d);
 
		return String.valueOf(y)+"-"+sm+"-"+sd;
		
	}

}
