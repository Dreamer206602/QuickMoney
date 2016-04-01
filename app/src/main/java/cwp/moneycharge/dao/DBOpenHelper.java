package cwp.moneycharge.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION=2;	//�Ѿ���������
	private static final String DBNAME="cmoneycharge.db";
	
	public DBOpenHelper(Context context){
		super(context,DBNAME,null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//�û���
		db.execSQL("create  table tb_account (_id INTEGER  PRIMARY KEY AUTOINCREMENT  NOT NULL,username varchar(20) ,pwd VARCHAR(50) DEFAULT '000000' NOT NULL)");
		//֧�����ͱ�
		db.execSQL("create  table tb_ptype(_id INTEGER   NOT NULL ,no integer not null,typename varchar(50) )");
		//�������ͱ�
		db.execSQL("create   table tb_itype(_id INTEGER   NOT NULL,no integer  not null ,typename varchar(50) )");
		// ֧����Ϣ��
		db.execSQL("create   table tb_pay (_id INTEGER  NOT NULL,no INTEGER  NOT NULL ,money decimal,time varchar(10),"
				+ "type integer,address varchar(100),mark varchar(200),photo varchar(200),kind varchar(10))");
		
		// ������Ϣ��
		db.execSQL("create   table tb_income (_id INTEGER   NOT NULL,no INTEGER  NOT NULL  ,money decimal,time varchar(10),"
				+ "type integer ,handler varchar(100),mark varchar(200),photo varchar(200),kind varchar(10))");
		
		// ��ǩ��Ϣ��
		db.execSQL("create  table tb_note (_id integer  ,no integer ,note varchar(200))");
		db.execSQL("insert into tb_account(_id,username,pwd) values(100000001,\"Ĭ���û�\",\"000000\")");
		
		//��ʼ������ �������ͱ��
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"1","����"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"2","����"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"3","��Ʊ"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"4","����"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"5","����"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"6","�ֺ�"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"7","��Ϣ"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"8","��ְ"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"9","����"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"10","���"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"11","���ۿ�"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"12","Ӧ�տ�"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"13","������"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"14","����"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"15","���"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"16","����ʶ��"});
		//��ʼ������ ֧�����ͱ��
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"1","���"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"2","���"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"3","���"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"4","ҹ��"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"5","������Ʒ"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"6","������Ʒ"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"7","�·�"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"8","Ӧ��"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"9","���Ӳ�Ʒ"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"10","ʳƷ"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"11","���"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"12","��Ʊ"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"13","���"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"14","����"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"15","����"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"16","����ʶ��"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 if(oldVersion < 2) {	//�������ݿ�
		        db.execSQL("ALTER TABLE tb_income ADD photo varchar(200)");
		        db.execSQL("ALTER TABLE tb_pay ADD photo varchar(200)");
		    }
	}
	
	public void droptable(SQLiteDatabase db){
		db.execSQL("drop table tb_itype");
		db.execSQL("drop table tb_ptype");
		db.execSQL("drop table tb_account");
		db.execSQL("drop table tb_income");
		db.execSQL("drop table tb_pay");
		db.execSQL("drop table tb_note");
	}
}
