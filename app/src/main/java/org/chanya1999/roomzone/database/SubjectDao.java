package org.chanya1999.roomzone.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.chanya1999.roomzone.model.Subject;

//interface สำหรับการกำหนดเมธอดในการใช้คำสั่ง sql กับฐานข้อมูล
@Dao
public interface SubjectDao {

    //เมธอดส่งคืนข้อมูลทั้งหมดที่เก็บในฐานข้อมูล
    @Query("SELECT * FROM subjects")
    Subject[] getAllSubjects();

    //เมธอดส่งคืนข้อมูลที่เก็บในฐานข้อมูลที่มี id ตรงกับพารามิเตอร์
    @Query("SELECT * FROM subjects WHERE id = :id")
    Subject getSubjectById(int id);

    //เมธอดเพิ่มข้อมูลลงฐานข้อมูล(สามารถเพิ่มได้มากกว่า 1 ในคราวเดียวกัน)
    @Insert
    void addSubject(Subject... subjects);

    //เมธอดปรับปรุงข้อมูลที่มีอยู่ในฐานข้อมูล
    @Update
    void updateSubject(Subject subject);

    //เมธอดลบข้อมูลที่มีอยู่ในฐานข้อมูล
    @Delete
    void deleteSubject(Subject subject);
}
