package com.example.android.widgettest.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.android.widgettest.Const;
import com.example.android.widgettest.PrefsUtils;
import com.example.android.widgettest.service.GetRssDataService;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent_received) {
        setUpNextAlarmInMin(context);
        String url = PrefsUtils.getUrl(context);
        Intent intent = new Intent(context, GetRssDataService.class);
        intent.putExtra(Const.URL_TAG, url);
        startWakefulService(context, intent);
//        Intent intentWidget = new Intent(context, RssWidget.class);
//        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, RssWidget.class));
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        context.sendBroadcast(intentWidget);
    }

    private void setUpNextAlarmInMin(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.roll(Calendar.MINUTE, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }


}