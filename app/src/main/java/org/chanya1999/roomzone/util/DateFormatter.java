package org.chanya1999.roomzone.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//คลาสสำหรับกำหนดรูปแบบการแสดงผลวันที่
public class DateFormatter {

  private static final String TAG = DateFormatter.class.getName();
  private static final String STORED_DATE_FORMAT = "yyyy-MM-dd";

  private SimpleDateFormat mDateFormatter;

  public DateFormatter() {
    mDateFormatter = new SimpleDateFormat(STORED_DATE_FORMAT, Locale.US);
  }

  public String formatDate(Date date) {
    return mDateFormatter.format(date);
  }

  public Date parseDateString(String dateString) {
    Date date = null;
    try {
      date = mDateFormatter.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
      Log.e(TAG, "Error parsing date");
    }
    return date;
  }

  //จัดรูปแบบวันที่ สำหรับแสดงผลบนหน้าจอ
  public static String formatDateForUi(Date date) {
    assert date != null;

    SimpleDateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
    String month = monthFormatter.format(date);

    SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
    //String yearInBe = String.valueOf(Integer.valueOf(yearFormatter.format(date)));
    String yearInBe = String.valueOf(Integer.parseInt(yearFormatter.format(date)) + 543);

    SimpleDateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
    String day = dayFormatter.format(date);

    return String.format(
        Locale.getDefault(),
        "%s/%s/%s",
        day, month, yearInBe
    );
  }

  // จัดรูปแบบเวลา สำหรับแสดงผลบนหน้าจอ
  public static String formatTimeForUi(Date date) {
    assert date != null;

    SimpleDateFormat hourFormatter = new SimpleDateFormat("HH", Locale.US);
    String hour = hourFormatter.format(date);

    SimpleDateFormat minuteFormatter = new SimpleDateFormat("mm", Locale.US);
    String minute = minuteFormatter.format(date);

    return String.format(
        Locale.getDefault(),
        "%s:%s",
        hour, minute
    );
  }

  public static String formatDateForUiShortYear(Date date) {
    SimpleDateFormat monthFormatter = new SimpleDateFormat("MM", Locale.US);
    String month = monthFormatter.format(date);

    SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.US);
    //String yearInBe = String.valueOf(Integer.valueOf(yearFormatter.format(date)));
    String yearInBe = String.valueOf(Integer.parseInt(yearFormatter.format(date)) + 543).substring(2);

    SimpleDateFormat dayFormatter = new SimpleDateFormat("dd", Locale.US);
    String day = dayFormatter.format(date);

    return String.format(
        Locale.getDefault(),
        "%s/%s/%s",
        day, month, yearInBe
    );
  }
}
