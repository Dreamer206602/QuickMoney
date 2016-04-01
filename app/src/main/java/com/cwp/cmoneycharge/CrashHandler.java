package com.cwp.cmoneycharge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.umeng.fb.example.CustomActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�ɸ������ӹܳ���,����¼���ʹ��󱨸�.
 * 
 * @author way
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	StringBuffer sb;
	String result;
	private static final String TAG = "CrashHandler";
	private static final int MODE_WORLD_READABLE = 0x0001;
	private UncaughtExceptionHandler mDefaultHandler;// ϵͳĬ�ϵ�UncaughtException������
	private static CrashHandler INSTANCE = new CrashHandler();// CrashHandlerʵ��
	private Context mContext;// �����Context����
	private Map<String, String> info = new HashMap<String, String>();// �����洢�豸��Ϣ���쳣��Ϣ
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");// ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����

	/** ��ֻ֤��һ��CrashHandlerʵ�� */
	private CrashHandler() {

	}

	/** ��ȡCrashHandlerʵ�� ,����ģʽ */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * ��ʼ��
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		ContextWrapper a = (ContextWrapper) mContext.getApplicationContext();
		SharedPreferences sp = a.getSharedPreferences("preferences",
				MODE_WORLD_READABLE);
		if (sp.getString("sendlog", "").equals("��")
				|| sp.getString("sendlog", "").equals("")) {
			mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// ��ȡϵͳĬ�ϵ�UncaughtException������
			Thread.setDefaultUncaughtExceptionHandler(this);// ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
		}
	}

	/**
	 * ��UncaughtException����ʱ��ת�����д�ķ���������
	 */
	@SuppressWarnings("static-access")
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// ����Զ����û�д�������ϵͳĬ�ϵ��쳣������������

			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				thread.sleep(3000);// ��������ˣ��ó����������3�����˳�����֤�ļ����沢�ϴ���������
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 *            �쳣��Ϣ
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null || mContext == null)
			return false;

		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,�����˳�", 0).show();
				Toast.makeText(mContext, "��Ѵ�����־������������", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		// �ռ��豸������Ϣ
		collectDeviceInfo(mContext);
		// ������־�ļ�
		final File file = saveCrashInfo2File(ex);
		TimerTask task = new TimerTask() {
			public void run() {
				// sendAppCrashReport(mContext, file);
				sendreport(mContext);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 4000);
		return true;
	}

	protected void sendreport(Context mContext) {
		Intent intent = new Intent(mContext, CustomActivity.class);
		intent.putExtra("cwp.md", result);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		SysApplication.getInstance().exit();
	}

	public void sendAppCrashReport(Context mContext, File file) { // ͨ��ϵͳ�������ļ�
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ����intent��Action����
			intent.setAction(Intent.ACTION_VIEW);// ��ȡ�ļ�file��MIME����
			String type = "text/plain";// ����intent��data��Type���ԡ�
			intent.setDataAndType(Uri.fromFile(file), type);// ��ת
			mContext.startActivity(intent);
			// Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,�����˳�", 0).show();
		} catch (Exception e) {

		} finally {
			SysApplication.getInstance().exit();
		}

	}

	/**
	 * �ռ��豸������Ϣ
	 * 
	 * @param context
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();// ��ð�������
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// �õ���Ӧ�õ���Ϣ������Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();// �������
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
				Log.d(TAG, field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private File saveCrashInfo2File(Throwable ex) {
		sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\r\n");
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		// ѭ���Ű����е��쳣��Ϣд��writer��
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// �ǵùر�
		result = writer.toString();
		sb.append(result);
		// �����ļ�
		long timetamp = System.currentTimeMillis();
		String time = format.format(new Date());
		String fileName = "crash-" + time + "-" + timetamp + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/crash/");
				if (!dir.exists())
					dir.mkdir();
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
