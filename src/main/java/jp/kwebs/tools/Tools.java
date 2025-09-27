package jp.kwebs.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
/**
 * 便利なツールクラス
 *
 */
public class Tools {
	/**
	 * 時間のかかるI/Oタスクを指定された秒数だけ実行する
	 * @param seconds　実行する秒数
	 */
	public static void time_consuming_io_task(double seconds) {
		//int count=0;
		try {
			URL resourceUrl = Tools.class.getClassLoader().getResource("dummy.xml");
			long startTime = System.nanoTime();
			while ((System.nanoTime() - startTime) / 1e9 < seconds) {
				//count++;
				for (int i = 0; i < 5000; i++) {
					try (InputStream is = resourceUrl.openStream()) {
						is.readAllBytes();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			//System.out.println("count=" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * タイムスタンプを日付に直した文字列を返す
	 * @param timestamp
	 * @return　yyyy-MM-dd hh:mm の文字列
	 */
    public static String timestamp2date(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return dateTime.toLocalDate() + " " + dateTime.toLocalTime();
    }
    /* *************************** いろいろな出力 **************************************/
    /**
     * messageをファイルに追記モードで書き込む
     * @param message 書き込むメッセージ
     */
	public static void toFile(String message) {
		toFile(Path.of("log.txt"), message);
	}
    /**
     * messageを指定したStringで指定したパスに追記モードで書き込む
     * @param message 書き込むメッセージ
     */	
	public static void toFile(String path, String message) {
		toFile(Path.of(path), message);
	}
	/**
	 * messageを指定したパスに追記モードで書き込む
	 * @param path
	 * @param message
	 */
	public static void toFile(Path path, String message) {
		try {
			Files.writeString(path, message+"\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.err.println("ファイルの書き込み中にエラーが発生しました: " + e.getMessage());
		}
	}
	/**
	 * コンソールにmessageを表示する
	 * @param message
	 */
	public static void toConsole(String message) {
		System.out.println(message);
	}
    /* *************************** いろいろな入力 **************************************/
	
	/**
	 * コンソールから文字列を入力する
	 * @return  入力した文字列
	 */
	public static String fromConsole() {	
		return Typing.stringValue();
	}
	
	/**
	 * ファイルから文字列を読み込む
	 * @param path ファイルパス
	 * @return	ファイルの内容
	 */
	public static String fromFile(String path) {
		return fromFile(Path.of(path));
	}
	/**
	 * ファイルから文字列を読み込む
	 * @param path ファイルパス 
	 * @return ファイルの内容.例外が発生した場合は"This is dummy data."を返す
	 */
	public static String fromFile(Path path) {
		try {
			return Files.readString(path);
		}catch(IOException e) {
            return "This is dummy data.";
        }
	}
	
    // テスト
    public static void main(String[] args) {
		main2();
	}
    
    private static void main1() {
    	System.out.println(timestamp2date(1710939600L));
    }
    private static void main2() {
        	time_consuming_io_task(1);
        	System.out.println("Done");
    }
    
    /* *************************** ディレクトリの再帰的な削除 ***********************************/
   
    public static void deleteDirectory(Path path) throws IOException {
        // パスが存在しない場合は何もしないで戻る
        if (Files.notExists(path)) {
            return;
        }

        // ディレクトリの場合は中身を先に削除してからディレクトリ本体を削除
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
                for (Path child : ds) {
                	deleteDirectory(child);
                }
            }
        }
        
        // ファイルまたは空になったディレクトリを削除
        Files.delete(path);
    }
}