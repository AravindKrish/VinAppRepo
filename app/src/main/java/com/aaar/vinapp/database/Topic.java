package com.aaar.vinapp.database;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topic_table")
public class Topic {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "topic")
    private String mTopic;

    public Topic (String topic){
        this.mTopic = topic;
    }

    public String getTopic (){
        return  this.mTopic;
    }
}
