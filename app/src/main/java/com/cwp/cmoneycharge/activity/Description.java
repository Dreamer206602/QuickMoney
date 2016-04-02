package com.cwp.cmoneycharge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cwp.cmoneycharge.R;

import cwp.moneycharge.model.ActivityManager;

public class Description extends Activity{

	Intent intentr;
	int userid;
	public Description() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		ActivityManager.getInstance().addActivity(this);
	}	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();// ʵ�ֻ����еķ���//  �����Զ��巽����ʾ������Ϣ
		Intent intentr=getIntent();
		userid=intentr.getIntExtra("cwp.id",100000001);}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { //���/����/���η��ؼ�
	    	Intent intent=new Intent(Description.this,About.class);
			intent.putExtra("cwp.id",userid);
			startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
