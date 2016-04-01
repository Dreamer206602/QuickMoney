package com.cwp.cmoneycharge;

/**
 *  @author loiy
 *  www.agrilink.cn
 *  2012.11.30   
 */
import java.util.Hashtable;

public class DigitUtil {

	/**
	 * �����е������ַ�
	 */
	private static final Character[] SCDigits = { '��', 'һ', '��', '��', '��', '��',
			'��', '��', '��', '��', '��' };

	/**
	 * ����������
	 */
	private static final Character[] araDigits = { '0', '1', '2', '2', '3',
			'4', '5', '6', '7', '8', '9' };

	/**
	 * ����������
	 */
	private static Hashtable<Character, Character> araHash = new Hashtable<Character, Character>();

	/**
	 * ����������
	 */
	private static Hashtable<Character, Integer> YWQBSHash = new Hashtable<Character, Integer>();

	static {
		for (int i = 0; i < SCDigits.length; i++) {
			araHash.put(SCDigits[i], araDigits[i]);
		}
		YWQBSHash.put('��', 100000000);
		YWQBSHash.put('��', 10000);
		YWQBSHash.put('ǧ', 1000);
		YWQBSHash.put('��', 100);
		YWQBSHash.put('ʮ', 10);
	}

	/**
	 * @param remain
	 *            ʣ�¶��ٸ��ַ�
	 * @param curchar
	 *            ��ǰ�������ַ�
	 */
	private static int ywqbs(int num, Boolean bool, int no, int remain,
			char curchar, char lastshow) {
		if (num == 0) {
			num += no;
			return num;
		} else {
			String numString = String.valueOf(num); // ��numת����String
			String last = numString.substring(numString.length() - 1,
					numString.length()); // ��ȡ���һ���ַ�
			String exceptlast = numString.substring(0, numString.length() - 1); // ��ȡ�������һ���ַ������ַ���
			if (exceptlast.length() == 0) { // ˵��num�Ǹ�λֵ
				num = Integer.parseInt(last) * no;
				return num;
			} else {
				/***** �������������Щ�ַ�[ʮ��ǧ����] start *****/
				if (bool && YWQBSHash.get(curchar) > YWQBSHash.get(lastshow)) { // ˵���ϸ��ַ�����[ʮ��ǧ����]
					num *= no;
					return num;
				}
				/***** �������������Щ�ַ�[ʮ��ǧ����] end *****/
				/***** ���������һ���ַ� start *****/
				/**** ����������(һǧ���ٶ�ʮһ��) ****/
				if (remain == 1
						&& YWQBSHash.get(curchar) > YWQBSHash.get(lastshow)) {
					num *= YWQBSHash.get(curchar);
					return num;
				}
				/**** ����������(һǧ���ٶ�ʮһ��) ****/
				/***** ���������һ���ַ� end *****/
				/***** last���Ϊ0 start *****/
				if (last.equals("0")) { // ѭ�������һλ�ַ�,last����0
					last = "1"; //
				}
				/***** last���Ϊ0 end *****/
				exceptlast = exceptlast + "0"; // ȱ�����һλ,��Ҫ����
				num = Integer.parseInt(exceptlast) + Integer.parseInt(last)
						* no;
				return num;
			}
		}
	}

	public static int parse(String word) {
		int num = 0;
		char lastchar = 'һ'; // �ϴ��ַ�,�ַ���ָ[ʮ��ǧ����],Ĭ�������дһ��'һ'
		char lastshow = 'һ'; // �ϴγ��ֵ��ַ�,�ַ���ָ[ʮ��ǧ����],Ĭ�������дһ��'һ'
		char[] ch = word.toCharArray();
		Boolean bool = false; // �Ƿ���������[ʮ��ǧ����]
		for (int i = 0; i < ch.length; i++) {
			Character find = araHash.get(ch[i]);// ��ȡ��������������[1,2,3...]
			if (find != null) {
				num += Integer.parseInt(String.valueOf(find.charValue()));
				bool = false;
				lastchar = 'һ'; // �ָ���Ĭ��ֵ
				continue;
			} else if (ch[i] == 'ʮ') {
				num = ywqbs(num, bool, 10, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = 'ʮ';
				lastshow = 'ʮ';
			} else if (ch[i] == 'ǧ') {
				num = ywqbs(num, bool, 1000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = 'ǧ';
				lastshow = 'ǧ';
			} else if (ch[i] == '��') {
				num = ywqbs(num, bool, 100, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '��';
				lastshow = '��';
			} else if (ch[i] == '��') {
				num = ywqbs(num, bool, 10000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '��';
				lastshow = '��';
			}
		}
		ch = null;
		return num;
	}

	// public static void main(String args[]) {
	// String wordes[] = {
	// "һǧ���ٶ�ʮһ��",
	// "һǧ���ٶ�ʮһ",
	// "һǧ��ʮ",
	// "һ����һ��",
	// "һǧ��ʮһ",
	// "һ����һ��ʮһ",
	// "һǧ����ʮ��",
	// "һǧ��",
	// "��ʮ��"
	// };
	// for(int i = 0; i < wordes.length; i++) {
	// DigitUtil t = new DigitUtil();
	// int num = t.parse(wordes[i]);
	// System.out.println(wordes[i] + " " + num);
	// }
	// }

}