package com.aaar.vinapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {

    private TopicsRepository mRepository;
    private LiveData<List<Topic>> mAllTopics;



    public TopicViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TopicsRepository(application);
        mAllTopics = mRepository.getmAllTopics();

    }

    LiveData<List<Topic>> getAllWords() { return mAllTopics; }

    public void insert(Topic topic) { mRepository.insert(topic); }


}
