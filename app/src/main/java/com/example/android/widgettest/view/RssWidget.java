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
import com.example.android.widgettest.service.GetRssDataService;
import com.example.android.widgettest.service.UpdateWidgetService;

import java.util.ArrayList;

public class RssWidget extends AppWidgetProvider implements WidgetActions {

    private static int mCurrent;
    private static ArrayList<RssItem> mRssItems;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getSerializableExtra(Const.RSS_ITEMS_TAG) != null) {
            mRssItems = (ArrayList<RssItem>) intent.getSerializableExtra(Const.RSS_ITEMS_TAG);
            updateData(context);
        }
        if (intent.getAction().equals(Const.ACTION_WIDGET_CONF)) {
            context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (intent.getAction().equals(Const.ACTION_WIDGET_PREV)) {
            onPrevPressed();
            updateData(context);
        } else if (intent.getAction().equals(Const.ACTION_WIDGET_NEXT)) {
            onNextPressed();
            updateData(context);
        } else {
            super.onReceive(context, intent);
        }

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
        ComponentName thisWidget = new ComponentName(context,
                RssWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context.getApplicationContext(),
                UpdateWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        context.startService(intent);
        for (int i : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, i);
        }

    }

    @Override
    public void onEnabled(Context context) {
        updateData(context);
        Intent intent = new Intent(context, GetRssDataService.class);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
    }


    @Override
    public void onNextPressed() {
        if (mRssItems == null) {
            return;
        }

        if (mCurrent == mRssItems.size() - 1) {
            return;
        }
        mCurrent = mCurrent + 1;
    }

    @Override
    public void onPrevPressed() {
        if (mRssItems == null) {
            return;
        }
        if (mCurrent == 0) {
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
