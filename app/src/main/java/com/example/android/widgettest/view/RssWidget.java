package com.example.android.widgettest.view;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.widgettest.Const;
import com.example.android.widgettest.R;
import com.example.android.widgettest.model.RssItem;
import com.example.android.widgettest.service.UpdateWidgetService;

import java.util.ArrayList;

public class RssWidget extends AppWidgetProvider implements WidgetActions {

    private Context mContext;
    private static int mCurrent;
    private AppWidgetManager mWidgetManager;
    private static ArrayList<RssItem> mRssItems;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        switch (intent.getAction()) {
            case Const.WIDGET_CONF:
                return;
            case Const.WIDGET_LEFT:
                onPrevPressed();
                break;
            case Const.WIDGET_RIGHT:
                onNextPressed();
                break;
        }
        mRssItems = (ArrayList<RssItem>) intent.getSerializableExtra(Const.RSS_ITEMS_TAG);
        updateData(context);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        if (mRssItems == null)
            return;
        if (mRssItems.size() == 0)
            return;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rss_widget);
        views.setTextViewText(R.id.title_textView_rssWidget, mRssItems.get(mCurrent).getTitle());
        views.setTextViewText(R.id.dest_textView_rssWidget, mRssItems.get(mCurrent).getDescription());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rss_widget);
//
//        Intent intent = new Intent(Const.WIDGET_LEFT);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.prev_imageButton_rssWidget, pendingIntent);
//
//        Intent intentl = new Intent(Const.WIDGET_RIGHT);
//        PendingIntent pendingIntentl = PendingIntent.getBroadcast(context, 0, intentl, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.next_imageButton_rssWidget, pendingIntentl);
//
//        Intent intent2 = new Intent(Const.WIDGET_RIGHT);
//        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.conf_imageButton_rssWidget, pendingIntent2);
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
        ComponentName thisWidget = new ComponentName(context,
                RssWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                UpdateWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        context.startService(intent);

    }

    @Override
    public void onEnabled(Context context) {
        mContext = context;
    }

    @Override
    public void onDisabled(Context context) {
    }


    @Override
    public void onNextPressed() {
        if (mRssItems == null) {
            return;
        }
        if (mCurrent == 0) {
            return;
        }
        mCurrent++;
    }

    @Override
    public void onPrevPressed() {
        if (mRssItems == null) {
            return;
        }
        if (mCurrent == mRssItems.size() - 1) {
            return;
        }
        mCurrent--;
    }

    @Override
    public void updateData(Context context) {
        Intent intent = new Intent(context, RssWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, RssWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
