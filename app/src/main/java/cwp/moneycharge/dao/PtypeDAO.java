package cwp.moneycharge.dao;

import cwp.moneycharge.model.Tb_ptype;
import cwp.moneycharge.dao.DBOpenHelper;
import java.util.List;
import java.util.ArrayList;

import com.cwp.cmoneycharge.R;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;

public class PtypeDAO {
	// (_id INTEGER NOT NULL PRIMARY KEY,no not null integer ,typename
	// varchar(50)
	private DBOpenHelper helper;// ����DBOpenHelper����
	private SQLiteDatabase db;// ����SQLiteDatabase����

	int[] imageId = new int[] { R.drawable.icon_spjs_zwwc,
			R.drawable.icon_spjs_zwwc, R.drawable.icon_spjs_zwwc,
			R.drawable.icon_spjs_zwwc, R.drawable.icon_jjwy_rcyp,
			R.drawable.icon_xxyl_wg, R.drawable.icon_yfsp,
			R.drawable.icon_rqwl_slqk, R.drawable.icon_jltx_sjf,
			R.drawable.icon_spjs, R.drawable.icon_jrbx_ajhk,
			R.drawable.icon_jrbx, R.drawable.icon_xcjt_dczc,
			R.drawable.icon_qtzx, R.drawable.icon_jrbx_lxzc, R.drawable.yysb };

	public PtypeDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}

	/**
	 * ������������
	 * 
	 * @param tb_ptype
	 */
	public void add(Tb_ptype tb_ptype) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL(
				"insert into tb_ptype (_id,no,typename) values (?,?,?)",
				new Object[] { tb_ptype.get_id(), tb_ptype.getNo(),
						tb_ptype.getTypename() });// ִ�����֧�����Ͳ���

	}

	/**
	 * �޸���������
	 * 
	 * @param tb_ptype
	 */
	public void modify(Tb_ptype tb_ptype) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL(
				"update Tb_ptype set typename = ? where _id = ? and no=?",
				new Object[] { tb_ptype.get_id(), tb_ptype.getNo(),
						tb_ptype.getTypename() });// ִ���޸�֧�����Ͳ���
	}

	public void modifyByName(int id, String old, String now) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update Tb_ptype set typename = ? where _id = ? and typename=?",
				new Object[] { id, now, old });// ִ���޸��������Ͳ���
	}

	/**
	 * ɾ����������
	 * 
	 * @param ids
	 */
	public void delete(Integer... ids) {
		if (ids.length > 0)// �ж��Ƿ����Ҫɾ����id
		{
			StringBuffer sb = new StringBuffer();// ����StringBuffer����
			for (int i = 0; i < ids.length - 1; i++)// ����Ҫɾ����id����
			{
				sb.append('?').append(',');// ��ɾ��������ӵ�StringBuffer������
			}
			sb.deleteCharAt(sb.length() - 1);// ȥ�����һ����,���ַ�
			db = helper.getWritableDatabase();// ����SQLiteDatabase����
			// ִ��ɾ����ǩ��Ϣ����
			db.execSQL("delete from tb_ptype where _id in (?) and no in (" + sb
					+ ")", (Object[]) ids);
		}
	}

	public void deleteByName(int id, String typename) {
		db = helper.getWritableDatabase();// ����SQLiteDatabase����
		// ִ��ɾ����ǩ��Ϣ����
		db.execSQL("delete from tb_ptype where _id =? and typename=?",
				new Object[] { id, typename });
	}

	public void deleteById(int id) {
		db = helper.getWritableDatabase();// ����SQLiteDatabase����
		// ִ��ɾ����ǩ��Ϣ����
		db.execSQL("delete from tb_ptype where _id =? ", new Object[] { id });
	}

	/**
	 * ��ȡ����������Ϣ
	 * 
	 * @param start
	 *            ��ʼλ��
	 * @param count
	 *            ÿҳ��ʾ����
	 * @return
	 */
	public List<Tb_ptype> getScrollData(int id, int start, int count) {
		List<Tb_ptype> lisTb_ptype = new ArrayList<Tb_ptype>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		// ��ȡ���б�ǩ��Ϣ
		Cursor cursor = db.rawQuery(
				"select * from tb_ptype where _id=? order by no limit ?,?",
				new String[] { String.valueOf(id), String.valueOf(start),
						String.valueOf(count) });
		while (cursor.moveToNext())// �������еı�ǩ��Ϣ
		{
			// ���������ı�ǩ��Ϣ��ӵ�������
			lisTb_ptype.add(new Tb_ptype(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getString(cursor
					.getColumnIndex("typename"))));
		}
		return lisTb_ptype;// ���ؼ���
	}

	/**
	 * ��ȡ�ܼ�¼��
	 * 
	 * @return
	 */
	public long getCount() {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select count(no) from tb_ptype", null);// ��ȡ֧�����͵ļ�¼��
		if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
		{
			return cursor.getLong(0);// �����ܼ�¼��
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	public long getCount(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery(
				"select count(no) from tb_ptype where _id=?",
				new String[] { String.valueOf(id) });// ��ȡ������Ϣ�ļ�¼��
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
	public int getMaxId() {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery("select max(no) from tb_ptype", null);// ��ȡ�������ͱ��е������
		while (cursor.moveToLast()) {// ����Cursor�е����һ������
			return cursor.getInt(0);// ��ȡ���ʵ������ݣ��������
		}
		return 0;// ���û�����ݣ��򷵻�0
	}

	/**
	 * ��ȡ���������� param id
	 * 
	 * @return
	 * */
	public List<String> getPtypeName(int id) {
		List<String> lisCharSequence = new ArrayList<String>();// �������϶���
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery(
				"select typename from tb_ptype where _id=?",
				new String[] { String.valueOf(id) });// ��ȡ�������ͱ��е������
		while (cursor.moveToNext()) {// ����Cursor�е����һ������
			lisCharSequence.add(cursor.getString(cursor
					.getColumnIndex("typename")));

		}
		return lisCharSequence;// ���û�����ݣ��򷵻�0

	}

	public String getOneName(int id, int no) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		Cursor cursor = db.rawQuery(
				"select typename from tb_ptype where _id=? and no=?",
				new String[] { String.valueOf(id), String.valueOf(no) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("typename"));
		}
		return "";
	}

	public int getOneImg(int id, int no) {
		if (imageId.length < no) {
			return imageId[14];
		}
		return imageId[no - 1];
	}

	public void initData(int id) {
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL("delete from tb_ptype where _id=?",
				new String[] { String.valueOf(id) }); // ȷ���޸�id
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "1", "���" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "2", "���" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "3", "���" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "4", "ҹ��" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "5", "������Ʒ" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "6", "������Ʒ" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "7", "�·�" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "8", "Ӧ��" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "9", "���Ӳ�Ʒ" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "10", "ʳƷ" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "11", "���" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "12", "��Ʊ" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "13", "���" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "14", "����" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "15", "����" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "16", "����ʶ��" });
	}
}
