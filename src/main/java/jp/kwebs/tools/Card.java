package jp.kwebs.tools;

/**
 * トランプカードを表現するクラス
 * 各カードはスート（絵柄）と数字を持つ
 *
 * @author T.Kawaba
 * @version 1.0
 */
public class Card {
    private String suit;
    private int number;

    /**
     * スートと数字を指定してカードを作成する
     *
     * @param suit スート（spade, club, diamond, heart）
     * @param number 数字（1-13）
     */
    public Card(String suit, int number) {
        this.suit = suit.toLowerCase();
        this.number = number;
    }

    /**
     * 連番を指定してカードを作成する
     * 1-13: スペード, 14-26: クラブ, 27-39: ダイヤ, 40-52: ハート, 53以上: ジョーカー
     *
     * @param seqNumber 連番（1-54）
     */
    public Card(int seqNumber) {
        int suitNumber = (seqNumber-1)/13; // 0, 1, 2, 3, 4
        this.suit=switch(suitNumber) {
            case	0	->	"spade";
            case	1	->	"club";
            case	2	->	"diamond";
            case	3	->	"heart";
            default		->	"joker";
        };
        this.number = (seqNumber % 13==0)? 13 : seqNumber % 13;
    }

    /**
     * このカードの連番を取得する
     * スートと数字から連番を計算して返す
     *
     * @return カードの連番
     */
    public int seqNumber() {
        int suitNumber = switch(suit.charAt(0)) {
            case	's'		->	0;
            case	'c'		->	1;
            case	'd'		->	2;
            case	'h'		->	3;
            default			->	4;
        };
        return suitNumber * 13 + number;
    }

    /**
     * カードのスート（絵柄）を取得する
     *
     * @return スート名（spade, club, diamond, heart, joker）
     */
    public String getSuit() {
        return suit;
    }

    /**
     * カードのスート（絵柄）を設定する
     *
     * @param suit 設定するスート名
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /**
     * カードの数字を取得する
     *
     * @return カードの数字（1-13）
     */
    public int getNumber() {
        return number;
    }

    /**
     * カードの数字を設定する
     *
     * @param number 設定する数字（1-13）
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * カードの文字列表現を返す
     *
     * @return カード情報を含む文字列
     */
    @Override
    public String toString() {
        return "Card [suit=" + suit + ", number=" + number + "]";
    }

    /**
     * 連番からカード番号を取得する静的メソッド
     *
     * @param seqNumber 連番
     * @return カード番号（1-13）
     */
    public static int toCardnumber(int seqNumber) {
        int cardNumber = (seqNumber % 13==0)? 13 : seqNumber % 13;
        return cardNumber;
    }

    /**
     * 連番からスート番号を取得する静的メソッド
     *
     * @param seqNumber 連番
     * @return スート番号（0:スペード, 1:クラブ, 2:ダイヤ, 3:ハート, 4:ジョーカー）
     */
    public static int toSuitnumber(int seqNumber) {
        int suitNumber = (seqNumber-1)/13; // 0, 1, 2, 3, 4
        return suitNumber;
    }

    /**
     * 連番からスート名を取得する静的メソッド
     *
     * @param seqNumber 連番
     * @return スート名（spade, club, diamond, heart, joker）
     */
    public static String toSuit(int seqNumber) {
        int suitNumber = (seqNumber-1)/13; // 0, 1, 2, 3, 4
        String suit=switch(suitNumber) {
            case	0	->	"spade";
            case	1	->	"club";
            case	2	->	"diamond";
            case	3	->	"heart";
            default		->	"joker";
        };
        return suit;
    }

    /**
     * メインメソッド - テスト用
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {

        System.out.println(new Card(1));
        System.out.println(new Card(13));
        System.out.println(new Card(26));
        System.out.println(new Card(39));
        System.out.println(new Card(52));
        System.out.println(new Card(2));
        System.out.println(new Card(14));
        System.out.println(new Card(53));

    }
}