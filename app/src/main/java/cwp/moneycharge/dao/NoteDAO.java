/**
 * 
 */
package cwp.moneycharge.dao;

/**
 * @author cwpcc
 *
 */
import java.util.ArrayList;
import java.util.List;

import cwp.moneycharge.dao.DBOpenHelper;
import cwp.moneycharge.model.Tb_note;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDAO {
//_id integer primary key,no integer autoincrement,note varchar(200))
	/**
	 * 
	 */
	private DBOpenHelper helper;// ����DBOpenHelper����
	private SQLiteDatabase db;// ����SQLiteDatabase����

	 
	public NoteDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}
	/**
	 * ��ӱ�ǩ��Ϣ
	 * 
	 * @param tb_note
	 */
	public void add(Tb_note tb_note) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL("insert into tb_note (_id,no,note) values (?,?,?)", new Object[] {
				tb_note.get_id(), tb_note.getNo(),tb_note.getNote() });// ִ����ӱ�ǩ��Ϣ����
	}

	/**
	 * ���±�ǩ��Ϣ
	 * 
	 * @param tb_note
	 */
	public void update(Tb_note tb_note) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL("update tb_note set note = ? where _id = ? and no=?", new Object[] {
				tb_note.getNote(), tb_note.get_id(),tb_note.getNo() });// ִ���޸ı�ǩ��Ϣ����
	}

	/**
	 * ���ұ�ǩ��Ϣ
	 * 
	 * @param id no
	 * @return
	 */
	public Tb_note find(int id,int no) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery(
				"select _id,no,note from tb_note where _id = ? and no=?",
				new String[] { String.valueOf(id) ,String.valueOf(no)});// ���ݱ�Ų��ұ�ǩ��Ϣ�����洢��Cursor����
		if (cursor.moveToNext())// �������ҵ��ı�ǩ��Ϣ
		{
			// ���������ı�ǩ��Ϣ�洢��Tb_flag����
			return new Tb_note(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")),
					cursor.getString(cursor.getColumnIndex("note")));
		}
		return null;// ���û����Ϣ���򷵻�null
	}

	/**
	 * �h����ǩ��Ϣ
	 * 
	 * @param ids
	 */
	public void detele(Integer... ids) {
		if (ids.length > 0)// �ж��Ƿ����Ҫɾ����id
		{
			StringBuffer sb = new StringBuffer();// ����StringBuffer����
			for (int i = 0; i < ids.length-1; i++)// ����Ҫɾ����id����
			{
				sb.append('?').append(',');// ��ɾ��������ӵ�StringBuffer������
			}
			sb.deleteCharAt(sb.length() - 1);// ȥ�����һ����,���ַ�
			db = helper.getWritableDatabase();// ����SQLiteDatabase����
			// ִ��ɾ����ǩ��Ϣ����
			db.execSQL("delete from tb_note where _id in (?) and no in (" + sb + ")",
					(Object[]) ids);
		}
	}

	/**
	 * ��ȡ��ǩ��Ϣ
	 * 
	 * @param start
	 *            ��ʼλ��
	 * @param count
	 *            ÿҳ��ʾ����
	 * @return
	 */
	public List<Tb_note> getScrollData(int id,int start, int count) {
		List<Tb_note> lisTb_note = new ArrayList<Tb_note>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ���б�ǩ��Ϣ
		Cursor cursor = db.rawQuery("select _id,no,note from tb_note where _id=? order by no limit ?,?",
				new String[] { String.valueOf(id),  String.valueOf(start), String.valueOf(count) });
		while (cursor.moveToNext())// �������еı�ǩ��Ϣ
		{
			// ���������ı�ǩ��Ϣ��ӵ�������
			lisTb_note.add(new Tb_note(
					cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")),
					cursor.getString(cursor.getColumnIndex("note"))));
		}
		return lisTb_note;// ���ؼ���
	}

	/**
	 * ��ȡ�ܼ�¼��
	 * 
	 * @return
	 */
	public long getCount(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select count(no) from tb_note where _id=?",new String[]{String.valueOf(id)});// ��ȡ��ǩ��Ϣ�ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	/**
	 * ��ȡ��ǩ�����
	 * 
	 * @return
	 */
	public int getMaxNo(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select max(no) from tb_note where _id=?",new String[]{String.valueOf(id)});// ��ȡ��ǩ��Ϣ���е������
		while (cursor.moveToLast()) {// ����Cursor�е����һ������
			return cursor.getInt(0);// ��ȡ���ʵ������ݣ��������
		}
		return 0;// ���û�����ݣ��򷵻�0
	}
	public void deleteUserData(int id){//����û�����
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase���� 
		db.execSQL("delete from tb_note where _id=?",new Object[]{id});
	}
}
