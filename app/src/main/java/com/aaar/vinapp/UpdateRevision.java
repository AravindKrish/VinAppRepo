package com.aaar.vinapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateRevision  {

    Topic revisedTopic;
    private Context context;
    public UpdateRevision(Context context,Topic topic){
        this.revisedTopic = topic;
        this.context = context;
    }

    public Topic updateRevisionCount(Topic topic){

        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(context);
        TopicDao topicDao = vinAppDatabase.topicDao();

        Date currentsystemTime = Calendar.getInstance().getTime();
        Date revisionTime = topic.getRevision1();
        Date createdDate = topic.getAdded();
        int revisioncount = topic.getCount();
        Calendar c1 = Calendar.getInstance();

        if (currentsystemTime.compareTo(revisionTime)>=0) {
            c1.setTime(createdDate);
            switch (revisioncount)
            {
                case 0:
                    c1.add(Calendar.DAY_OF_MONTH, 2);
                    topic.setCount(1);
                    topic.setmRevision1(c1.getTime());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    Log.d("UpdatedRevisiontime",  dateFormat.format(topic.getRevision1()));
                    Log.d("TopicsIdimmediatelyafteradding", Long.toString(topic.getIdentifier()));

                    break;

                case 1:
                    c1.add(Calendar.DAY_OF_YEAR, 14);
                    topic.setCount(2);
                    topic.setmRevision1(c1.getTime());
                    break;

                case 2:
                    c1.add(Calendar.MONTH, 2);
                    topic.setCount(3);
                    topic.setmRevision1(c1.getTime());
                    break;
            }
            Log.d("Revision count Passed to Async Task", Integer.toString(topic.getCount()));
            new UpdateAsyncTask(topicDao).execute(topic);
            return topic;

        }

        else{

            return null;
        }
    }


}
