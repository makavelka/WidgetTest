package com.example.android.widgettest.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.widgettest.Const;
import com.example.android.widgettest.model.RssItem;
import com.example.android.widgettest.receiver.AlarmReceiver;
import com.example.android.widgettest.view.RssWidget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GetRssDataService extends IntentService {

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            //get and send location information
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getRssItems(intent.getStringExtra(Const.URL_TAG));
    }

    public GetRssDataService() {
        super("GetRssService");
    }

    public void getRssItems(String feedUrl) {
        if (feedUrl == null) {
            return;
        }
        if (feedUrl.equals("")) {
            return;
        }
        try {
            URL url = new URL(feedUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                parseInputStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void parseInputStream(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<RssItem> arrayList = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(is);
        Element element = document.getDocumentElement();
        String title = "";
        String description = "";
        NodeList nodeList = element.getElementsByTagName("item");
        if (nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element entry = (Element) nodeList.item(i);
                Element titleE = (Element) entry.getElementsByTagName(
                        "title").item(0);
                Element descriptionE = (Element) entry
                        .getElementsByTagName("description").item(0);
                title = titleE.getFirstChild().getNodeValue();
                if (descriptionE != null) {
                    description = descriptionE.getFirstChild().getNodeValue();
                } else {
                    description = "";
                }
                arrayList.add(new RssItem(title, description));
            }
        }
        sendArrayToWidget(arrayList);

    }

    private void sendArrayToWidget(ArrayList<RssItem> arrayList) {
        Intent intent = new Intent(this, RssWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(Const.RSS_ITEMS_TAG, arrayList);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, RssWidget.class));
        if (ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            this.sendBroadcast(intent);
        }
        setAlarm(this);
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.roll(Calendar.MINUTE, 1);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60, pendingIntent);
    }

}
