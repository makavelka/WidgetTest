package com.example.android.widgettest.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.example.android.widgettest.Const;
import com.example.android.widgettest.R;

public class UpdateWidgetService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager mWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {
            RemoteViews views = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.rss_widget);
            Intent intent1 = new Intent(Const.WIDGET_LEFT);
            intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.prev_imageButton_rssWidget, pendingIntent);

            Intent intent2 = new Intent(Const.WIDGET_RIGHT);
            intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent pendingIntentl = PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.next_imageButton_rssWidget, pendingIntentl);

            Intent intent3 = new Intent(Const.WIDGET_RIGHT);
            intent3.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.conf_imageButton_rssWidget, pendingIntent2);

            mWidgetManager.updateAppWidget(widgetId, views);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}