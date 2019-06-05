package com.aaar.vinapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class EditTopicActivity extends AppCompatActivity {

    private TopicDao topicDao;
    private TextInputEditText textInputEditTextTopicName;
    public EditText editTextLink;
    private EditText editTextTopicDetails;
    private Button buttonEditSave;
    public Topic currentTopic;
    private Topic topic;
    private long currentTopicIdentifier;
    private FloatingActionButton fabsave;
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
        Intent intent = this.getIntent();
        currentTopic = (Topic)intent.getSerializableExtra("viewedTopic");
        topicPosition = intent.getIntExtra("viewedposition", 1);
        fabsave = (FloatingActionButton) findViewById(R.id.fab_updatesave);
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
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTopic.setmTopic(textInputEditTextTopicName.getText().toString());
                currentTopic.setmLink(editTextLink.getText().toString());
                currentTopic.setmDetails(editTextTopicDetails.getText().toString());
                currentTopic.setmAdded(currentTopic.getAdded());
                currentTopic.setmRevision1(revision1date);
                if (currentTopic.getTopic().isEmpty()) {
                    Toast.makeText(EditTopicActivity.this, "Please Enter a topic Name", Toast.LENGTH_LONG).show();
                }
                else {
                    if (currentTopic.getLink().isEmpty()) {
                        update(currentTopic);
                                      }
                                      else{
                        if(Patterns.WEB_URL.matcher(currentTopic.getLink()).matches() && URLUtil.isValidUrl(currentTopic.getLink())){
                            update(currentTopic);
                        }
                        else{
                            Toast.makeText(EditTopicActivity.this, "Please Enter a Valid URL", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
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
