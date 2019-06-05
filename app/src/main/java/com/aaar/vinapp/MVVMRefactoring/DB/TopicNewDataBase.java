package com.aaar.vinapp.MVVMRefactoring.DB;

import android.content.Context;

import com.aaar.vinapp.database.DateTypeConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TopicNew.class}, version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter.class)

public abstract class TopicNewDataBase extends RoomDatabase {

    public abstract TopicNewDao topicNewDao();

    private static volatile TopicNewDataBase INSTANCE;

    public static TopicNewDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TopicNewDataBase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TopicNewDataBase.class, "topics_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
