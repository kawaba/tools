package jp.kwebs.tools;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * 整数またはCardオブジェクトを使って複数のトランプのカードを表示する 表示位置をbias属性で指定できる
 * <p>
 * このクラスはSwingを使用してトランプカードの画像を表示するウィンドウを提供します。
 * カードは0.3秒間隔で順次表示され、マウスクリックで追加のカードを引くことができます。
 * 表示位置は"above", "middle", "below"などのバイアス名で指定可能です。
 *
 * @author WEBS/Takashi_Kawaba
 * @version 1.0
 * @since 2024
 */
public class CardView extends JFrame {
    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

    // 表示位置を決めるデータ
    /** 中央位置の縦方向バイアス値 */
    private static final int MIDDLE = 0;
    /** 上側位置の縦方向バイアス値 */
    private static final int ABOVE = -320;
    /** 下側位置の縦方向バイアス値 */
    private static final int BELOW = 320;

    /** 表示できる最大カード枚数 */
    private static final int MAX_SIZE = 5;

    /** ウィンドウの縦幅 */
    private static final int WINDOW_HEIGHT = 320;
    /** カードの横幅 */
    private static final int CARD_WIDTH = 180;
    /** カードの縦幅 */
    private static final int CARD_HEIGHT = 240;

    /** 表示するカードの枚数 */
    private int number_of_cards;
    /** ウィンドウの横幅 */
    private int windows_width;
    /** バイアスを表す名前（Windowタイトルとしても使用） */
    private String biasname;
    /** 縦方向のバイアス値 */
    private int bias = MIDDLE;

    /**
     * カードシュー（表示するカードを取り出した）
     * <ul>
     * <li>重複なく無限にカードを引ける</li>
     * <li>カードを追加する時に使うため保管する</li>
     * <li>ゲーム参加者は同じカードシューを持つようにすること</li>
     * <li>ウィンドをクリックした時、shoeからカードを取り出し表示する処理が必要な場合にsetterでshoeにインスタンスをセット</li>
     * <li>それ以外では、nullが入っている</li>
     * </ul>
     */
    private Shoe shoe;

    /**
     * カードシューを設定します。
     * setterでしかセットできない
     *
     * @param shoe 設定するカードシューオブジェクト
     */
    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }

    /** 表示するカードのリスト。最初は空 */
    private ArrayList<Integer> cardList = new ArrayList<>();

    // クリックイベントでセットされるデータ
    /** クリックされたウィンドウのタイトル（biasname） */
    private String cl_owner;
    /** クリックされたカードのインデックス番号 */
    private int cl_index;
    /** クリックされてカードのカード番号 */
    private int cl_cardNumber;
    /** 表示されたカードの枚数 */
    private int displayedCardCount = 0;

    /**
     * CardViewオブジェクトを作成し、指定された枚数のカードを表示するウィンドウを初期化します。
     * <p>
     * コンストラクタの処理内容：
     * <ul>
     * <li>number_of_cardsのサイズで表示ようWindowを作成する</li>
     * <li>マウスイベントリスナーを登録する</li>
     * <li>タイマーイベントを登録し、実行する -- 0.3秒に一度、カードを表示する</li>
     * </ul>
     *
     * @param biasname タイトル＋表示位置（"above", "banker", "middle", "player", "below"など）
     * @param number_of_cards 表示するカード枚数
     */
    public CardView(String biasname, int number_of_cards) {

        this.biasname = biasname;

        /*
         * ***********************************
         *
         * 上下の表示位置のバイアスを指定する。
         *
         ************************************/
        String bn = biasname.toLowerCase();
        bias = switch (bn) {
            case "above", "banker" 	-> ABOVE;
            case "middle", "player" -> MIDDLE;
            case "below" 			-> BELOW;
            default 				-> MIDDLE;
        };
        /*
         * ***********************************
         *
         * ウィンドウを表示用にセットする
         *
         *
         *************************************/
        this.number_of_cards = number_of_cards;

        windows_width = this.number_of_cards * (180 + 14) + 90; // ウィンドウの横幅

        setTitle(biasname);

        setSize(windows_width, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
        //setLayout(null);

        // OSに応じたLook and Feelを設定
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
                // その他のOSの場合はデフォルトのクロスプラットフォームLookAndFeelを使用
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
        SwingUtilities.updateComponentTreeUI(this);

        // 座標を設定してウィンドウを表示
        int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        int x = (screenWidth - windows_width) / 2;
        int y = (screenHeight - WINDOW_HEIGHT) / 2 + bias; // 中央からbiasピクセル下に表示
        setLocation(x, y);
        /*
         * ****************************************
         *
         * マウスイベントの登録
         *
         *****************************************/
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                // どのカードがクリックされたかインデックスを得る
                int x = evt.getX();
                int cardWidthWithMargin = CARD_WIDTH + 20; // カードの横幅 + マージン
                int clickedCardIndex = (x) / cardWidthWithMargin;

                // インデックスが正当なら処理を実行する
                if (clickedCardIndex >= 0 && clickedCardIndex < cardList.size()) {

                    // ウィンドウ名、インデックス、カードの値を表示
                    cl_owner = biasname;
                    cl_index = clickedCardIndex;
                    cl_cardNumber = cardList.get(clickedCardIndex);
                    System.out.println("Clicked " + cl_owner + "/" + cl_index + ": " + cl_cardNumber);

                    /*
                     * カードシューから一枚を引いて、表示リストに加える 全カードを再表示する
                     * フィールドにShoeオブジェクトが必要なので、setterでセットしておく
                     */
                    if(shoe!=null) {
                        display(shoe.deal());
                    }
                }
            }
        });
        /*
         * ****************************************
         *
         * タイマーイベントの登録・開始
         *
         * displayedCardCountは初期値は0
         * displayedCardCountがゼロの場合、updateDisplay()は、1枚のカードを表示する。
         * updateDisplay()のループはゼロオリジンなので。
         *
         * 登録してあるカード枚数よりも表示済カード枚数（displayedCardCount）が小さい場合は、
         * 　　　0.3秒に一回、
         * 　　　updateDisplay()を実行してdisplayedCardCount枚のカードを表示し
         * 		 displayedCardCountを１増やす
         *       ⇒繰り返し処理になる
         *
         * updateDisplay()は、表示をクリアしてdisplayedCardCount枚までのカードを表示する
         *
         *
         *****************************************/
        Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayedCardCount < cardList.size()) {
                    updateDisplay();
                    displayedCardCount++;
                } else {
                    ((Timer) e.getSource()).stop(); // 表示するカードがなくなったらタイマーを停止

                    /////////////////////////////////////////////////////
                    // 呼び出したスレッドを再開させるためにnotify
                    synchronized (CardView.this) {
                        CardView.this.notify();
                    }
                    /////////////////////////////////////////////////////
                }
            }
        });
        timer.start();
    }

    /**
     * カードリストをウィンドウに表示します。
     * <p>
     * displayメソッドを使って、カードリストをウィンドウに表示する
     *
     * @param list 表示するカードのリスト
     */
    public void show_cards(List<Integer> list) {
        cardList.addAll(list);
        //updateDisplay();	// タイマーで実行するので不要
        setVisible(true);
    }

    /**
     * 指定されたカードを表示リストに追加し、全てのカードを再表示します。
     * <p>
     * カードを表示リストに１枚追加し、表示リストにあるカードをすべて表示する
     *
     * @param cardNumber 追加するカード番号
     */
    public void display(int cardNumber) {

        /*
         * クリックイベントで、必要以上にカードが追加されるのを防ぐ
         */
        if (cardList.size() == number_of_cards) {
            return;
        }

        // 新規にカードを追加して、全カードリストを再表示する
        cardList.add(cardNumber);
        updateDisplay();	// タイマーイベントは終了しているので、これは必要。
    }

    /**
     * 勝利カード（カード番号60）を表示します。
     */
    public void displayWin() {
        display(60);
    }

    /**
     * 引き分けカード（カード番号61）を表示します。
     */
    public void displayTie() {
        display(61);
    }

    /**
     * ウィンドウを閉じます。
     */
    public void close() {
        dispose();
    }

    /**
     * 現在の表示リストにあるカードをすべて表示します。
     * <p>
     * 表示リストにあるカードをすべて表示する
     */
    private void updateDisplay() {
        getContentPane().removeAll(); // コンテンツをクリア

        for (int i = 0; i <= displayedCardCount; i++) {
            int cardNumber = cardList.get(i);

            ImageIcon cardImage = createImageIcon(cardNumber + ".png");
            Image scaledImage = cardImage.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
            cardImage = new ImageIcon(scaledImage);

            JLabel cardLabel = new JLabel(cardImage);
            getContentPane().add(cardLabel);
        }
        revalidate();
        repaint();
    }

    /**
     * JARファイル内のリソースから画像アイコンを作成します。
     * <p>
     * jarファイル内のリソースにアクセスするメソッド
     *
     * @param path 画像ファイルのパス
     * @return 作成されたImageIconオブジェクト、見つからない場合はnull
     */
    protected ImageIcon createImageIcon(String path) {
        // クラスローダーを使用してリソースを取得
        URL imgURL = CardView.class.getClassLoader().getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Could not find file: " + path);
            return null;
        }
    }

    /**
     * 指定されたカードリストを表示するCardViewウィンドウを作成・表示します。
     * <p>
     * 引数に指定したカードを表示する
     * 表示可能な最大枚数はMAX_SIZE。これを超えるカードは削除される。
     *
     * @param biasname 表示位置、タイトル（"above", "middle", "below"など）
     * @param list 表示するカードのリスト
     * @param shoe マウスクリックでカードを追加表示するためのカードシュー
     * @return 作成されたCardViewオブジェクト（更新の時に使う）
     */
    public static CardView show(String biasname, List<Integer> list, Shoe shoe) {

        // 表示枚数の制限
        if(list.size()>MAX_SIZE) {
            list.subList(MAX_SIZE, list.size()).clear();
        }

        // cardsインスタンスを生成する 表示幅はカード枚数＋１、Shoeはstatic
        var cards = new CardView(biasname, list.size()+1);
        cards.setShoe(shoe);

        // swingのスレッドでカードを表示する
        // 表示終了を待たずにmainスレッドに直ちにリターンする
        SwingUtilities.invokeLater(() -> {
            cards.show_cards(list);
        });

        //////////////////////////////////////////////////////
        // イベント処理が終わるまで待つ
        synchronized (cards) {
            try {
                cards.wait();	// cardsが処理の最後にnotify()するまで、このスレッドを停止する
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //////////////////////////////////////////////////////
        return cards;
    }

    /**
     * 指定されたカードリストを表示するCardViewウィンドウを作成・表示します。
     * <p>
     * 引数に指定したカードを表示する
     *
     * maxは、追加で配るカードを表示できるようにウィンドウの横幅を
     * あらかじめきめておくために使う。表示するカードの数よりも大きいことがある
     *
     * @param biasname 表示位置、タイトル
     * @param list 表示するカードのリスト
     * @return 作成されたCardViewオブジェクト（更新の時に使う）
     */
    public static CardView show(String biasname, List<Integer> list) {

        return show(biasname, list, null);

    }

    /**
     * デフォルトタイトル"Cards"でカードリストを表示するCardViewウィンドウを作成・表示します。
     * <p>
     * 簡易版 show()メソッド
     *
     * カードを表示するだけの機能
     *
     * @param list 表示するカードのリスト
     * @return 作成されたCardViewオブジェクト
     */
    public static CardView show(List<Integer> list) {
        return CardView.show("Cards", list);

    }

    /**
     * デフォルトタイトル"Cards"でカードリストを表示し、カードシューも設定するCardViewウィンドウを作成・表示します。
     *
     * @param list 表示するカードのリスト
     * @param shoe マウスクリックでカードを追加表示するためのカードシュー
     * @return 作成されたCardViewオブジェクト
     */
    public static CardView show(List<Integer> list, Shoe shoe) {
        return CardView.show("Cards", list, shoe);

    }

    /**
     * テスト用のメインメソッドです。
     * サンプルカード（22, 31, 5, 6, 8）を表示します。
     *
     * @param args コマンドライン引数（使用しない）
     */
    public static void main(String[] args) {
        var cards = List.of(22,31,5,6,8);
        CardView.show(cards);
    }

}