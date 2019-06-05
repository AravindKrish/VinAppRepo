package com.aaar.vinapp.database;

import java.io.Serializable;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topic_table")
public class Topic implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "identifier")
    private long mIdentifier;

    @NonNull
    @ColumnInfo(name = "topic")
    private String mTopic;

    @ColumnInfo(name = "link")
    private String mLink;

    @ColumnInfo(name = "details")
    private String mDetails;

    @ColumnInfo(name = "added")
    private Date mAdded;

    @ColumnInfo(name = "revision1")
    private Date mRevision1;

    @ColumnInfo (name = "count")
    private int mCount;




    public Topic (String topic, String link, String details, Date added, Date revision1){
        this.mTopic = topic;
        this.mLink = link;
        this.mDetails = details;
        this.mAdded = added;
        this.mRevision1 = revision1;

        //this.mIdentifier = identifier;

    }

    public long getIdentifier(){return mIdentifier;}

    public String getTopic (){
        return  mTopic;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public void setmDetails(String mDetails) {
        this.mDetails = mDetails;
    }

    public void setmAdded(Date mAdded) {
        this.mAdded = mAdded;
    }

    public void setmTopic(@NonNull String mTopic) {
        this.mTopic = mTopic;
    }

    public void setmRevision1(@NonNull Date mRevision1) {
        this.mRevision1 = mRevision1;
    }

    public void setIdentifier(@NonNull long mIdentifier) {
        this.mIdentifier = mIdentifier;
    }

    public String getLink(){
        return mLink;
    }

    public String getDetails(){
        return mDetails;
    }

    public Date getAdded() { return mAdded;}

    public Date getRevision1(){return mRevision1;}

    public int getCount(){return mCount;}

    public void setCount(@NonNull int mCount) {
        this.mCount = mCount;
    }


}
