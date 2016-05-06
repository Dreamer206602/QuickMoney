package com.cwp.cmoneycharge.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baidu.mapapi.SDKInitializer;
import com.cwp.cmoneycharge.handler.CrashHandler;
import com.cwp.pattern.utils.LockPatternUtils;

public class CrashApplication extends Application {
	public boolean isLocked = true;
	private static CrashApplication mInstance;
	private LockPatternUtils mLockPatternUtils;
	
	LockScreenReceiver receiver ;
	IntentFilter filter ;

	public static CrashApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
		receiver = new LockScreenReceiver();
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(receiver, filter);
		
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	
	class LockScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				isLocked  = true;
			}
		}
	}
	
}
