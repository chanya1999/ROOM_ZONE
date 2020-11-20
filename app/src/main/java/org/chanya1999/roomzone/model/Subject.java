package org.chanya1999.roomzone.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.chanya1999.roomzone.util.DateConverter;

import java.util.Date;

@Entity(tableName = "subjects")

//คลาสต้นแบบในการออกแบบฐานข้อมูล
public class Subject {
    //อาเรย์เก็บชื่อวันในสัปดาห์
    public static final String[] longDaysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    //กำหนดคอลัมภ์ชื่อ primaryKey ซึ่งจะให้ระบบสร้างอัตโนมัติ
    @PrimaryKey(autoGenerate = true)
    public final int id;

    //กำหนดคอลัมภ์ชื่อ subject_name เก็บชื่อวิชา
    @ColumnInfo(name = "subject_name")
    public final String subjectName;

    //กำหนดคอลัมภ์ชื่อ day เก็บวันในสัปดาห์
    @ColumnInfo(name = "day")
    public final int day;

    //กำหนดคอลัมภ์ชื่อ start_time เก็บเวลาเริ่มต้น
    @ColumnInfo(name = "start_time")
    @TypeConverters(DateConverter.class)
    public final Date startTime;

    //กำหนดคอลัมภ์ชื่อ end_time เก็บเวลาเลิกเรียน
    @ColumnInfo(name = "end_time")
    @TypeConverters(DateConverter.class)
    public final Date endTime;

    //กำหนดคอลัมภ์ชื่อ room เก็บสถานที่เรียน
    @ColumnInfo(name = "room")
    public final String room;

    //กำหนดคอลัมภ์ชื่อ number_of_student เก็บจำนวนผู้เรียน
    @ColumnInfo(name = "number_of_student")
    public final int numberOfStudent;

    //กำหนดคอลัมภ์ชื่อ credit เก็บหย่วยกิตที่เรียน
    @ColumnInfo(name = "credit")
    public final int credit;

    //กำหนดคอลัมภ์ชื่อ note เก็บข้อความบันทึก
    @ColumnInfo(name = "note")
    public final String note;

    //กำหนดคอลัมภ์ชื่อ last_update เก็บวัน/เดือน/ปี และเวลา การแก้ไขข้อความบันทึกครั้งล่าสุด
    @ColumnInfo(name = "last_update")
    @TypeConverters(DateConverter.class)
    public final Date lastUpdate;

    //เมธอดเริ่มต้นสำหรับสร้างวัตถุ
    public Subject(int id, String subjectName, int day, Date startTime, Date endTime, String room, int numberOfStudent, int credit, String note, Date lastUpdate) {
        this.id = id;
        this.subjectName = subjectName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.numberOfStudent = numberOfStudent;
        this.credit = credit;
        this.note = note;
        this.lastUpdate = lastUpdate;
    }

}

