package com.example.apphydrojetting.activity.ui.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public JobsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Drain and Pipe Services");
    }

    public LiveData<String> getText() {
        return mText;
    }
}