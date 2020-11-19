package org.chanya1999.roomzone.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.chanya1999.roomzone.util.DateConverter;

import java.util.Date;

@Entity(tableName = "subjects")

public class Subject {

    public static final String[] shortDaysOfWeek = { "Sun.", "Mon.", "Tue.", "Wed.", "Thu.", "Fri.", "Sat."};
    public static final String[] longDaysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @PrimaryKey(autoGenerate = true)
    public final int id;

    @ColumnInfo(name = "subject_name")
    public final String subjectName;

    @ColumnInfo(name = "day")
    public final int day;

    @ColumnInfo(name = "start_time")
    @TypeConverters(DateConverter.class)
    public final Date startTime;

    @ColumnInfo(name = "end_time")
    @TypeConverters(DateConverter.class)
    public final Date endTime;

    @ColumnInfo(name = "room")
    public final String room;

    @ColumnInfo(name = "number_of_student")
    public final int numberOfStudent;

    @ColumnInfo(name = "credit")
    public final int credit;

    @ColumnInfo(name = "note")
    public final String note;

    @ColumnInfo(name = "last_update")
    @TypeConverters(DateConverter.class)
    public final Date lastUpdate;

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

