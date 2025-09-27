package jp.kwebs.tools;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeZoneConverter {

    /**
     * JSTの日付時刻をPDTの日付時刻に変換
     *
     * @param jstDateTime JST時刻のLocalDateTime
     * @return PDT時刻のLocalDateTime
     */
    public static LocalDateTime jst2Pdt(LocalDateTime jstDateTime) {
        // 日時のゾーンを作成する
        ZoneId jstZone = ZoneId.of("Asia/Tokyo");
        ZoneId pdtZone = ZoneId.of("America/Los_Angeles");

        // LocalDateTime（JST）を ZonedDateTime に変換
        ZonedDateTime jstZoned = jstDateTime.atZone(jstZone);

        // PDTのZonedDateTimeを作成して、さらにそれをLocalDateTimeにして返す
        return jstZoned.withZoneSameInstant(pdtZone).toLocalDateTime();
    }

    /**
     * PDTの日付時刻をJSTの日付時刻に変換
     * @param pdtDateTime PDT時刻のLocalDateTime
     * @return JST時刻のLocalDateTime
     */
    public static LocalDateTime pdt2Jst(LocalDateTime pdtDateTime) {
        // 日時のゾーンを作成する
        ZoneId pdtZone = ZoneId.of("America/Los_Angeles");
        ZoneId jstZone = ZoneId.of("Asia/Tokyo");

        // LocalDateTime（PDT）を ZonedDateTime に変換
        ZonedDateTime pdtZoned = pdtDateTime.atZone(pdtZone);

        // JSTのZonedDateTimeを作成して、さらにそれをLocalDateTimeにして返す
        return pdtZoned.withZoneSameInstant(jstZone).toLocalDateTime();
    }
}
