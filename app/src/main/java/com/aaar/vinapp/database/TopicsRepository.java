package com.aaar.vinapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TopicsRepository {

    private TopicDao mtopicDao;
    private LiveData<List<Topic>> mAllTopics;

    TopicsRepository(Application application) {
        VinAppDatabase db = VinAppDatabase.getDatabase(application);
        mtopicDao = db.topicDao();
        mAllTopics = mtopicDao.getAllTopics();
    }

    LiveData<List<Topic>> getmAllTopics() {
        return mAllTopics;
    }

    public void insert (Topic topic) {
        new insertAsyncTask(mtopicDao).execute(topic);
    }

    private static class insertAsyncTask extends AsyncTask<Topic, Void, Void> {

        private TopicDao mAsyncTaskDao;

        insertAsyncTask(TopicDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Topic... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
