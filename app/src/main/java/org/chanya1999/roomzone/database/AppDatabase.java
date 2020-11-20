package org.chanya1999.roomzone.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.chanya1999.roomzone.model.Subject;
import org.chanya1999.roomzone.util.AppExecutors;

import java.util.Calendar;

@Database(entities = {Subject.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    //กำหนดชื่อฐานข้อมูล
    private static final String DB_NAME = "subject.db";

    //สร้างตัวแปรชนิด AppDatabase
    private static AppDatabase sInstance;

    //สร้างตัวแปรชนิด SubjectDao
    public abstract SubjectDao subjectDao();

    //สร้างฐานข้อมูลโดยมีการกำหนดค่าต่าง ๆ พร้อมตรวจสอบว่ามีฐานข้อมูลอยู่แล้วหรือไม่
    public static synchronized AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DB_NAME
            ).addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }
            }).build();
        }
        return sInstance;
    }

}
