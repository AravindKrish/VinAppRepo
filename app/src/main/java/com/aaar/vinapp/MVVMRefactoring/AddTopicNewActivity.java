package com.aaar.vinapp.MVVMRefactoring;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aaar.vinapp.AlarmReceiver;
import com.aaar.vinapp.R;
import com.aaar.vinapp.TopicViewAdapter;
import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import androidx.appcompat.app.AppCompatActivity;

public class AddTopicNewActivity extends AppCompatActivity {

    public static final String TOPIC_ADDED = "lasttopicAdded";
    Button buttonSave;
    Button buttonSeeAllTopis;
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private Topic topic;
    private TopicDao topicDao;
    TextInputEditText tieditTextAddTopic;
    EditText editTextLink;
    EditText editTextTopicDetails;
    TopicViewAdapter tvAdapter;
    private FloatingActionButton fab_save;

    public int revisioncount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_topic_activity);
        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        topicDao = vinAppDatabase.topicDao();
        tieditTextAddTopic = (TextInputEditText) findViewById(R.id.ti_edittext_addtopic);
        editTextLink =(EditText) findViewById(R.id.edittext_link);
        editTextTopicDetails = (EditText) findViewById(R.id.edittext_topicdetails);
        fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = Calendar.getInstance().getTime();
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.setTime(date);
                c1.add(Calendar.MINUTE, 2);
                c2.setTime(date);
                c2.add(Calendar.MINUTE, 2);
                Date revision1date = c2.getTime();
                Log.d("Printc1triggertime", c1.getTime().toString());

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 108, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.setRepeating(AlarmManager.RTC, c1.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);


                topic = new Topic(tieditTextAddTopic.getText().toString(), editTextLink.getText().toString(), editTextTopicDetails.getText().toString(), date, revision1date);
                topic.setCount(revisioncount);
                if (topic.getTopic().isEmpty()) {
                    Toast.makeText(AddTopicNewActivity.this, "Please Enter a topic Name", Toast.LENGTH_LONG).show();
                } else {
                    if (topic.getLink().isEmpty()) {
                        //insert(topic);
                        sendIntent(topic);
                    }
                    else{

                        if(Patterns.WEB_URL.matcher(topic.getLink()).matches() && URLUtil.isValidUrl(topic.getLink())){
                            //insert(topic);
                            sendIntent(topic);
                        }
                        else{
                            Toast.makeText(AddTopicNewActivity.this, "Please Enter a Valid URL", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }

    private class insertAsyncTask extends AsyncTask<Topic, Void, Topic> {

        private TopicDao mAsyncTaskDao;
        insertAsyncTask(TopicDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Topic doInBackground(final Topic... params) {
            mAsyncTaskDao.insert(params[0]);
            params[0]= mAsyncTaskDao.getcurrenttopic(params[0].getTopic());
            return params[0];
        }

        @Override
        protected void onPostExecute(Topic topic) {
            super.onPostExecute(topic);
            Intent intent = new Intent();
            intent.putExtra(TOPIC_ADDED,topic);
            setResult(RESULT_OK,intent);
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
            List <Topic> alltopics = (List<Topic>) mAsyncTaskDao.getAllTopics();
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

    public void printLastTopic(Topic topic){
        this.topic = topic;


    }

    public void sendIntent(Topic topic){
        Intent intent = new Intent();
        intent.putExtra(TOPIC_ADDED,topic);
        setResult(RESULT_OK,intent);
        finish();
    }



}
