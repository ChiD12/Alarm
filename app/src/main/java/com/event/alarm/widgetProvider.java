package com.event.alarm;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class widgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetID : appWidgetIds) {
            /*Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);*/

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("methodName","myMethod");
            //intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);


            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);




            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);
            views.setOnClickPendingIntent(R.id.wdgBT,pendingIntent);



            appWidgetManager.updateAppWidget(appWidgetID,views);

        }
    }
}
