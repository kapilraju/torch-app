package com.kapil.torch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.widget.RemoteViews;

public class TorchWidgetProvider extends AppWidgetProvider {

	private final String CLASS_NAME = "TorchWidgetProvider";

	private final static String WIDGET_BUTTON_TOGGLE = "widgetButtonToggle";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final int N = appWidgetIds.length;
		Log.i(CLASS_NAME, "onUpdate");

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			Log.i(CLASS_NAME, "appWidgetId:" + appWidgetId);

			Intent recieverIntent = new Intent(context, TorchWidgetReciever.class);
			recieverIntent.setAction(WIDGET_BUTTON_TOGGLE);
			recieverIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, recieverIntent, 0);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			remoteViews.setOnClickPendingIntent(R.id.widgetButton,
					pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}
	}

}
