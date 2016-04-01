package cwp.moneycharge.dao;

import cwp.moneycharge.model.Datapicker;
import cwp.moneycharge.model.Tb_itype; 
import cwp.moneycharge.dao.DBOpenHelper;
import java.util.List;
import java.util.ArrayList;

import com.cwp.cmoneycharge.R;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;


/**
 * @author cwpcc
 *
 */ 
public class ItypeDAO {
//(_id INTEGER  NOT NULL PRIMARY KEY,no not null int ,typename varchar(50) 
	private DBOpenHelper helper;// ����DBOpenHelper����
	private SQLiteDatabase db;// ����SQLiteDatabase����

	int[] imageId = new int[] { R.drawable.icon_zysr_gzsr,
			R.drawable.icon_qtsr_zjsr, R.drawable.icon_zysr_tzsr,
			R.drawable.icon_qtsr, R.drawable.icon_qtzx,
			R.drawable.icon_jrbx_xfss, R.drawable.icon_zysr_lxsr,
			R.drawable.icon_zysr_jzsr, R.drawable.icon_zysr_jjsr,
			R.drawable.icon_lyyp_hwzb, R.drawable.icon_qtsr_jysd,
			R.drawable.icon_qtsr_jysd, R.drawable.icon_qtsr_jysd,
			R.drawable.icon_qtsr, R.drawable.icon_qtsr_ljsr, R.drawable.yysb };
	
	public ItypeDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
	}

/**	
 * ������������
 * @param Tb_itype
*/
	public void add(Tb_itype tb_itype){
		db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
		db.execSQL("insert into tb_itype (_id,no,typename) values (?,?,?)", new Object[] {
				tb_itype.get_id(),tb_itype.getNo(),tb_itype.getTypename()});// ִ������������Ͳ���
		
	}
	/**	
	 * �޸���������
	 * @param Tb_itype
	*/	
		public void modify(Tb_itype tb_itype){
			db=helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
			db.execSQL("update tb_itype set typename = ? where _id = ? and no=?", new Object[] {
					tb_itype.get_id(), tb_itype.getNo(),tb_itype.getTypename() });// ִ���޸��������Ͳ���
		}
	/**	
	 * ɾ����������
	 * @param  ids
	*/	
		public void delete(Integer... ids){
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
				db.execSQL("delete from tb_itype where _id in (?) and no in (" + sb + ")",
						(Object[]) ids);
			}
		}

		public void deleteByName(int id,String typename){
			db = helper.getWritableDatabase();// ����SQLiteDatabase����
			// ִ��ɾ����ǩ��Ϣ����
			db.execSQL("delete from tb_itype where _id =? and typename=?", new Object[] { id, typename});
		}

		public void deleteById(int id){
			db = helper.getWritableDatabase();// ����SQLiteDatabase����
			// ִ��ɾ����ǩ��Ϣ����
			db.execSQL("delete from tb_itype where _id =? ", new Object[] { id});
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
		public List<Tb_itype> getScrollData(int id,int start, int count) {
			List<Tb_itype> lisTb_itype = new ArrayList<Tb_itype>();// �������϶���
			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
			// ��ȡ���б�ǩ��Ϣ
			Cursor cursor = db.rawQuery("select * from tb_itype where _id=? order by no limit ?,?",
					new String[] { String.valueOf(id),  String.valueOf(start), String.valueOf(count) });
			while (cursor.moveToNext())// �������еı�ǩ��Ϣ
			{
				// ���������ı�ǩ��Ϣ��ӵ�������
				lisTb_itype.add(new Tb_itype(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getInt(cursor.getColumnIndex("no")),
						cursor.getString(cursor.getColumnIndex("typename"))));
			}
			return lisTb_itype;// ���ؼ���
		}
		
		/**
		 * ��ȡ�ܼ�¼��
		 * 
		 * @return
		 */
		public long getCount() {
			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
			Cursor cursor = db.rawQuery("select count(no) from tb_itype", null);// ��ȡ�������͵ļ�¼��
			if (cursor.moveToNext())// �ж�Cursor���Ƿ�������
			{
				return cursor.getLong(0);// �����ܼ�¼��
			}
			return 0;// ���û�����ݣ��򷵻�0
		}
		public long getCount(int id) {
			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
			Cursor cursor = db
					.rawQuery("select count(no) from tb_itype where _id=?", new String[]{String.valueOf(id)});// ��ȡ������Ϣ�ļ�¼��
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
			Cursor cursor = db.rawQuery("select max(no) from tb_itype", null);// ��ȡ�������ͱ��е������
			while (cursor.moveToLast()) {// ����Cursor�е����һ������
				return cursor.getInt(0);// ��ȡ���ʵ������ݣ��������
			}
			return 0;// ���û�����ݣ��򷵻�0
		}
		/**
		 * ��ȡ����������
		 * param id
		 * @return
		 * */
		public List<String> getItypeName(int id){
			List<String> lisString = new ArrayList<String>();// �������϶���
			db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select typename from tb_itype where _id=?",new String[]{String.valueOf(id) } );
			while (cursor.moveToNext()) {// ����Cursor�е����һ������
				lisString.add(cursor.getString(cursor.getColumnIndex("typename")));
				
			}
			return lisString;
			
		}
		public String getOneName(int id,int no){
			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
			Cursor cursor = db.rawQuery("select typename from tb_itype where _id=? and no=?",new String[]{String.valueOf(id),String.valueOf(no) } ); 
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
		
		//��ȡָ��ʱ�������ݿ�����
		
		public List<Datapicker> getDataOnDay(int id,String date1,String date2){
			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase���� 
			List<Datapicker> datapicker = new ArrayList<Datapicker>();// �������϶���
			String sql= "'"+date1+"' and '"+date2+"'";		
			Cursor cursor = db.rawQuery("select no, time,sum(money) as tmoney from tb_pay where time between "+sql+" and _id =? group BY time order by time",new String[]{String.valueOf(id)} );// ��ȡ֧����Ϣ���е������
			while (cursor.moveToNext())// �������е�֧����Ϣ
			{		
			 
				// ����������֧����Ϣ��ӵ�������
					datapicker.add(new Datapicker( 
						cursor.getInt(cursor.getColumnIndex("no")),
						cursor.getDouble(cursor.getColumnIndex("tmoney")),
						cursor.getString(cursor.getColumnIndex("time"))));
			}
			return datapicker;// ���ؼ���
				// ����������֧����Ϣ��ӵ�������
				 
			} // ���ؼ���
		 
		public void modifyByName(int id,String old,String now){
			db = helper.getWritableDatabase();
			db.execSQL("update tb_itype set typename = ? where _id = ? and typename=?", new Object[] {
					id, now,old});// ִ���޸��������Ͳ���
		}
		
		public void initData(int id) { 

			db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����    
			
			// ��ǩ��Ϣ��  
			db.execSQL("delete from tb_itype where _id=?",new String[]{String.valueOf(id)}); // ȷ���޸�id
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"1","����"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"2","�н�"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"3","��Ʊ"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"4","����"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"5","����"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"6","�ֺ�"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"7","��Ϣ"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"8","��ְ"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"9","����"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"10","���"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"11","���ۿ�"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"12","Ӧ�տ�"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"13","������"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"14","����"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"15","���"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"16","����ʶ��"});
		}
}
