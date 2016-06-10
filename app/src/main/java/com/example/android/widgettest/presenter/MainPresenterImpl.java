package com.example.android.widgettest.presenter;

import com.example.android.widgettest.view.MainView;

public class MainPresenterImpl implements MainPresenter {

    private MainView mView;

    @Override
    public void getDataFromUrl(String url) {

    }

    @Override
    public void onCreate(MainView mainView) {
        mView = mainView;
    }

    @Override
    public void saveUrlToSharedPrefs(String url) {

    }
}
