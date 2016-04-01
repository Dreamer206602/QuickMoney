/**
 * 
 */
package cwp.moneycharge.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cwp.moneycharge.model.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * @author cwpcc
 * 
 */
public class IncomeDAO {

	private DBOpenHelper helper;// ����DBOpenHelper����
	private SQLiteDatabase db;// ����SQLiteDatabase����
	private int no = 1;// ���
	private int userid = 100000001;

	public IncomeDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}

	/**
	 * ���������Ϣ
	 * 
	 * @param tb_income
	 */
	public void add(Tb_income tb_income) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ�����������Ϣ����
		db.execSQL(
				"insert into tb_income (_id,no,money,time,type,handler,mark,photo,kind) values (?,?,?,?,?,?,?,?,?)",
				new Object[] { tb_income.get_id(), tb_income.getNo(),
						tb_income.getMoney(), tb_income.getTime(),
						tb_income.getType(), tb_income.getHandler(),
						tb_income.getMark(), tb_income.getPhoto(), "����" });
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param Tb_income
	 */
	public void update(Tb_income tb_income) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ���޸�������Ϣ����
		db.execSQL(
				"update Tb_income set money = ?,time = ?,type = ?,handler = ?,mark = ?,photo = ?   where _id = ? and no=?",
				new Object[] { tb_income.getMoney(), tb_income.getTime(),
						tb_income.getType(), tb_income.getHandler(),
						tb_income.getMark(), tb_income.getPhoto(),
						tb_income.get_id(), tb_income.getNo() });
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param id
	 *            ��no
	 * @return
	 */
	public Tb_income find(int id, int no) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db
				.rawQuery(
						"select _id, no, money,time,type,handler,mark,photo,kind from Tb_income where _id = ? and no=?",
						new String[] { String.valueOf(id), String.valueOf(no) });// �����û��ͱ�Ų���������Ϣ�����洢��Cursor����
		if (cursor.moveToFirst())// �������ҵ���������Ϣ
		{
			// ����������������Ϣ�洢��Tb_income����
			return new Tb_income(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")),
					cursor.getDouble(cursor.getColumnIndex("money")),
					cursor.getString(cursor.getColumnIndex("time")),
					cursor.getInt(cursor.getColumnIndex("type")),
					cursor.getString(cursor.getColumnIndex("handler")),
					cursor.getString(cursor.getColumnIndex("mark")),
					cursor.getString(cursor.getColumnIndex("photo")), "����");
		}
		return null;// ���û����Ϣ���򷵻�null
	}

	/**
	 * �h��������Ϣ
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
			// ִ��ɾ��������Ϣ����
			db.execSQL("delete from Tb_income where _id in (?) and no in  ("
					+ sb + ")", (Object[]) ids);
		}
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @param start
	 *            ��ʼλ��
	 * @param count
	 *            ÿҳ��ʾ����
	 * @return
	 */
	public List<Tb_income> getScrollData(int id, int start, int count) {
		List<Tb_income> tb_income = new ArrayList<Tb_income>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ����������Ϣ
		Cursor cursor = db.rawQuery(
				"select * from Tb_income where _id=? order by no limit ?,?",
				new String[] { String.valueOf(id), String.valueOf(start),
						String.valueOf(count) });
		while (cursor.moveToNext())// �������е�������Ϣ
		{
			// ����������������Ϣ��ӵ�������
			tb_income.add(new Tb_income(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getDouble(cursor
					.getColumnIndex("money")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getInt(cursor
					.getColumnIndex("type")), cursor.getString(cursor
					.getColumnIndex("handler")), cursor.getString(cursor
					.getColumnIndex("mark")), cursor.getString(cursor
					.getColumnIndex("photo")), null));
		}
		return tb_income;// ���ؼ���
	}

	public List<Tb_income> getScrollData(int id, int start, int count,
			String date1, String date2) {
		List<Tb_income> tb_income = new ArrayList<Tb_income>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ����������Ϣ
		Cursor cursor = db
				.rawQuery(
						"select * from Tb_income where _id=? and time >= ? and time <= ? order by time DESC",
						new String[] { String.valueOf(id), date1, date2 });
		while (cursor.moveToNext())// �������е�������Ϣ
		{
			// ����������������Ϣ��ӵ�������
			tb_income.add(new Tb_income(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getDouble(cursor
					.getColumnIndex("money")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getInt(cursor
					.getColumnIndex("type")), cursor.getString(cursor
					.getColumnIndex("handler")), cursor.getString(cursor
					.getColumnIndex("mark")), cursor.getString(cursor
					.getColumnIndex("photo")), null));
		}
		return tb_income;// ���ؼ���
	}

	public List<Tb_income> getScrollDataTotal(int id, int start, int count,
			String date1, String date2) {
		List<Tb_income> tb_income = new ArrayList<Tb_income>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ����������Ϣ
		Cursor cursor = db
				.rawQuery(
						"select * from Tb_income where _id=? and time >= ? and time <= ? UNION select * from Tb_pay  where _id=? and time >= ? and time <= ? order by time DESC",
						new String[] { String.valueOf(id), date1, date2,
								String.valueOf(id), date1, date2 });
		while (cursor.moveToNext())// �������е�������Ϣ
		{
			// ����������������Ϣ��ӵ�������
			tb_income.add(new Tb_income(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getDouble(cursor
					.getColumnIndex("money")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getInt(cursor
					.getColumnIndex("type")), cursor.getString(cursor
					.getColumnIndex("handler")), cursor.getString(cursor
					.getColumnIndex("mark")), cursor.getString(cursor
					.getColumnIndex("photo")), cursor.getString(cursor
					.getColumnIndex("kind"))));
		}
		return tb_income;// ���ؼ���
	}

	public List<Tb_income> search(int id, String string) {
		List<Tb_income> tb_income = new ArrayList<Tb_income>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ����������Ϣ
		Cursor cursor = db
				.rawQuery(
						"select * from Tb_income where _id=? and  money like ? union "
								+ "select * from Tb_income where _id=? and time like ? union "
								+ "select * from Tb_income where _id=? and handler like ? union "
								+ "select * from Tb_income where _id=? and  mark like ? union "
								+ "select * from Tb_income where _id=? and kind like ? union "
								+ "select Tb_income.* from Tb_income,tb_itype where Tb_income._id=? and Tb_income.type=tb_itype.no and tb_itype.typename like ?  union "
								+ "select tb_pay.* from tb_pay,tb_ptype where tb_pay._id=? and tb_pay.type=tb_ptype.no and tb_ptype.typename like ?  union "
								+ "select * from tb_pay where _id=? and money like ? union "
								+ "select * from tb_pay where _id=? and time like ? union "
								+ "select * from tb_pay where _id=? and address like ? union "
								+ "select * from tb_pay where _id=? and mark like ? union "
								+ "select * from tb_pay where _id=? and kind like ? "
								+ "order by time DESC ",
						new String[] { String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%",
								String.valueOf(id), "%" + string + "%", });
		while (cursor.moveToNext())// �������е�������Ϣ
		{
			// ����������������Ϣ��ӵ�������
			tb_income.add(new Tb_income(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getDouble(cursor
					.getColumnIndex("money")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getInt(cursor
					.getColumnIndex("type")), cursor.getString(cursor
					.getColumnIndex("handler")), cursor.getString(cursor
					.getColumnIndex("mark")), cursor.getString(cursor
					.getColumnIndex("photo")), cursor.getString(cursor
					.getColumnIndex("kind"))));
		}
		return tb_income;// ���ؼ���
	}

	/*
	 * �߼�����
	 */

	public List<Tb_income> searchALL(int id, String date1, String date2,
			String[] sp, String[] sin, String string) {
		List<Tb_income> tb_income = new ArrayList<Tb_income>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		String[] paytype = { "money", "address", "mark" };
		String[] incometype = { "money", "handler", "mark" };
		// ��ȡ����������Ϣ
		StringBuffer sql = new StringBuffer();// ƴ��sql
		for (int i = 0; i < sin.length; i++) {
			for (int j = 0; j < 3; j++) {
				sql.append("select Tb_income.* from Tb_income,tb_itype where Tb_income._id="
						+ "'"
						+ String.valueOf(id)
						+ "'"
						+ " and  time >= "
						+ "'"
						+ date1
						+ "'"
						+ " and time <= "
						+ "'"
						+ date2
						+ "'"
						+ " and Tb_income.type=tb_itype.no and tb_itype.typename="
						+ "'"
						+ sin[i]
						+ "'"
						+ " and Tb_income."
						+ incometype[j]
						+ " like "
						+ "'%"
						+ string
						+ "%'"
						+ "  union ");
			}
		}
		for (int i = 0; i < sp.length; i++) {
			for (int j = 0; j < 3; j++) {
				sql.append("select tb_pay.* from tb_pay,tb_ptype where tb_pay._id="
						+ "'"
						+ String.valueOf(id)
						+ "'"
						+ " and  time >= "
						+ "'"
						+ date1
						+ "'"
						+ " and time <= "
						+ "'"
						+ date2
						+ "'"
						+ " and tb_pay.type=tb_ptype.no and tb_ptype.typename="
						+ "'"
						+ sp[i]
						+ "'"
						+ " and tb_pay."
						+ paytype[j]
						+ " like " + "'%" + string + "%'" + "  union ");
			}
		}
		sql.delete(sql.length() - 6, sql.length()); // ɾ�����һ��uniom
		sql.append("order by time DESC");
		// System.out.println("sql" + sql);
		Cursor cursor = db.rawQuery(sql.toString(), null);
		while (cursor.moveToNext())// �������е�������Ϣ
		{
			// ����������������Ϣ��ӵ�������
			tb_income.add(new Tb_income(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getDouble(cursor
					.getColumnIndex("money")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getInt(cursor
					.getColumnIndex("type")), cursor.getString(cursor
					.getColumnIndex("handler")), cursor.getString(cursor
					.getColumnIndex("mark")), cursor.getString(cursor
					.getColumnIndex("photo")), cursor.getString(cursor
					.getColumnIndex("kind"))));
		}
		return tb_income;// ���ؼ���
	}

	/**
	 * ��ȡ�ܼ�¼��
	 * 
	 * @return
	 */
	public long getCount() {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select count(no) from tb_income", null);// ��ȡ������Ϣ�ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	public long getCount(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery(
				"select count(no) from tb_income where _id=?",
				new String[] { String.valueOf(id) });// ��ȡ������Ϣ�ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public int getMaxNo(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery(
				"select max(no) from tb_income where _id=?",
				new String[] { String.valueOf(id) });// ��ȡ������Ϣ���е������
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

	public Double getamountData(int userid2) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Double data = 0.0;
		Cursor cursor = db.rawQuery(
				"select sum(money) from tb_income where _id=?",
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
						"select total(money) as tmoney from tb_income where time >= ? and time <= ? and  _id =?",
						new String[] { date1, date2, String.valueOf(userid) });// ��ȡ֧����Ϣ���е������
		while (cursor.moveToNext())// �������е�֧����Ϣ
		{

			// ����������֧����Ϣ��ӵ�������
			data = data + cursor.getDouble(cursor.getColumnIndex("tmoney"));
		}
		cursor.close();
		datapicker = new Datapicker(no, data, date1);
		no++;
		return datapicker;// ���ؼ���
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
			Log.i("cwp", "===========pay" + temp
					+ datapicker.getMoney().toString());
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
		db.execSQL("delete from tb_income where _id=?", new Object[] { id });
	}

	public List<KindData> getKDataOnDay(int id, String date1, String date2) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		List<KindData> kinddatalist = new ArrayList<KindData>();
		Cursor cursor = db
				.rawQuery(
						"select type,  total(money) as tmoney from tb_income  where time >= ? and time <= ? and  _id =? group by type",
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

	public String gettime(int mYear, int defaultMonth, int defaultDay,
			boolean first, boolean end) {
		String dall, dmonth, dday;
		if (first || end) {
			String d1, d2;
			d1 = String.valueOf(mYear) + "-";
			d2 = String.valueOf(mYear) + "-";
			switch (defaultMonth) {
			case 1:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-31";
				break;
			case 2:
				if (mYear % 4 == 0 && mYear % 100 != 0 || mYear % 400 == 0) {
					d1 += "0" + String.valueOf(defaultMonth) + "-01";
					d2 += "0" + String.valueOf(defaultMonth) + "-29";
				} else {
					d1 += "0" + String.valueOf(defaultMonth) + "-01";
					d2 += "0" + String.valueOf(defaultMonth) + "-28";
				}
				break;
			case 3:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-31";
				break;
			case 4:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-30";
				break;
			case 5:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-31";
				break;
			case 6:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-30";
				break;
			case 7:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-31";
				break;
			case 8:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-31";
				break;
			case 9:
				d1 += "0" + String.valueOf(defaultMonth) + "-01";
				d2 += "0" + String.valueOf(defaultMonth) + "-30";
				break;
			case 10:
				d1 += String.valueOf(defaultMonth) + "-01";
				d2 += String.valueOf(defaultMonth) + "-31";
				break;
			case 11:
				d1 += String.valueOf(defaultMonth) + "-01";
				d2 += String.valueOf(defaultMonth) + "-30";
				break;
			case 12:
				d1 += String.valueOf(defaultMonth) + "-01";
				d2 += String.valueOf(defaultMonth) + "-31";
				break;
			}
			if (first) {
				dall = d1;
			} else {
				dall = d2;
			}
		} else {
			if (defaultMonth < 10) {
				dmonth = "0" + Integer.toString(defaultMonth);
			} else {
				dmonth = Integer.toString(defaultMonth);
			}
			if (defaultDay < 10) {
				dday = "0" + Integer.toString(defaultDay);
			} else {
				dday = Integer.toString(defaultDay);
			}
			dall = Integer.toString(mYear) + "-" + dmonth + "-" + dday;
		}
		return dall;
	}
}
