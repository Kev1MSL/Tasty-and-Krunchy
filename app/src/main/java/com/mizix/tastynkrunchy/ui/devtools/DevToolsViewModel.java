package com.mizix.tastynkrunchy.ui.devtools;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DevToolsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DevToolsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Dev tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}