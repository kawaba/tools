package jp.kwebs.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * HTTPクライアントクラス
 * RESTful APIとの通信を行うためのユーティリティクラス
 * JSON形式のデータの送受信をサポートします
 * @author T.Kawaba
 * @version 1.0
 */
public class Client {

	/**
	 * 指定されたクラス型のTypeReferenceを作成するヘルパーメソッド
	 * 
	 * @param <T> 型パラメータ
	 * @param type 対象のクラス型
	 * @return TypeReferenceオブジェクト
	 */
	private static <T> TypeReference<T> type(Class<T> type) {
		return new TypeReference<T>() {
		};
	}

	/**
	 * 指定されたURLからJSONデータを取得し、指定されたクラス型のオブジェクトに変換します
	 * 
	 * @param <T> 戻り値の型パラメータ
	 * @param urlString 取得元のURL文字列
	 * @param type 変換先のクラス型
	 * @return 変換されたオブジェクト、エラーが発生した場合はnull
	 */
	public static <T> T read(String urlString, Class<T> type) {

		Object o;
		try {
			o = readJson(urlString, type(type));
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.convertValue(o, type);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 指定されたURLからJSONデータを取得し、TypeReferenceで指定された型のオブジェクトに変換します
	 * 
	 * @param <T> 戻り値の型パラメータ
	 * @param urlString 取得元のURL文字列
	 * @param type 変換先の型を示すTypeReference
	 * @return 変換されたオブジェクト、エラーが発生した場合はnull
	 */
	public static <T> T read(String urlString, TypeReference<T> type) {

		Object o;
		try {
			o = readJson(urlString, type);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.convertValue(o, type);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * HTTPのGETリクエストを送信してJSONレスポンスを取得する内部メソッド
	 * 
	 * @param <T> 型パラメータ
	 * @param urlString リクエスト先のURL文字列
	 * @param typeReference レスポンスを変換する型の参照
	 * @return デシリアライズされたオブジェクト
	 * @throws IOException HTTP通信またはJSON解析でエラーが発生した場合
	 */
	private static <T> Object readJson(String urlString, TypeReference<T> typeReference) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// レスポンス取得
		try (InputStream in = con.getInputStream(); Reader reader = new InputStreamReader(in)) {

			ObjectMapper mapper = new ObjectMapper(); // 新しいインスタンスを作成
			mapper.registerModule(new JavaTimeModule()); // モジュールを登録
			Object obj = mapper.readValue(reader, typeReference);
			return obj;

		}
	}
	
	/**
	 * 指定されたURLにオブジェクトをJSON形式でPOSTリクエストとして送信します
	 * 
	 * @param <T> 送信するオブジェクトの型パラメータ
	 * @param urlString 送信先のURL文字列
	 * @param obj 送信するオブジェクト
	 * @return HTTPレスポンスコード、エラーが発生した場合は500
	 */
	public static <T> int write(String urlString, T obj) {

		int status;
		try {
			status = writeJson(urlString, obj);
			return status;

		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		}
	}
	
	/**
	 * HTTPのPOSTリクエストでJSONデータを送信する内部メソッド
	 * 
	 * @param <T> 送信するオブジェクトの型パラメータ
	 * @param urlString 送信先のURL文字列
	 * @param obj 送信するオブジェクト
	 * @return HTTPレスポンスコード
	 * @throws IOException HTTP通信またはJSON変換でエラーが発生した場合
	 */
	private static <T> int writeJson(String urlString, T obj) throws IOException {
		
		// 接続を開く
		URL url = new URL(urlString); 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // POST リクエストを設定
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
		
		 // objを JSON に変換
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // モジュールを登録
        String json = mapper.writeValueAsString(obj);
        
        // リクエストボディに JSON を書き込む
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // レスポンスコードを取得
        int code = conn.getResponseCode();
        conn.disconnect();
        return code;
	}	

	/**
	 * クライアントクラスのテストメソッド
	 * 各種APIの動作確認を行います
	 * 
	 * @param args コマンドライン引数（未使用）
	 * @throws Exception 処理中にエラーが発生した場合
	 */
	public static void main(String[] args) throws Exception {

		String url = "http://localhost:8080/addbook";
		Book book = new Book("材料工学", LocalDate.of(2020, 4, 1), "田中太","Science",5000,true);
		int status = Client.write(url, book);
		System.out.println("status=" + status);
		
		
		// ウェブサービスを呼び出して通貨レートを得る
		// 
		url = "https://openexchangerates.org/api/latest.json?app_id=2c2a442738aa4f37bf82054c3369eb14&base=USD&symbols=JPY,EUR,KRW";

		Exchange ratedata = Client.read(url, Exchange.class);
		System.out.println(ratedata);

		///////////
		// ローカルでmijserviceプロジェクトを起動して、レコードを取得する

        url = "http://localhost:8080/books";
        
		var books = Client.read(url, new TypeReference<List<Book>>() {});
		for (Book bk : books) {
			System.out.println(bk);
		}
		
		

	}

}

/**
 * 通貨レート情報を格納するクラス
 * Open Exchange Rates APIのレスポンスJSONを受け取るための汎用クラス
 */
class Exchange {
	/** 免責事項 */
	private String disclaimer;
	
	/** ライセンス情報 */
	private String license;
	
	/** タイムスタンプ（Unix時間） */
	private long timestamp;
	
	/** 基準通貨 */
	private String base;
	
	/** 通貨レートのマップ（通貨コード -> レート） */
	private HashMap<String, Double> rates;

	/**
	 * 免責事項を取得します
	 * 
	 * @return 免責事項の文字列
	 */
	public String getDisclaimer() {
		return disclaimer;
	}

	/**
	 * 免責事項を設定します
	 * 
	 * @param disclaimer 設定する免責事項の文字列
	 */
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	/**
	 * ライセンス情報を取得します
	 * 
	 * @return ライセンス情報の文字列
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * ライセンス情報を設定します
	 * 
	 * @param license 設定するライセンス情報の文字列
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * タイムスタンプを取得します
	 * 
	 * @return Unix時間形式のタイムスタンプ
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * タイムスタンプを設定します
	 * 
	 * @param timestamp 設定するUnix時間形式のタイムスタンプ
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 基準通貨を取得します
	 * 
	 * @return 基準通貨のコード（例：USD）
	 */
	public String getBase() {
		return base;
	}

	/**
	 * 基準通貨を設定します
	 * 
	 * @param base 設定する基準通貨のコード
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * 通貨レートのマップを取得します
	 * 
	 * @return 通貨コードをキー、レートを値とするHashMap
	 */
	public HashMap<String, Double> getRates() {
		return rates;
	}

	/**
	 * 通貨レートのマップを設定します
	 * 
	 * @param rates 設定する通貨レートのHashMap
	 */
	public void setRates(HashMap<String, Double> rates) {
		this.rates = rates;
	}

	/**
	 * オブジェクトの文字列表現を返します
	 * 
	 * @return 基準通貨とレート情報を含む文字列
	 */
	@Override
	public String toString() {
		return "base=" + base + ", \nrates=" + rates;
	}
}
