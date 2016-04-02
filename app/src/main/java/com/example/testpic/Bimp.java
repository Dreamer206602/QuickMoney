package com.example.testpic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bimp {
	public static int max = 0;
	public static int flag = 0;
	public static int flag2 = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();

	// ͼƬsd��ַ �ϴ�������ʱ��ͼƬ�������淽��ѹ���� ���浽��ʱ�ļ��� ͼƬѹ����С��100KB��ʧ��Ȳ�����
	public static List<String> drr = new ArrayList<String>();
	public static List<String> smdrr = new ArrayList<String>();

	public static Bitmap getbitmap(String path) throws IOException {
		Bitmap bitmap = null;
		File f = new File(path);
		if (f.exists()) { /* ����Bitmap���󣬲�����mImageView�� */
			bitmap = BitmapFactory.decodeFile(path);
		}
		return bitmap;
	}

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
