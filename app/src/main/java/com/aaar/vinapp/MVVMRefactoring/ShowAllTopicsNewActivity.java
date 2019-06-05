package com.aaar.vinapp.MVVMRefactoring;


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

import com.aaar.vinapp.AddTopicActivity;
import com.aaar.vinapp.MVVMRefactoring.DB.TopicNew;
import com.aaar.vinapp.MVVMRefactoring.DB.TopicNewDao;
import com.aaar.vinapp.R;
import com.aaar.vinapp.ScreenSlidePagerActivity;
import com.aaar.vinapp.TopicViewAdapter;
import com.aaar.vinapp.UpdateRevision;
import com.aaar.vinapp.ViewModel.TopicViewModel;
import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowAllTopicsNewActivity extends AppCompatActivity implements TopicViewAdapter.DeleteTopicListener, TopicViewAdapter.StarClickListener {
    public static final int ADD_TOPIC_REQUEST_CODE = 101;
    public static final int VIEW_TOPIC_REQUEST_CODE = 102;
    private TopicNew topicNew;
    private TopicNewDao topicNewDao;
    private RecyclerView mNewRecyclerView;
    private FloatingActionButton fab_AddTopic;
    private Boolean isresumed = false;
    private TopicViewNewAdapter mNewAdapter;
    public List<TopicNew> topicNewList;
    public ArrayList<Topic> topicList;
    private int addedpositon;
    public int revisioncount;
    private int updatedposition;
    public static final String CHANNEL_ID = "channel";
    private Topic updateTopic;
    private Topic revisedTopic;
    public static boolean isactivityrunning = false;
    private TopicViewModel topicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topicnew_list_activity);
        //VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        //topicDao = vinAppDatabase.topicDao();
        mNewRecyclerView = findViewById(R.id.topiclist_recyclerview);
        fab_AddTopic = (FloatingActionButton) findViewById(R.id.fab_addtopic);
        fab_AddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowAllTopicsNewActivity.this, "Navigating to Add topics", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowAllTopicsNewActivity.this, AddTopicNewActivity.class);
                startActivityForResult(intent, ADD_TOPIC_REQUEST_CODE);
            }
        });
        // TBR getTopics();
        // TBR topicList = new ArrayList<>();
        mNewAdapter = new TopicViewNewAdapter(this);
        mNewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewRecyclerView.setAdapter(mNewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteNewCallBack(mNewAdapter));
        itemTouchHelper.attachToRecyclerView(mNewRecyclerView);
        addedpositon = mNewAdapter.getItemCount();
        createNotificationChannels();
        topicViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);
        topicViewModel.getAllTopics().observe(this, new Observer<List<TopicNew>>() {
            @Override
            public void onChanged(List<TopicNew> topicsNew) {
                mNewAdapter.setTopiclist(topicsNew);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_TOPIC_REQUEST_CODE) {
                TopicNew topicNewAdded = ((TopicNew) data.getSerializableExtra(AddTopicActivity.TOPIC_ADDED));
                //topicNewList.add(topicNewAdded);
                topicViewModel.insert(topicNewAdded);
                //this.mAdapter.notifyItemInserted(topicList.size() - 1);

            }
            if (requestCode == VIEW_TOPIC_REQUEST_CODE) {
            /*    updateTopic = (Topic) data.getSerializableExtra(EditTopicActivity.TOPIC_UPDATED);
                updatedposition = data.getIntExtra(UPDATED_POSITION, 1);
                this.mAdapter.notifyItemChanged(updatedposition, updateTopic);*/

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isactivityrunning = true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            showTutorials();
        }

    }

/*TBR    public ArrayList getTopics() {
        ArrayList<Topic> A = null;
        try {
            A = new GetTopicsAsyncTask(topicDao, this).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return A;
    }*/

    @Override
    public void deleteTopicFromDB(long itemremovedid) {
        new DeleteTopicAsyncTask(topicNewDao, this, itemremovedid).execute();
        fab_AddTopic.show();

    }

    @Override
    public void onStarClick(Topic topicRevised, int position) {

        UpdateRevision updateRevision = new UpdateRevision(this, topicRevised);
        revisedTopic = updateRevision.updateRevisionCount(topicRevised);
        if (revisedTopic != null) {
            Log.d("String", "onStarClick:" + revisedTopic.getRevision1());
            this.mNewAdapter.notifyItemChanged(position, revisedTopic);
            Toast.makeText(this, (4 - revisedTopic.getCount()) + " revisions to go", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Its not revision time yet", Toast.LENGTH_LONG).show();
        }
    }

    private class GetTopicsAsyncTask extends AsyncTask<Void, Void, ArrayList<Topic>> {
        private TopicDao mAsyncTaskDao;
        private Context context;
        private List<String> mtopicList;

        GetTopicsAsyncTask(TopicDao dao, Context context) {
            mAsyncTaskDao = dao;
            this.context = context;
        }

        @Override
        protected ArrayList<Topic> doInBackground(final Void... params) {
            List<Topic> alltopics = (List<Topic>) mAsyncTaskDao.getAllTopics();
            ListIterator<Topic> listIterator = alltopics.listIterator();
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
            mNewAdapter.notifyDataSetChanged();
        }
    }

    public class DeleteTopicAsyncTask extends AsyncTask<Void, Void, Void> {
        private TopicNewDao mAsyncTaskDao;
        private Context context;
        private long mtopicIdentifier;

        DeleteTopicAsyncTask(TopicNewDao dao, Context context, long topicIdentifier) {
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

    private void createNotificationChannels() {
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

    public void showTutorials() {
        Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
        startActivity(intent);
    }


}

