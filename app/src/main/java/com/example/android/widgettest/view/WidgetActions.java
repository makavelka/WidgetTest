package com.example.android.widgettest.view;

import android.content.Context;

public interface WidgetActions {
    void onNextPressed();
    void onPrevPressed();
    void updateData(Context context);
}
