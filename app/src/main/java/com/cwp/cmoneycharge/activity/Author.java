package com.cwp.cmoneycharge.activity;

import cwp.moneycharge.model.ActivityManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cwp.cmoneycharge.R;

public class Author extends Activity {
	Intent intentr;
	int userid;
	public Author() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author);
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
	    	Intent intent=new Intent(Author.this,About.class);
			intent.putExtra("cwp.id",userid);
			startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
