package com.aaar.vinapp.ViewModel;

import android.app.Application;

import com.aaar.vinapp.MVVMRefactoring.DB.TopicNew;
import com.aaar.vinapp.MVVMRefactoring.TopicRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TopicViewModel extends AndroidViewModel {

  private TopicRepository mTopicRepository;
  private LiveData<List<TopicNew>> mtopicList;

    public TopicViewModel(Application application) {
        super(application);
        mTopicRepository = new TopicRepository(application);
        mtopicList = mTopicRepository.getAllTopics();
    }

    public LiveData<List<TopicNew>> getAllTopics() { return mtopicList; }

    public void insert(TopicNew topicNew) { mTopicRepository.insert(topicNew); }
}
