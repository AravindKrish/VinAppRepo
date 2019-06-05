package com.aaar.vinapp;

import android.app.Application;

import com.aaar.vinapp.database.Topic;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TopicViewModel extends AndroidViewModel {

  private TopicRepository mTopicRepository;
  private LiveData<List<Topic>> mtopicList;

    public TopicViewModel(Application application) {
        super(application);
        mTopicRepository = new TopicRepository(application);
        mtopicList = mTopicRepository.getAllTopics();
    }

    public LiveData<List<Topic>> getAllTopics() { return mtopicList; }

    public void insert(Topic topic) { mTopicRepository.insert(topic); }
}
