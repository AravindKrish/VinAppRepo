package com.aaar.vinapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ViewEditTopicActivity extends AppCompatActivity {

    private TopicDao topicDao;
    private TextInputEditText textInputEditTextTopicName;
    public EditText editTextLink;
    private EditText editTextTopicDetails;
    private Button buttonEditSave;
    public Topic currentTopic;
    private Topic topic;
    private long currentTopicIdentifier;
    public static final String TOPIC_UPDATED = "lasttopicupdated";
    public static final String UPDATED_POSITION = "updatedposition";
    public int topicPosition;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_topic_activity);
        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        topicDao = vinAppDatabase.topicDao();
        textInputEditTextTopicName = (TextInputEditText) findViewById(R.id.ti_edittext_edittopic);
        editTextLink = (EditText) findViewById(R.id.edittext_editlink);
        editTextTopicDetails = (EditText) findViewById(R.id.edittext_edittopicdetails);
        buttonEditSave = (Button) findViewById(R.id.button_saveedit);
        Intent intent = this.getIntent();
        //Bundle bundle = intent.getExtras();
        currentTopic = (Topic)intent.getSerializableExtra("viewedTopic");
        topicPosition = intent.getIntExtra("viewedposition", 1);

        textInputEditTextTopicName.setText(currentTopic.getTopic());
        final Date revision1date = currentTopic.getRevision1();
        final Date date = Calendar.getInstance().getTime();
        currentTopicIdentifier =  currentTopic.getIdentifier();
        Log.d("Current Topic identifier", ""+currentTopicIdentifier);
        Log.d("Revision Count", ""+currentTopic.getCount());
        if(currentTopic.getLink()!= null) {
            editTextLink.setText(currentTopic.getLink());
        }
          if (currentTopic.getDetails()!= null) {
            editTextTopicDetails.setText(currentTopic.getDetails());
        }

        buttonEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTopic.setmTopic(textInputEditTextTopicName.getText().toString());
                currentTopic.setmLink(editTextLink.getText().toString());
                currentTopic.setmDetails(editTextTopicDetails.getText().toString());
                currentTopic.setmAdded(currentTopic.getAdded());
                currentTopic.setmRevision1(revision1date);

                //topic = new Topic(textInputEditTextTopicName.getText().toString(), editTextLink.getText().toString(), editTextTopicDetails.getText().toString(), date, revision1date);
                update(currentTopic);
            }
        });
//        textInputEditTextTopicName.setFocusable(feditTextLink.getText().toString()alse);
//        textInputEditTextTopicName.setClickable(false);
//        editTextLink.setFocusable(false);
//        editTextLink.setClickable(false);
//        editTextTopicDetails.setFocusable(false);
//        editTextTopicDetails.setClickable(false);
//
//        buttonEditSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                textInputEditTextTopicName.setFocusable(true);
//                textInputEditTextTopicName.setClickable(true);
//                editTextLink.setFocusable(true);
//                editTextLink.setClickable(true);
//                editTextTopicDetails.setFocusable(true);
//                editTextTopicDetails.setClickable(true);
//                buttonEditSave.setText("Save");
//
//                buttonEditSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(ViewEditTopicActivity.this, "Wowwww OSM", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

    }

    public void update(Topic topic){
        Log.d("Anyways", "update:"+topic.getIdentifier());
        new UpdateAsyncTask(topicDao).execute(topic);
        Intent intent = new Intent();
        intent.putExtra(TOPIC_UPDATED,topic);
        intent.putExtra(UPDATED_POSITION, topicPosition);
        setResult(RESULT_OK,intent);
        finish();

    }



}
