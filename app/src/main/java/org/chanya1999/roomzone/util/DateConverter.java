package org.chanya1999.roomzone.util;

import androidx.room.TypeConverter;

import java.util.Date;

//คลาสแปลงชนิดข้อมูลระหว่าง date กับ Long
public class DateConverter {

  //แปลง long เป็น date
  @TypeConverter
  public static Date toDate(Long timestamp) {
    return timestamp == null ? null : new Date(timestamp);
  }

  //แปลง date เป็น long
  @TypeConverter
  public static Long toTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}