package com.aaar.vinapp;

import android.app.Application;
import android.os.AsyncTask;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TopicRepository {

    private TopicDao mTopicDao;
    public LiveData<List<Topic>> mtopicList;


    public TopicRepository(Application application){

        VinAppDatabase db =VinAppDatabase.getDatabase(application);
        mTopicDao = db.topicDao();
        mtopicList = (LiveData<List<Topic>>) mTopicDao.getAllTopics();
    }

    public LiveData<List<Topic>> getAllTopics(){
        return mtopicList;
    }

    public void insert (Topic topic){
        new insertAsyncTask(mTopicDao).execute(topic);
    }

    private static class insertAsyncTask extends AsyncTask <Topic, Void, Void>{

        private TopicDao mInserAsyncTaskDao;

        insertAsyncTask (TopicDao dao){mInserAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Topic... topics) {
            mInserAsyncTaskDao.insert(topics[0]);
            return null;
        }
    }
}
