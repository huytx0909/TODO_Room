package com.example.huy.myfirstapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Query("SELECT * FROM Task")
    List<Task> getAll();

    @Query("SELECT * FROM Task WHERE time LIKE (:time)")
    List<Task> loadAllByTime(String time);

    @Delete
    void delete(Task task);

    @Query("UPDATE Task SET status = (:isChecked) WHERE description = (:description)")
    void setStatus(String isChecked, String description);

    @Query("UPDATE Task SET time = (:appointedTime), description = (:newDescription) WHERE description = (:oldDescription)")
    void update(String oldDescription, String newDescription, String appointedTime);

    @Query("SELECT * FROM Task WHERE description = (:description)")
    Task get1Task(String description);

}
