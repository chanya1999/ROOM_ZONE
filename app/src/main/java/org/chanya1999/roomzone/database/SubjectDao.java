package org.chanya1999.roomzone.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.chanya1999.roomzone.model.Subject;


@Dao
public interface SubjectDao {

    @Query("SELECT * FROM subjects")
    Subject[] getAllSubjects();

    @Query("SELECT * FROM subjects WHERE id = :id")
    Subject getSubjectById(int id);

    @Insert
    void addSubject(Subject... subjects);

    @Update
    void updateSubject(Subject subject);

    @Delete
    void deleteSubject(Subject subject);
}
