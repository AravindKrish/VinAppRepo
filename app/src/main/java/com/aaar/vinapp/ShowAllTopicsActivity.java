package com.aaar.vinapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.aaar.vinapp.EditTopicActivity.UPDATED_POSITION;

public class ShowAllTopicsActivity extends AppCompatActivity implements TopicViewAdapter.DeleteTopicListener, TopicViewAdapter.StarClickListener {
    public static final int ADD_TOPIC_REQUEST_CODE = 101;
    public static final int VIEW_TOPIC_REQUEST_CODE = 102;
    //public static Object isactivityrunning;


    //implements TopicViewAdapter.TopicClickListener

    private Topic topic;
    private TopicDao topicDao;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab_AddTopic;
    private Boolean isresumed = false;
    private TopicViewAdapter mAdapter;
    public ArrayList <Topic> topicList;
    private int addedpositon;
    public int revisioncount;
    private int updatedposition;
    public static final String CHANNEL_ID = "channel";
    private Topic updateTopic;
    private Topic revisedTopic;
    public static boolean isactivityrunning = false;
    //private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topicnew_list_activity);
        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        topicDao = vinAppDatabase.topicDao();
        mRecyclerView = findViewById(R.id.topiclist_recyclerview);
        ///checkBox = (CheckBox) findViewById(R.id.checkbox_singleitem);

        fab_AddTopic = (FloatingActionButton) findViewById(R.id.fab_addtopic);
        fab_AddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowAllTopicsActivity.this, "Navigating to Add topics", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowAllTopicsActivity.this, AddTopicActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, ADD_TOPIC_REQUEST_CODE);
            }
        });
        getTopics();
        topicList = new ArrayList<>();
        mAdapter = new TopicViewAdapter(this, topicList,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallBack(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        addedpositon = mAdapter.getItemCount();
        createNotificationChannels();
       /* mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab_AddTopic.isShown())
                    fab_AddTopic.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab_AddTopic.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //addedpositon = mAdapter.getItemCount();
        if (resultCode == RESULT_OK)
        {
            if (requestCode == ADD_TOPIC_REQUEST_CODE){
                 Log.d(AddTopicActivity.TOPIC_ADDED,((Topic)data.getSerializableExtra(AddTopicActivity.TOPIC_ADDED)).getTopic());
                 topicList.add((Topic)data.getSerializableExtra(AddTopicActivity.TOPIC_ADDED));
                this.mAdapter.notifyItemInserted(topicList.size()-1);

            }

            if (requestCode == VIEW_TOPIC_REQUEST_CODE){

                updateTopic = (Topic) data.getSerializableExtra(EditTopicActivity.TOPIC_UPDATED);
                updatedposition = data.getIntExtra(UPDATED_POSITION, 1);

                //this.mAdapter.notifyDataSetChanged();
                this.mAdapter.notifyItemChanged(updatedposition, updateTopic);

            }
            //this.mAdapter.notifyItemChanged(position,payload);

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        isactivityrunning = true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            showTutorials();
        }
        //topicList.clear();
        //topicList.addAll(getTopics());
        //topicList = getTopics();
        //this.mAdapter.notifyDataSetChanged();

    }

    public ArrayList  getTopics() {
        ArrayList <Topic> A = null;
        try {
            A = new GetTopicsAsyncTask(topicDao, this ).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return A;
    }

    @Override
    public void deleteTopicFromDB(long itemremovedid) {
        new DeleteTopicAsyncTask(topicDao, this, itemremovedid).execute();
        fab_AddTopic.show();

    }

    @Override
    public void onStarClick(Topic topicRevised, int position) {

       UpdateRevision updateRevision = new UpdateRevision(this, topicRevised);
        revisedTopic = updateRevision.updateRevisionCount(topicRevised);

           if (revisedTopic != null){
               Log.d("String", "onStarClick:"+revisedTopic.getRevision1());
               this.mAdapter.notifyItemChanged(position, revisedTopic);
               Toast.makeText(this, (4-revisedTopic.getCount())+" revisions to go", Toast.LENGTH_LONG).show();
               //new UpdateAsyncTask1(topicDao).execute(topic);

           }
           else{
               Toast.makeText(this, "Its not revision time yet", Toast.LENGTH_LONG).show();
           }

    }

    //This is the implementation of the On Topic Click Interface method inside the TopicviewAdapter
    /*@Override
    public void onTopicClick(int position) {
     Intent intent = new Intent(this, EditTopicActivity.class);
     startActivity(intent);
    }*/

    private class GetTopicsAsyncTask extends AsyncTask<Void, Void, ArrayList<Topic> > {

        private TopicDao mAsyncTaskDao;
        private Context context;


        private List <String> mtopicList;

        GetTopicsAsyncTask(TopicDao dao, Context context) {
            mAsyncTaskDao = dao;
            this.context = context;
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
            topicList.addAll(alltopics);
            mAdapter.notifyDataSetChanged();
        }
    }

    public class DeleteTopicAsyncTask extends AsyncTask <Void, Void, Void>{
        private TopicDao mAsyncTaskDao;
        private Context context;
        private long mtopicIdentifier;

        DeleteTopicAsyncTask(TopicDao dao, Context context,long topicIdentifier) {
            mAsyncTaskDao = dao;
            this.context = context;
            this.mtopicIdentifier = topicIdentifier;
        }

        @Override
        protected Void doInBackground(Void... voids) {
                mAsyncTaskDao.deletebyTopicIdentifier(mtopicIdentifier);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

        private void createNotificationChannels(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Integer.toString(R.string.channel_name);
            String description = Integer.toString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void showTutorials(){
        Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
        startActivity(intent);
    }
}
