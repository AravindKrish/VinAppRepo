package com.aaar.vinapp.database;

import java.util.List;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TopicDao {

    @Insert
    void insert(Topic topic);

    //@Query("DELETE FROM topic_table")
    //void deleteAll();

    @Query("SELECT * from topic_table ORDER BY topic ASC")
    List<Topic> getAllTopics();




}
