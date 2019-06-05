package com.aaar.vinapp.MVVMRefactoring.DB;


import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TopicNewDao {

    @Insert
    void insert(TopicNew topicNew);

    //@Query("DELETE FROM topic_table")
    //void deleteAll();

    @Update
    void update(TopicNew topicNew);


    @Query("SELECT * from topic_table ORDER BY revision1 ASC")
    LiveData<List<TopicNew>> getAllTopics();

    @Query("SELECT * FROM topic_table WHERE topic = :topicname")
    TopicNew getcurrenttopic(String topicname);

    @Query("DELETE FROM topic_table WHERE topic = :topicname")
    void deletebyTopic(String topicname);

    @Query("DELETE FROM topic_table WHERE identifier = :identifier")
    void deletebyTopicIdentifier(long identifier);

    @Query("UPDATE topic_table SET topic = :topicname, link = :link, details = :details, revision1 = :revision1, count = :count  WHERE identifier = :identifier")
    int updateTopic(String topicname, String link, String details, Date revision1, int count, Long identifier);

}
