package com.aaar.vinapp.MVVMRefactoring;

import android.app.Application;
import android.os.AsyncTask;

import com.aaar.vinapp.MVVMRefactoring.DB.TopicNew;
import com.aaar.vinapp.MVVMRefactoring.DB.TopicNewDao;
import com.aaar.vinapp.MVVMRefactoring.DB.TopicNewDataBase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TopicRepository {

    private TopicNewDao mTopicNewDao;
    public LiveData<List<TopicNew>> mtopicList;


    public TopicRepository(Application application){

        TopicNewDataBase db = TopicNewDataBase.getDatabase(application);
        mTopicNewDao = db.topicNewDao();
        mtopicList = (LiveData<List<TopicNew>>) mTopicNewDao.getAllTopics();
    }

    public LiveData<List<TopicNew>> getAllTopics(){
        return mtopicList;
    }

    public void insert (TopicNew topicNew){
        new insertAsyncTask(mTopicNewDao).execute(topicNew);
    }

    private static class insertAsyncTask extends AsyncTask<TopicNew, Void, Void> {

        private TopicNewDao mInserAsyncTaskDao;

        insertAsyncTask (TopicNewDao dao){mInserAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(TopicNew... topics) {
            mInserAsyncTaskDao.insert(topics[0]);
            return null;
        }
    }
}
