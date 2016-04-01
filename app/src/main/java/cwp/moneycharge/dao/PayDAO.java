/**
 * 
 */
package cwp.moneycharge.dao;

/**
 * @author cwpcc
 *
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cwp.moneycharge.model.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.GetChars;
import android.util.Log;

public class PayDAO {

	private DBOpenHelper helper;// ����DBOpenHelper����
	private SQLiteDatabase db;// ����SQLiteDatabase����
	private int no = 1;// ���
	private int userid = 100000001;

	public PayDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}

	/**
	 * ���֧����Ϣ
	 * 
	 * @param tb_pay
	 */
	public void add(Tb_pay tb_pay) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ�����֧����Ϣ����
		db.execSQL(
				"insert into tb_pay (_id,no,money,time,type,address,mark,photo,kind) values (?,?,?,?,?,?,?,?,?)",
				new Object[] { tb_pay.get_id(), tb_pay.getNo(),
						tb_pay.getMoney(), tb_pay.getTime(), tb_pay.getType(),
						tb_pay.getAddress(), tb_pay.getMark(),
						tb_pay.getPhoto(), "֧��" });
	}

	/**
	 * ����֧����Ϣ
	 * 
	 * @param tb_pay
	 */
	public void update(Tb_pay tb_pay) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ���޸�֧����Ϣ����
		db.execSQL(
				"update tb_pay set money = ?,time = ?,type = ?,address = ?,mark = ?,photo = ? where _id = ? and no=?",
				new Object[] { tb_pay.getMoney(), tb_pay.getTime(),
						tb_pay.getType(), tb_pay.getAddress(),
						tb_pay.getMark(), tb_pay.getPhoto(), tb_pay.get_id(),
						tb_pay.getNo() });
	}

	/**
	 * ����֧����Ϣ
	 * 
	 * @param id
	 *            ,no
	 * @return
	 */
	public Tb_pay find(int id, int no) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db
				.rawQuery(
						"select _id,no,money,time,type,address,mark,photo from tb_pay where _id = ? and no=?",
						new String[] { String.valueOf(id), String.valueOf(no) });// ���ݱ�Ų���֧����Ϣ�����洢��Cursor����
		if (cursor.moveToNext())// �������ҵ���֧����Ϣ
		{
			// ����������֧����Ϣ�洢��Tb_outaccount����
			return new Tb_pay(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")),
					cursor.getDouble(cursor.getColumnIndex("money")),
					cursor.getString(cursor.getColumnIndex("time")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getString(cursor.getColumnIndex("address")),
					cursor.getString(cursor.getColumnIndex("mark")),
					cursor.getString(cursor.getColumnIndex("photo")));
		}
		return null;// ���û����Ϣ���򷵻�null
	}

	/**
	 * �h��֧����Ϣ
	 * 
	 * @param ids
	 */
	public void detele(Integer... ids) {
		if (ids.length > 0)// �ж��Ƿ����Ҫɾ����id
		{
			StringBuffer sb = new StringBuffer();// ����StringBuffer����
			for (int i = 0; i < ids.length - 1; i++)// ����Ҫɾ����id����
			{
				sb.append('?').append(',');// ��ɾ��������ӵ�StringBuffer������
			}
			sb.deleteCharAt(sb.length() - 1);// ȥ�����һ����,���ַ�
			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
			// ִ��ɾ��֧����Ϣ����
			db.execSQL("delete from tb_pay where _id in (?) and no in (" + sb
					+ ")", (Object[]) ids);
		}
	}

	/**
	 * ��ȡ֧����Ϣ
	 * 
	 * @param start
	 *            ��ʼλ��
	 * @param count
	 *            ÿҳ��ʾ����
	 * @return
	 */
	public List<Tb_pay> getScrollData(int id, int start, int count) {
		List<Tb_pay> tb_pay = new ArrayList<Tb_pay>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ����֧����Ϣ
		Cursor cursor = db
				.rawQuery(
						"select _id,no,money,time,type,address,mark,photo from tb_pay where _id=? order by no limit ?,?",
						new String[] { String.valueOf(id),
								String.valueOf(start), String.valueOf(count) });
		while (cursor.moveToNext())// �������е�֧����Ϣ
		{
			// ����������֧����Ϣ��ӵ�������
			tb_pay.add(new Tb_pay(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")), cursor
							.getDouble(cursor.getColumnIndex("money")), cursor
							.getString(cursor.getColumnIndex("time")), cursor
							.getInt(cursor.getColumnIndex("type")), cursor
							.getString(cursor.getColumnIndex("address")),
					cursor.getString(cursor.getColumnIndex("mark")), cursor
							.getString(cursor.getColumnIndex("photo"))));
		}
		return tb_pay;// ���ؼ���
	}

	/**
	 * ��ȡ�ܼ�¼��
	 * 
	 * @return
	 */
	public long getCount() {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select count(no) from tb_pay", null);// ��ȡ֧����Ϣ�ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	public long getCount(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select count(no) from tb_pay where _id=?",
				new String[] { String.valueOf(id) });// ��ȡ������Ϣ�ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	/**
	 * ��ȡ֧�������
	 * 
	 * @return
	 */
	public int getMaxNo(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select max(no) from tb_pay where _id=?",
				new String[] { String.valueOf(id) });// ��ȡ֧����Ϣ���е������
		while (cursor.moveToLast()) {// ����Cursor�е����һ������
			return cursor.getInt(0);// ��ȡ���ʵ������ݣ��������
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	@SuppressLint("SimpleDateFormat")
	public String addDays(String sdate, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = (Date) sdf.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		date = cal.getTime();
		String result = sdf.format(date);
		return result;
	}

	public Double getamountData(int userid) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Double data = 0.0;
		Cursor cursor = db.rawQuery(
				"select sum(money) from tb_pay where _id=?",
				new String[] { String.valueOf(userid) });
		// ��ȡ֧����Ϣ���е������
		while (cursor.moveToNext())// �������е�֧����Ϣ
		{
			// ����������֧����Ϣ��ӵ�������
			data = cursor.getDouble(0);
		}
		cursor.close();
		return data;
	}

	public Datapicker getDataOnDay(String date1, String date2) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Double data = 0.0;
		Datapicker datapicker;
		Cursor cursor = db
				.rawQuery(
						"select total(money) as tmoney from tb_pay  where time >= ? and time <= ? and  _id =?",
						new String[] { date1, date2, String.valueOf(userid) });// ��ȡ֧����Ϣ���е������
		while (cursor.moveToNext())// �������е�֧����Ϣ
		{

			// ����������֧����Ϣ��ӵ�������
			data = cursor.getDouble(cursor.getColumnIndex("tmoney"));
		}
		cursor.close();
		datapicker = new Datapicker(no, data, date1);
		no++;
		return datapicker;
		// ����������֧����Ϣ��ӵ�������

	} // ���ؼ���

	public List<Datapicker> getDataMonth(int id, int year, int month) {
		String d1, d2;
		d1 = String.valueOf(year) + "-";
		d2 = String.valueOf(year) + "-";
		userid = id;
		no = 1;
		switch (month) {
		case 1:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 2:
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
				d1 += "0" + String.valueOf(month) + "-01";
				d2 += "0" + String.valueOf(month) + "-29";
			} else {
				d1 += "0" + String.valueOf(month) + "-01";
				d2 += "0" + String.valueOf(month) + "-28";
			}
			break;
		case 3:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 4:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-30";
			break;
		case 5:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 6:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-30";
			break;
		case 7:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 8:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 9:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-30";
			break;
		case 10:
			d1 += String.valueOf(month) + "-01";
			d2 += String.valueOf(month) + "-31";
			break;
		case 11:
			d1 += String.valueOf(month) + "-01";
			d2 += String.valueOf(month) + "-30";
			break;
		case 12:
			d1 += String.valueOf(month) + "-01";
			d2 += String.valueOf(month) + "-31";
			break;

		}
		List<Datapicker> datapickerlist = new ArrayList<Datapicker>();
		Datapicker datapicker;
		for (String temp = d1; temp.compareTo(d2) <= 0;) {
			// �������϶���
			datapicker = getDataOnDay(temp, temp);
			datapickerlist.add(datapicker);
			temp = addDays(temp, 1);
		}

		return datapickerlist;
	}

	public List<Datapicker> getDataAnytime(int id, String date1, String date2) {

		userid = id;
		no = 1;

		List<Datapicker> datapickerlist = new ArrayList<Datapicker>();
		Datapicker datapicker;
		for (String temp = date1; temp.compareTo(date2) <= 0;) {
			// �������϶���
			datapicker = getDataOnDay(temp, temp);
			datapickerlist.add(datapicker);
			temp = addDays(temp, 1);
		}

		return datapickerlist;
	}

	public void deleteUserData(int id) {// ����û�����
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL("delete from tb_pay where _id=?", new Object[] { id });
	}

	public List<Tb_pay> getScrollData(int id, int start, int count,
			String date1, String date2) {
		List<Tb_pay> tb_pay = new ArrayList<Tb_pay>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ����֧����Ϣ
		Cursor cursor = db
				.rawQuery(
						"select _id,no,money,time,type,address,mark,photo from tb_pay where _id=? and time >= ? and time <= ? order by time DESC",
						new String[] { String.valueOf(id), date1, date2 });
		while (cursor.moveToNext())// �������е�֧����Ϣ
		{
			// ����������֧����Ϣ��ӵ�������
			tb_pay.add(new Tb_pay(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")), cursor
							.getDouble(cursor.getColumnIndex("money")), cursor
							.getString(cursor.getColumnIndex("time")), cursor
							.getInt(cursor.getColumnIndex("type")), cursor
							.getString(cursor.getColumnIndex("address")),
					cursor.getString(cursor.getColumnIndex("mark")), cursor
							.getString(cursor.getColumnIndex("photo"))));
		}
		return tb_pay;// ���ؼ���
	}

	public List<KindData> getKDataOnDay(int id, String date1, String date2) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		List<KindData> kinddatalist = new ArrayList<KindData>();
		Cursor cursor = db
				.rawQuery(
						"select type,  total(money) as tmoney from tb_pay  where time >= ? and time <= ? and  _id =? group by type",
						new String[] { date1, date2, String.valueOf(id) });// ��ȡ֧����Ϣ���е������
		while (cursor.moveToNext())// �������е�֧����Ϣ
		{

			// ����������֧����Ϣ��ӵ�������
			kinddatalist.add(new KindData(cursor.getInt(cursor
					.getColumnIndex("type")), cursor.getDouble(cursor
					.getColumnIndex("tmoney"))));
		}
		cursor.close();

		return kinddatalist;// ���ؼ���
		// ����������֧����Ϣ��ӵ�������

	} // ���ؼ���

	public List<KindData> getKDataOnMonth(int id, int year, int month) {
		String d1, d2;
		d1 = String.valueOf(year) + "-";
		d2 = String.valueOf(year) + "-";
		switch (month) {
		case 1:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 2:
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
				d1 += "0" + String.valueOf(month) + "-01";
				d2 += "0" + String.valueOf(month) + "-29";
			} else {
				d1 += "0" + String.valueOf(month) + "-01";
				d2 += "0" + String.valueOf(month) + "-28";
			}
			break;
		case 3:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 4:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-30";
			break;
		case 5:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 6:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-30";
			break;
		case 7:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 8:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-31";
			break;
		case 9:
			d1 += "0" + String.valueOf(month) + "-01";
			d2 += "0" + String.valueOf(month) + "-30";
			break;
		case 10:
			d1 += String.valueOf(month) + "-01";
			d2 += String.valueOf(month) + "-31";
			break;
		case 11:
			d1 += String.valueOf(month) + "-01";
			d2 += String.valueOf(month) + "-30";
			break;
		case 12:
			d1 += String.valueOf(month) + "-01";
			d2 += String.valueOf(month) + "-31";
			break;

		}
		return getKDataOnDay(id, d1, d2);
	}
}