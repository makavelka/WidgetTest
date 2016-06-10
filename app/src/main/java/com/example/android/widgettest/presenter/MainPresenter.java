package com.example.android.widgettest.presenter;

import com.example.android.widgettest.view.MainView;

public interface MainPresenter {
    void getDataFromUrl(String url);
    void onCreate(MainView mainView);
    void saveUrlToSharedPrefs(String url);
}
