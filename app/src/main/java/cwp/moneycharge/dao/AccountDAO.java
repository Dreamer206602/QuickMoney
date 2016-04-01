package cwp.moneycharge.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cwp.moneycharge.model.*;

public class AccountDAO {
	private DBOpenHelper helper;// ����DBOpenHelper����

	private SQLiteDatabase db;// ����SQLiteDatabase����

	public AccountDAO(Context context)// ���幹�캯��
	{
		helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����

	}

	/**
	 * ���������Ϣ
	 * 
	 * @param tb_account
	 */
	public int add(Tb_account tb_account) {

		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ������������
		if (tb_account.get_id() == 0) {
			db.execSQL(
					"insert into tb_account (username,pwd) values (?,?)",
					new Object[] { tb_account.getUsername(),tb_account.getPwd() });
		} else {
			db.execSQL(
					"insert into tb_account (_id,username,pwd) values (?,?,?)",
					new Object[] { tb_account.get_id(),tb_account.getUsername(), tb_account.getPwd() });
		}
		Cursor cursor = db.rawQuery(
				"select _id from tb_account where username=? and pwd=?",
				new String[] { tb_account.getUsername(), tb_account.getPwd() });
		if (cursor.moveToNext())// �������ҵ���������Ϣ
		{
			// ������洢��Tb_account����
			return cursor.getInt(cursor.getColumnIndex("_id"));
		} else
			return 100000001;
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param tb_account
	 */
	public void update(int id, String username, String pwd) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ִ���޸��������
		db.execSQL("update tb_account set username=? , pwd = ? where _id=?  ",
				new String[] { username, pwd, String.valueOf(id) });
	}

	public void deleteById(int id) {
		db = helper.getWritableDatabase();// ����SQLiteDatabase����
		// ִ��ɾ����ǩ��Ϣ����
		db.execSQL("delete from tb_account where _id =? ", new Object[] { id });
	}

	/**
	 * ����������Ϣ
	 * 
	 * @return
	 */
	public Tb_account find(String username, String pwd) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// �������벢�洢��Cursor����
		Cursor cursor = db
				.rawQuery(
						"select _id, username, pwd from tb_account where pwd=? and username=? ",
						new String[] { pwd, username });
		if (cursor.moveToNext())// �������ҵ���������Ϣ
		{
			// ������洢��Tb_account����
			return new Tb_account(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getString(cursor.getColumnIndex("pwd")));
		}
		return null;// ���û����Ϣ���򷵻�null
	}

	public String find(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// �������벢�洢��Cursor����
		Cursor cursor = db.rawQuery(
				"select username from tb_account where _id=? ",
				new String[] { String.valueOf(id) });
		if (cursor.moveToNext())// �������ҵ���������Ϣ
		{
			// ������洢��Tb_account����
			return cursor.getString(cursor.getColumnIndex("username"));

		} else
			return "������";// ���û����Ϣ���򷵻�null
	}

	public long getCount() {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select count(_id) from tb_account ", null);// ��ȡ������Ϣ�ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

}