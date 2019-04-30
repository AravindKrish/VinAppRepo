package com.aaar.vinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;

import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    EditText editTextAddTopic;
    Button buttonSave;
    Button buttonSeeAllTopis;
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private Topic topic;
    private TopicDao topicDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        topicDao = vinAppDatabase.topicDao();


        editTextAddTopic = (EditText) findViewById(R.id.edittext_addtopic);
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSeeAllTopis = (Button) findViewById(R.id.button_seetopiclist);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic = new Topic(editTextAddTopic.getText().toString());
                insert(topic);


            }
        });

        buttonSeeAllTopis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ShowAllTopicsActivity.class);
                startActivity(intent);


            }
        });


    }

    private class insertAsyncTask extends AsyncTask<Topic, Void, Void> {

        private TopicDao mAsyncTaskDao;

        insertAsyncTask(TopicDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Topic... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //getTopics();
            Toast.makeText(MainActivity.this, "Topic Added", Toast.LENGTH_SHORT).show();
            finish();


        }
    }

    public void insert (Topic topic) {
        new insertAsyncTask(topicDao).execute(topic);
    }

    private class GetTopicsAsyncTask extends AsyncTask<Void, Void, Void> {

        private TopicDao mAsyncTaskDao;

        GetTopicsAsyncTask(TopicDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            List <Topic> alltopics =   mAsyncTaskDao.getAllTopics();
            ListIterator<Topic> listIterator =  alltopics.listIterator();
            for (Topic topic : alltopics) {
                Log.d("Topic Name", topic.getTopic());
            }

            Log.d("vinapp-size", String.valueOf(alltopics.size()));
            return null;

        }
    }

    public void getTopics() {
        new GetTopicsAsyncTask(topicDao).execute();
    }

}
