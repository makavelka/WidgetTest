package com.example.android.widgettest.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.example.android.widgettest.Const;
import com.example.android.widgettest.R;
import com.example.android.widgettest.view.RssWidget;

public class UpdateWidgetService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager mWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.rss_widget);
            Intent active = new Intent(this, RssWidget.class);
            active.setAction(Const.ACTION_WIDGET_CONF);
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.conf_imageButton_rssWidget, actionPendingIntent);

            active = new Intent(this, RssWidget.class);
            active.setAction(Const.ACTION_WIDGET_PREV);
            actionPendingIntent = PendingIntent.getBroadcast(this, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.prev_imageButton_rssWidget, actionPendingIntent);

            active = new Intent(this, RssWidget.class);
            active.setAction(Const.ACTION_WIDGET_NEXT);
            actionPendingIntent = PendingIntent.getBroadcast(this, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.next_imageButton_rssWidget, actionPendingIntent);

            mWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}