package com.aaar.vinapp.MVVMRefactoring;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aaar.vinapp.EditTopicActivity;
import com.aaar.vinapp.R;
import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicDao;
import com.aaar.vinapp.database.VinAppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import static com.aaar.vinapp.EditTopicActivity.TOPIC_UPDATED;
import static com.aaar.vinapp.EditTopicActivity.UPDATED_POSITION;

public class ViewTopicNewActivity extends AppCompatActivity {

    private TopicDao topicDao;
    private Topic viewedTopic;
    private TextView textViewTopicName;
    private TextView textViewTopicLink;
    private TextView textViewTopicDetail;
    private Button buttonEdit;
    private Button buttonBacktoTopics;
    public int viewedtopicPosition;
    private long currentTopicIdentifier;
    private FloatingActionButton fabEdit;
    public static final int EDIT_TOPIC_REQUEST_CODE = 103;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_topic_activity);
        VinAppDatabase vinAppDatabase = VinAppDatabase.getDatabase(this);
        topicDao = vinAppDatabase.topicDao();
        setUIElements();
        setFieldValuesFromIntent();
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PMFromViewTopicActivity", Long.toString(viewedTopic.getIdentifier()));
                Intent intent = new Intent(ViewTopicNewActivity.this, EditTopicActivity.class);
                intent.putExtra("viewedTopic", viewedTopic);
                intent.putExtra("viewedposition", viewedtopicPosition);
                startActivityForResult(intent, EDIT_TOPIC_REQUEST_CODE);
            }
        });
/*        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PMFromViewTopicActivity", Long.toString(viewedTopic.getIdentifier()));
                Intent intent = new Intent(ViewTopicActivity.this, EditTopicActivity.class);
                intent.putExtra("viewedTopic", viewedTopic);
                intent.putExtra("viewedposition", viewedtopicPosition);
                startActivityForResult(intent, EDIT_TOPIC_REQUEST_CODE);
            }
        });*/






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_TOPIC_REQUEST_CODE) {
                viewedTopic = (Topic) data.getSerializableExtra(TOPIC_UPDATED);
                viewedtopicPosition = data.getIntExtra(UPDATED_POSITION, 1);
                setFieldValuesAfterUpdate();

            }
        }
    }
    public void setUIElements(){
        textViewTopicName = (TextView) findViewById(R.id.tv_view_topic_name);
        textViewTopicLink = (TextView) findViewById(R.id.tv_view_topic_link);
        textViewTopicDetail = (TextView) findViewById(R.id.tv_view_topic_details);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edittopic);
        //buttonEdit = (Button) findViewById(R.id.button_edit);
        //buttonBacktoTopics = (Button) findViewById(R.id.button_backtotopics);
    }

    public void setFieldValuesFromIntent(){
        Intent intent = this.getIntent();
        viewedTopic = (Topic)intent.getSerializableExtra("currentTopic");
        viewedtopicPosition = intent.getIntExtra("position", 1);
        textViewTopicName.setText(viewedTopic.getTopic());
        if (viewedTopic.getLink().isEmpty()) {
            Log.d("Topic Link", "empty");
        }
        else{
            textViewTopicLink.setTextColor(Color.parseColor("#0000EE"));
            final String url = viewedTopic.getLink();
            SpannableString spanString = new SpannableString(url);
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            textViewTopicLink.setText(spanString);
            textViewTopicLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(ViewTopicNewActivity.this, Uri.parse(url));
                    textViewTopicLink.setTextColor(Color.parseColor("#551A8B"));

                }
            });

        }
        if (viewedTopic.getDetails()!= null) {
            textViewTopicDetail.setText(viewedTopic.getDetails());
        }
        currentTopicIdentifier =  viewedTopic.getIdentifier();
        viewedtopicPosition = intent.getIntExtra("position", 1);

    }

    public void setFieldValuesAfterUpdate(){

        textViewTopicName.setText(viewedTopic.getTopic());
        if (viewedTopic.getLink()!= null) {
            textViewTopicLink.setText(viewedTopic.getLink());
        }
        if (viewedTopic.getDetails()!= null) {
            textViewTopicDetail.setText(viewedTopic.getDetails());
        }

        currentTopicIdentifier =  viewedTopic.getIdentifier();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(TOPIC_UPDATED,viewedTopic);
        intent.putExtra(UPDATED_POSITION, viewedtopicPosition);
        setResult(RESULT_OK,intent);
        finish();
    }
}

