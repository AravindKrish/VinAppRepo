package com.aaar.vinapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;

public class UpdateAsyncTask extends AsyncTask<Topic, Void, Topic> {

    //TopicDao topicDao;
    private long currentTopicIdentifier;

    private TopicDao mupdateAsyncTaskDao;

    public UpdateAsyncTask(TopicDao topicDao){
        mupdateAsyncTaskDao = topicDao;

    }

    @Override
    protected Topic doInBackground(Topic... topic) {
        //mupdateAsyncTaskDao.update(topic[0]);
        //mupdateAsyncTaskDao.update(topic[0]);
        mupdateAsyncTaskDao.updateTopic(topic[0].getTopic(),topic[0].getLink(), topic[0].getDetails(), topic[0].getRevision1(), topic[0].getCount(), topic[0].getIdentifier());
        Log.d("doInBackground:getIdentifier", Long.toString(topic[0].getIdentifier()));
        Log.d("doInBackgroundAravind:", topic[0].getTopic());
        return topic[0];

    }

    @Override
    protected void onPostExecute(Topic topic) {
        super.onPostExecute(topic);
        Log.d("OnPostExecute", Integer.toString(topic.getCount()));

    }


}