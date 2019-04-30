package com.aaar.vinapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowAllTopics extends AppCompatActivity {

    private Topic topic;
    private TopicDao topicDao;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_list_activity);
        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        topicDao = vinAppDatabase.topicDao();
        mRecyclerView = findViewById(R.id.topic_recyclerview);
        getTopics();



    }

    private class GetTopicsAsyncTask extends AsyncTask<Void, Void, ArrayList<Topic> > {

        private TopicDao mAsyncTaskDao;

        private TopicViewAdapter mAdapter;
        private List <String> mtopicList;

        GetTopicsAsyncTask(TopicDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected ArrayList<Topic> doInBackground(final Void... params) {
            List<Topic> alltopics =   mAsyncTaskDao.getAllTopics();
            ListIterator<Topic> listIterator =  alltopics.listIterator();
            for (Topic topic : alltopics) {
                Log.d("Topic Name", topic.getTopic());
            }

            Log.d("vinapp-size", String.valueOf(alltopics.size()));
            return new ArrayList<>(alltopics);

        }

        @Override
        protected void onPostExecute(ArrayList<Topic> alltopics) {

            super.onPostExecute(alltopics);

            List<Topic> topicobjectList = alltopics;
            List <String> topiclist = null;
            ArrayList <String> topics = null;
            for (Topic topic : alltopics) {
                topics.add(topic.getTopic());
            }
            topiclist = topics;

            mAdapter = new TopicViewAdapter(getApplicationContext(), topiclist);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        }
    }



    public void getTopics() {
        new ShowAllTopics.GetTopicsAsyncTask(topicDao).execute();
    }


}
