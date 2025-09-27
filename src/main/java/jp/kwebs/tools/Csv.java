package jp.kwebs.tools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * CSV形式の文字列を解析し、各項目にアクセスするためのクラス。
 * Iteratorインターフェースを実装しており、順次処理も可能。
 *
 * <p>CSV文字列はコンマまたは改行で区切られた値として解析される。
 * 空白文字は自動的にトリムされる。</p>
 *
 * @author kwebs
 * @version 1.0
 */
public class Csv implements Iterator<String>{

    /** CSVの各項目を格納するリスト */
    private List<String> items;

    /**
     * pos は現在のindex
     */
    private int pos = 0;

    /**
     * CSV文字列からCsvオブジェクトを構築する。
     *
     * @param csvString 解析するCSV文字列。コンマまたは改行で区切られた値を含む
     * @throws NullPointerException csvStringがnullの場合
     */
    public Csv(String csvString) {
        /*
         * "(\\s*,\\s*)|\n" は、
         * 0文字以上の空白文字で囲まれたコンマ、または改行文字の位置で分割する
         * という区切り文字指定
         */
        var array = csvString.split("(\\s*,\\s*)|\n");
        items = Arrays.asList(array);
    }

    /**
     * 指定されたインデックスの項目を文字列として取得する。
     *
     * @param index 取得する項目のインデックス（0から開始）
     * @return 指定されたインデックスの項目の文字列表現
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     */
    public String get(int index) {
        return items.get(index);
    }

    /**
     * 指定されたインデックスの項目を整数として取得する。
     *
     * @param index 取得する項目のインデックス（0から開始）
     * @return 指定されたインデックスの項目を整数に変換した値
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     * @throws NumberFormatException 項目が整数に変換できない場合
     */
    public int getInt(int index) {
        return Integer.parseInt(get(index));
    }

    /**
     * 指定されたインデックスの項目を倍精度浮動小数点数として取得する。
     *
     * @param index 取得する項目のインデックス（0から開始）
     * @return 指定されたインデックスの項目を倍精度浮動小数点数に変換した値
     * @throws IndexOutOfBoundsException インデックスが範囲外の場合
     * @throws NumberFormatException 項目が倍精度浮動小数点数に変換できない場合
     */
    public double getDouble(int index) {
        return Double.parseDouble(get(index));
    }

    /**
     * CSV項目の総数を取得する。
     *
     * @return CSV項目の総数
     */
    public int size() {
        return items.size();
    }

    /**
     * すべてのCSV項目を含むリストを取得する。
     *
     * @return すべての項目を含むList&lt;String&gt;
     */
    public List<String> getItems() {
        return items;
    }

    /**
     * このCsvオブジェクトの文字列表現を返す。
     *
     * @return すべての項目を含むリストの文字列表現
     */
    @Override
    public String toString() {
        return items.toString();
    }

    /**
     * まだ処理されていない項目があるかどうかを判定する。
     *
     * @return まだ処理されていない項目がある場合はtrue、そうでなければfalse
     */
    @Override
    public boolean hasNext() {
        return pos < size() ? true : false;
    }

    /**
     * 次の項目を取得し、内部位置を進める。
     *
     * @return 次の項目の文字列表現。項目がない場合はnull
     */
    @Override
    public String next() {
        return pos < size() ? get(pos++) : null;
    }

    /**
     * 次の項目を整数として取得し、内部位置を進める。
     *
     * @return 次の項目を整数に変換した値
     * @throws NumberFormatException 項目が整数に変換できない場合
     * @throws NullPointerException 次の項目がnullの場合
     */
    public int nextInt() {
        return Integer.parseInt(next());
    }

    /**
     * 次の項目を倍精度浮動小数点数として取得し、内部位置を進める。
     *
     * @return 次の項目を倍精度浮動小数点数に変換した値
     * @throws NumberFormatException 項目が倍精度浮動小数点数に変換できない場合
     * @throws NullPointerException 次の項目がnullの場合
     */
    public double nextDouble() {
        return Double.parseDouble(next());
    }

    /**
     * イテレータの位置を先頭に戻す。
     * posを0に初期化する
     */
    public void reset() {
        pos=0;
    }
}