package com.example.pefranksacco.ui.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class reportsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public reportsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is reports fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}