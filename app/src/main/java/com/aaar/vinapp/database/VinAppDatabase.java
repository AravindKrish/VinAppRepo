package com.aaar.vinapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Topic.class}, version = 1)
public abstract class VinAppDatabase extends RoomDatabase {
    public abstract TopicDao topicDao();

    private static volatile VinAppDatabase INSTANCE;

    static VinAppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VinAppDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VinAppDatabase.class, "topics_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
