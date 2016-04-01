/**
 * 
 */
package cwp.moneycharge.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author cwpcc
 * 
 */
public class Tb_income {
	// _id INTEGER NOT NULL PRIMARY KEY,no not null integer ,money decimal,time
	// varchar(10),
	// type integer ,handler varchar(100),mark varchar(200))
	private int _id;// �洢�û�id
	private int no;// �洢������
	private double money;// �洢������
	private String time;// �洢����ʱ��
	private int type;// �洢�������id
	private String handler;// �洢�����ַ
	private String mark;// �洢���뱸ע
	private String kind;// �洢������֧���
	private String photo;// �洢֧����ע

	public Tb_income(int id, int no, double d, String time, int type,
			String handler, String mark, String photo, String kind) {

		super();
		this._id = id;// Ϊ�û�id
		this.no = no;// Ϊ�����Ÿ�ֵ
		this.money = d;// Ϊ�����ֵ
		this.time = time;// Ϊ����ʱ�丳ֵ
		this.type = type;// Ϊ�������ֵ
		this.handler = handler;// Ϊ���븶���ֵ
		this.mark = mark;// Ϊ���뱸ע��ֵ
		this.kind = kind;// Ϊ���뱸ע��ֵ
		this.photo = photo;// Ϊ֧����ע��ֵ
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Tb_income() {
		super();
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public double getMoney() {
		return money;
	}

	public String getMoney2() {
		double a = money;
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(a);
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

}
