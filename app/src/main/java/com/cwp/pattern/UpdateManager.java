package com.cwp.pattern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cwp.cmoneycharge.R;
import com.zhy.view.RoundProgressBarWidthNumber;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager extends Service {
	/* ����د */
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD2 = 4;
	/* ���ؽ��� */
	private static final int DOWNLOAD_FINISH = 2;
	public static final int IS_FINISH = 0;
	public static final int DOWNLOAD_FILERR = 3;
	/* ���������XML��Ϣ */
	HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;
	/* ��¼���������Y */
	private int progress;
	/* �Ƿ�ȡ������ */
	private boolean cancelUpdate = false;

	private static final int down_step = 3;

	private Context mContext;
	/* ���½��Ȱ� */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	public InputStream inputStream;
	private String show;

	private RemoteViews contentView;
	private NotificationManager notificationManager;
	protected Notification notification;
	public int updateCount = 0;
	private TextView update_text;

	private RoundProgressBarWidthNumber mRoundProgressBar;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ��������
			case DOWNLOAD:
				mRoundProgressBar.setProgress(progress);
				// ���ý�����λ�Z
				break;
			case DOWNLOAD_FINISH:
				// ��װ�ļ�
				installApk();
				break;
			case DOWNLOAD_FILERR:
				// �������ļ�
				Toast.makeText(mContext, "�����ļ���������������", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		};
	};

	public Handler handler = new Handler() {
		// ��Handler�л�ȡ��Ϣ����дhandleMessage()����
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// �ɹ���������,���޸���
				if (show.equals("show")) {
					Toast.makeText(mContext, "���޸���", Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				// �и���
				showNoticeDialog();
				break;
			case -1:
				// ��ס
				if (show.equals("show")) {
					Toast.makeText(mContext, "������˵�״��", Toast.LENGTH_LONG)
							.show();
					break;
				}
			}
		}
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/**
	 * ��?�������
	 */
	public void checkUpdate(String show) {
		this.show = show;
		if (isWifi(mContext)) {
			isUpdate();
		} else {
			if (show.equals("show")) {
				Toast.makeText(mContext, "��ǰΪ��WIFI���磡", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	/**
	 * ��?����Ƿ��и��°�c
	 */
	public void isUpdate() {
		// ��ȡ��ǰ����汾
		// ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ
		// InputStream inStream = ParseXmlService.class.getClassLoader()
		// .getResourceAsStream("version.xml");
		new Thread() {
			@Override
			public void run() {
				try {
					double versionCode = getVersionCode(mContext);
					URL url = new URL(
							"http://linmp6-wordpress.stor.sinaapp.com/version.xml");
					// ������������
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(1 * 1000);
					conn.setReadTimeout(1 * 1000); // ��������
					inputStream = conn.getInputStream();
					// ��ȡͼƬ����
					ParseXmlService service = new ParseXmlService();
					// ����XML�ļ�?����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
					mHashMap = service.parseXml(inputStream);
					Message message = new Message();
					message.what = 1;
					if (null != mHashMap) {
						Double serviceCode = Double.valueOf(mHashMap
								.get("version"));
						// �汾�ж�
						if ((serviceCode - versionCode) > 0) {
							message.what = 2;
						}
					}
					inputStream.close();
					// ������Ϣ����Ϣ������
					handler.sendMessage(message);
				} catch (Exception e) {
					Message message = new Message();
					message.what = -1;
					handler.sendMessage(message);
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * ��ȡ����汾��
	 * 
	 * @param context
	 * @return
	 */
	public double getVersionCode(Context context) {
		double versionCode = 0;
		try {
			// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
			versionCode = Double.valueOf(
					context.getPackageManager().getPackageInfo(
							context.getPackageName(), 0).versionName)
					.doubleValue();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * ��ʾ������¶Ի��U
	 */
	private void showNoticeDialog() {
		// ��??���U
		Builder builder = new Builder(mContext);
		builder.setTitle("�������");
		builder.setMessage("��⵽�°汾" + mHashMap.get("version") + "������������?\n\n"
				+ mHashMap.get("text").replace("\\n", "\n"));
		// ����
		builder.setPositiveButton("����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// ��ʾ���ضԻ��U
				showDownloadDialog();
			}
		});
		// �Ժ����
		builder.setNegativeButton("�Ժ����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * ��ʾ������ضԻ��U
	 */
	private void showDownloadDialog() {
		// ��??�����ضԻ��U
		Builder builder = new Builder(mContext);
		builder.setTitle("���ڸ���");
		// �����ضԻ������ӽ��Ȱ�
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mRoundProgressBar = (RoundProgressBarWidthNumber) v
				.findViewById(R.id.id_progress02);
		// mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		// update_text = (TextView) v.findViewById(R.id.update_text);
		builder.setView(v);
		// ȡ������
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// ����ȡ��״��
				cancelUpdate = true;

				notification.flags = Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(mContext, mHashMap.get("name"),
						"�û���ȡ������", null);
				notificationManager.notify(R.layout.notification_item,
						notification);
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// �����ļ�
		downloadApk();
	}

	/**
	 * ����apk�ļ�
	 */
	private void downloadApk() {
		// �������߳��������
		new downloadApkThread().start();
	}

	/**
	 * �����ļ��߳�
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ���������
					if (length > -1) {
						createNotification();
						InputStream is = conn.getInputStream();
						File file = new File(mSavePath);
						// �ж��ļ�Ŀ¼�Ƿ����
						if (!file.exists()) {
							file.mkdir();
						}
						File apkFile = new File(mSavePath, mHashMap.get("name"));
						FileOutputStream fos = new FileOutputStream(apkFile);
						int count = 0;
						// ����
						byte buf[] = new byte[1024];
						// д�뵽�ļ���
						do {
							int numread = is.read(buf);
							count += numread;
							progress = (int) (((float) count / length) * 100);
							// ���½���
							mHandler.sendEmptyMessage(DOWNLOAD);
							if (numread <= 0) {
								// �������
								mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
								break;
							}
							// д���ļ�
							fos.write(buf, 0, numread);
						} while (!cancelUpdate);// ���ȡ����ֹͣ�½�
						fos.close();
						is.close();
					} else {
						mHandler.sendEmptyMessage(DOWNLOAD_FILERR);
					}

				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ȡ�����ضԻ����Եj
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * make true current connect service is wifi
	 * 
	 * @param mContext
	 * @return
	 */
	private static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * ��װAPK�ļ�
	 */
	@SuppressWarnings("deprecation")
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i,
				0);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(mContext, mHashMap.get("name"), "���سɹ�",
				pendingIntent);
		notificationManager.notify(R.layout.notification_item, notification);
		/*** stop service *****/
		stopSelf();
	}

	/**
	 * ����������createNotification����
	 * 
	 * @param
	 * @return
	 * @see UpdateService
	 */
	@SuppressWarnings("deprecation")
	public void createNotification() {

		String app_name = null;
		// notification = new Notification(R.drawable.dot_enable,app_name +
		// getString(R.string.is_downing) ,System.currentTimeMillis());
		notification = new Notification(
		// R.drawable.video_player,//Ӧ�õ�ͼ��
				R.drawable.ic_launcher,// Ӧ�õ�ͼ��
				mHashMap.get("name") + "��������", System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		contentView = new RemoteViews(mContext.getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle,
				mHashMap.get("name") + "��������");
		contentView.setTextViewText(R.id.notificationPercent, "������");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
		notification.contentView = contentView;

		// updateIntent = new Intent(this, AboutActivity.class);
		// updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// //updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		// notification.contentIntent = pendingIntent;

		notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(R.layout.notification_item, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
