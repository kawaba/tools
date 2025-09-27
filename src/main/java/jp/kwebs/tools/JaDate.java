package jp.kwebs.tools;
/*
 * 表示用の和暦のクラス
 *
 */
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class JaDate {

	JapaneseDate date;		// 和暦
	LocalDate localdate;	// 西暦

	public JaDate(LocalDate date) {
		this.localdate = date;
		this.date = JapaneseDate.from(date);
	}

	/**
	 * 和暦クラスのインスタンスを返す
	 *
	 * @param date
	 * @return
	 */
	public static JaDate from(LocalDate date) {
		return new JaDate(date);
	}

	/**
	 * 和暦文字列を返す
	 *
	 * @param date
	 * @return
	 */
	public static String wareki(LocalDate date) {
		return JaDate.from(date).wareki();
	}

	/**
	 * 和暦文字列を返す(曜日は1文字）
	 *
	 * @param date
	 * @return
	 */
	public static String warekiS(LocalDate date) {
		return JaDate.from(date).warekiS();
	}

	/**
	 * 和暦文字列を返す（日まで）
	 *
	 * @param date
	 * @return
	 */
	public static String warekiD(LocalDate date) {
		return JaDate.from(date).warekiD();
	}

	/**
	 * 和暦文字列を返す（月まで）
	 *
	 * @param date
	 * @return
	 */
	public static String warekiM(LocalDate date) {
		return JaDate.from(date).warekiM();
	}

	/**
	 * 和暦文字列を返す（年だけ）
	 *
	 * @param date
	 * @return
	 */
	public static String warekiY(LocalDate date) {
		return JaDate.from(date).warekiY();
	}

	/**
	 * 和暦年号を返す
	 *
	 * @param date
	 * @return
	 */
	public static String nengo(LocalDate date) {
		return JaDate.from(date).nengo();
	}

	/**
	 * 和暦年を返す
	 *
	 * @param date
	 * @return
	 */
	public static int nen(LocalDate date) {
		return JaDate.from(date).nen();
	}

	/**
	 * 月を返す
	 *
	 * @param date
	 * @return
	 */
	public static int tuki(LocalDate date) {
		return JaDate.from(date).tuki();
	}

	/**
	 * 日を返す
	 *
	 * @param date
	 * @return
	 */
	public static int hi(LocalDate date) {
		return JaDate.from(date).hi();
	}

	/**
	 * 曜日を返す
	 *
	 * @param date
	 * @return
	 */
	public static String youbi(LocalDate date) {
		return JaDate.from(date).youbi();
	}

	/**
	 * 曜日（１文字）を返す
	 *
	 * @param date
	 * @return
	 */
	public static String youbiS(LocalDate date) {
		return JaDate.from(date).youbiS();
	}

	////////////////////////////////////////////////////////////////////
	/**
	 * 和暦を返す
	 *
	 * @return
	 */
	public String wareki() {
		return nengo() + s00(nen()) + "年" + s00(tuki()) + "月" + s00(hi()) + "日" + " " + youbi();
	}

	/**
	 * 和暦を返す（曜日は1文字）
	 *
	 * @return
	 */
	public String warekiS() {
		return nengo() + s00(nen()) + "年" + s00(tuki()) + "月" + s00(hi()) + "日" + " " + youbiS();
	}

	/**
	 * 和暦を返す（日まで）
	 *
	 * @return
	 */
	public String warekiD() {
		return nengo() + s00(nen()) + "年" + s00(tuki()) + "月" + s00(hi()) + "日";
	}

	/**
	 * 和暦を返す(月まで）
	 *
	 * @return
	 */
	public String warekiM() {
		return nengo() + s00(nen()) + "年" + s00(tuki()) + "月";
	}

	/**
	 * 和暦を返す（年まで）
	 *
	 * @return
	 */
	public String warekiY() {
		return nengo() + s00(nen()) + "年";
	}

	/**
	 * 年号を返す
	 *
	 * @return
	 */
	public String nengo() {

		char nengo = date.getEra().toString().charAt(0);
		if(nengo == 'M') {
			return "明治";
		} else if(nengo == 'T') {
			return "大正";
		} else if(nengo == 'S') {
			return "昭和";
		} else {
			return "平成";
		}
	}

	public String s00(int n) {
		return (n > 9 ? "" : "0") + n;
	}

	/**
	 * 和暦での年を返す
	 *
	 * @return
	 */
	public int nen() {
		return date.get(ChronoField.YEAR_OF_ERA);
	}

	/**
	 * 月を返す
	 *
	 * @return
	 */
	public int tuki() {
		return localdate.getMonthValue();
	}

	/**
	 * 日を返す
	 *
	 * @return
	 */
	public int hi() {
		return localdate.getDayOfMonth();
	}

	/**
	 * 曜日（漢字）を返す
	 *
	 * @return
	 */
	public String youbi() {
		return localdate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.JAPAN);
	}

	/**
	 * 曜日（漢字 1文字）を返す
	 *
	 * @return
	 */
	public String youbiS() {
		return localdate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPAN);
	}

	// サンプル
	public static void main(String[] args) {
		//LocalDate date = LocalDate.of(2030, 1, 13);
		int y = Input.getInt("年");
		int m = Input.getInt("月");
		int d = Input.getInt("日");
		m = (m == 0) ? 1 : m;
		d = (d == 0) ? 1 : d;
		LocalDate date = LocalDate.of(y, m, d);

		System.out.println(date);
		JaDate jdate = JaDate.from(date);

		//////////
		//////////
		System.out.println("/////// インスタンスメソッド //////\n");
		//////////
		//////////

		System.out.println(jdate.wareki());
		System.out.println(jdate.warekiS());
		System.out.println(jdate.warekiD());
		System.out.println(jdate.warekiM());
		System.out.println(jdate.warekiY());

		System.out.println(jdate.nengo());
		System.out.println(jdate.nen());
		System.out.println(jdate.tuki());
		System.out.println(jdate.hi());
		System.out.println(jdate.youbi());
		System.out.println(jdate.youbiS());

		//////////
		//////////
		System.out.println("/////// クラスメソッド //////\n");
		//////////
		//////////

		System.out.println(JaDate.wareki(date));
		System.out.println(JaDate.warekiS(date));
		System.out.println(JaDate.warekiD(date));
		System.out.println(JaDate.warekiM(date));
		System.out.println(JaDate.warekiY(date));
		System.out.println(JaDate.nengo(date));
		System.out.println(JaDate.nen(date));
		System.out.println(JaDate.tuki(date));
		System.out.println(JaDate.hi(date));
		System.out.println(JaDate.youbi(date));
		System.out.println(JaDate.youbiS(date));

	}

}
