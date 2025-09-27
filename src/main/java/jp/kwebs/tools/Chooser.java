package jp.kwebs.tools;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * ファイルやディレクトリを選択するためのダイアログを提供するユーティリティクラス。
 * SwingのJFileChooserを使用してファイル選択ダイアログやディレクトリ選択ダイアログを表示します。
 * 
 * @author T.Kawaba
 * @version 1.0
 */
public class Chooser {
	
	/** 選択されたファイルのパスリスト */
	private List<String> flist;
	
	/** 選択されたディレクトリのパス */
	private String dir;
	
	/**
	 * 選択されたファイルのパスリストを取得します。
	 * 
	 * @return 選択されたファイルのパスリスト
	 */
	public List<String> getFlist() {
		return flist;
	}
	
	/**
	 * 選択されたディレクトリのパスを取得します。
	 * 
	 * @return 選択されたディレクトリのパス
	 */
	public String getDir() {
		return dir;
	}
	
	/**
	 * ファイル選択ダイアログを表示し、選択されたファイルのパスをflistに設定します。
	 * OS別にLook and Feelを設定し、複数ファイルの選択が可能です。
	 * ダイアログ処理完了後にnotify()を呼び出して待機中のスレッドを再開します。
	 */
	private void fileDialog() {
        /*
		try {
            // Windowsé¢¨ã®Look and Feelã‚'è¨­å®š
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        */
        String osName = System.getProperty("os.name").toLowerCase();
        try {
            if (osName.contains("mac")) {
                // Macの場合
                UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
            } else if (osName.contains("windows")) {
                // Windowsの場合
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (osName.contains("linux")) {
                // Linuxの場合
                // GTK+ルックアンドフィールを試みる
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else {
                // その他のOSの場合はデフォルトのクロスプラットフォーム LookAndFeelを使用
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e) {
            // 例外が発生した場合は、デフォルトのルックアンドフィールにフォールバック
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
        
        fileChooser.setDialogTitle("ファイルを選択してください");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            List<String> filePaths = new ArrayList<>();
            for (File file : selectedFiles) {
                filePaths.add(file.getAbsolutePath());
            }
            flist = filePaths;
        } else {
            flist = null;
        }
		/////////////////////////////////////////////////////
		// 呼び出したスレッドを再開させるためにnotify
        synchronized (this) {
        	this.notify();
        }
        /////////////////////////////////////////////////////	         
    }
	
	/**
	 * ディレクトリ選択ダイアログを表示し、選択されたディレクトリのパスをdirに設定します。
	 * WindowsのLook and Feelを使用してダイアログを表示します。
	 * ダイアログ処理完了後にnotify()を呼び出して待機中のスレッドを再開します。
	 */
	private void directoryDialog() {
        try {
            // WindowsのLook and Feelを設定
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
        
        fileChooser.setDialogTitle("ディレクトリを選択してください");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            dir = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            dir = null;
        }
		/////////////////////////////////////////////////////
		// 呼び出したスレッドを再開させるためにnotify
        synchronized (this) {
        	this.notify();
        }
        /////////////////////////////////////////////////////	            
    }
	
	/**
	 * 指定されたファイルパスの親ディレクトリの絶対パスを取得します。
	 * 
	 * @param file ファイルパス
	 * @return 親ディレクトリの絶対パス
	 */
	public static String getAbsDir(String file) {
		return Path.of(file).getParent().toAbsolutePath().toString(); // 親ディレクトリのPathを取得    
    }

	/**
	 * ファイル選択ダイアログを表示し、選択されたファイルのパスリストを返します。
	 * EDTでダイアログを表示し、メインスレッドでは結果が返されるまで待機します。
	 * 
	 * @return 選択されたファイルのパスリスト。キャンセルされた場合はnull
	 */
    public static List<String> fDialog() {
    	var chooser = new Chooser();
		SwingUtilities.invokeLater(() -> {
			chooser.fileDialog();
		});    	
		
        // イベント処理が終わるまで待つ
        synchronized (chooser) {
            try {
            	chooser.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }     	
    	return chooser.getFlist();
    }
    
    /**
     * 絶対パスのリストからファイル名のみを抽出したリストを作成します。
     * 
     * @param absList 絶対パスのリスト
     * @return ファイル名のみのリスト
     */
    public static List<String> toFlist(List<String> absList) {
    	var list = new ArrayList<String>();
    	for(String fname : absList) {
    		list.add(Path.of(fname).getFileName().toString());
    	}
    	return list;
    }
    
    /**
     * ディレクトリ選択ダイアログを表示し、選択されたディレクトリのパスを返します。
     * EDTでダイアログを表示し、メインスレッドでは結果が返されるまで待機します。
     * 
     * @return 選択されたディレクトリのパス。キャンセルされた場合はnull
     */
    public static String dDialog() {
    	var chooser = new Chooser();
		SwingUtilities.invokeLater(() -> {
			chooser.directoryDialog();
		});    	
		
        // イベント処理が終わるまで待つ
        synchronized (chooser) {
            try {
            	chooser.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }     	
    	return chooser.getDir();
    }    
    
    /**
     * このクラスの動作を確認するためのメインメソッドです。
     * ファイル選択ダイアログを表示し、選択されたファイルの情報を出力します。
     * 
     * @param args コマンドライン引数（使用されません）
     */
    public static void main(String[] args) {
        // ファイルを選択するダイアログを表示
        List<String> selectedFiles = fDialog();
        if (selectedFiles != null) {
            System.out.println("選択されたファイル:");
            for (String file : selectedFiles) {
                System.out.print(file + " / ");
                System.out.println(getAbsDir(file));
            }
        } else {
            System.out.println("ファイルが選択されませんでした。");
        }
        
        var ls = toFlist(selectedFiles);
        for(String name : ls) {
        	System.out.println(name);
        }
    }
}
