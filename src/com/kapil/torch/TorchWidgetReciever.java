package com.kapil.torch;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.widget.RemoteViews;

public class TorchWidgetReciever extends BroadcastReceiver {

	private static final String CLASS_NAME = "TorchWidgetReciever";
	
	private static final String TORCH_WIDGET_PREF = "torchWidgetPreference";

	private static final String FLASH_STATE = "flashState";
	
	private Camera camera = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(CLASS_NAME, "onReceive: " + this.hashCode());
		Log.i(CLASS_NAME, "intent action: " + intent.getAction());

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		ComponentName componentName = new ComponentName(context,
				TorchWidgetProvider.class);
		
		boolean flashState = isFlashOn(context);
		
		
		
		Log.i(CLASS_NAME, "Flash State: " + flashState);

		if (flashState) {
			turnFlashON();
			remoteViews.setImageViewResource(R.id.widgetButton,
					R.drawable.ic_launcher);
		} else {
			turnFlashOFF();
			remoteViews.setImageViewResource(R.id.widgetButton,
					R.drawable.widget_img);
		}
		appWidgetManager.updateAppWidget(componentName, remoteViews);
		setFlashState(context, !flashState);
		
	}

	private void turnFlashOFF() {
		Log.d(CLASS_NAME, "turning flash OFF");
		if(camera == null)
			camera = Camera.open();
		Parameters params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(params);
		camera.startPreview();
		camera.release();
	}

	private void turnFlashON() {
		Log.d(CLASS_NAME, "turning flash ON");

		if(camera == null)
			camera = Camera.open();
		
		Parameters params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(params);
		camera.startPreview();
		camera.release();
	}
	
	private boolean isFlashOn(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				TORCH_WIDGET_PREF, 0);
		return settings.getBoolean(FLASH_STATE, false);
	}

	private void setFlashState(Context context, boolean state) {
		SharedPreferences settings = context.getSharedPreferences(
				TORCH_WIDGET_PREF, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(FLASH_STATE, state);
		editor.commit();
	}

}
