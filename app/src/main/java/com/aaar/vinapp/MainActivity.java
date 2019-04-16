package com.aaar.vinapp;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aaar.vinapp.database.Topic;
import com.aaar.vinapp.database.TopicViewModel;
import com.aaar.vinapp.database.TopicsRepository;

public class MainActivity extends AppCompatActivity {

    private TopicViewModel topicViewModel;
    EditText editTextAddTopic;
    Button buttonSave;
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private TopicsRepository mRepository;
    private Topic topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextAddTopic = (EditText) findViewById(R.id.editTextAddTopic);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        topic = new Topic(editTextAddTopic.getText().toString());
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRepository.insert(topic);
//                Intent replyIntent = new Intent();
//                if(TextUtils.isEmpty(editTextAddTopic.getText())){
//                    setResult(RESULT_CANCELED, replyIntent);
//                }
//                else{
//                    String topic = editTextAddTopic.getText().toString();
//                    replyIntent.putExtra(EXTRA_REPLY, topic);
//                }
//                finish();
            }
        });
    }
}
