package com.aaar.vinapp.database;

import android.arch.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TopicDao {

    @Insert
    void insert(Topic topic);

    @Query("DELETE FROM topic_table")
    void deleteAll();

    @Query("SELECT * from topic_table ORDER BY topic ASC")
    LiveData <List<Topic>> getAllTopics();


}
