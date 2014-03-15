package com.kapil.torch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private boolean isCameraFlashAvailable;
	private Camera camera;
	private Parameters params;
	private Boolean isFlashON;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(ACTIVITY_SERVICE, "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		isCameraFlashAvailable = this.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);
		isFlashON = false;

		openCamera();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(ACTIVITY_SERVICE, "onCreateOptionsMenu");

		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.about:
			startAboutActivity();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void startAboutActivity() {
		Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
		startActivity(intent);
	}

	public void onToggleClicked(View view) {

		if (isCameraFlashAvailable) {
			if (!isFlashON) {
				Log.d(ACTIVITY_SERVICE, "turning flash light ON");
				turnFlashON();
				ImageButton imgButton = (ImageButton) view;
				imgButton.setImageResource(R.drawable.light_bulb_on);
				isFlashON = true;
			} else {
				Log.d(ACTIVITY_SERVICE, "turning flash light OFF");
				turnFlashOFF();
				ImageButton imgButton = (ImageButton) view;
				imgButton.setImageResource(R.drawable.light_bulb_off);
				isFlashON = false;
			}
		} else {
			showErrorToast();
		}

	}

	private void showErrorToast() {
		Toast.makeText(getApplicationContext(),
				"No Camera Flash available!", Toast.LENGTH_SHORT).show();
	}

	private void turnFlashOFF() {
		Log.d(ACTIVITY_SERVICE, "turning flash OFF");

		params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(params);
		camera.stopPreview();
	}

	private void turnFlashON() {
		Log.d(ACTIVITY_SERVICE, "turning flash ON");

		openCamera();
		params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(params);
		camera.startPreview();
	}

	private void openCamera() {
		if (isCameraFlashAvailable && camera == null) {
			Log.d(ACTIVITY_SERVICE, "Opening camera");
			camera = Camera.open();
		}
	}

	@Override
	protected void onResume() {
		Log.d(ACTIVITY_SERVICE, "onResume");
		super.onResume();
		openCamera();
		if (isCameraFlashAvailable && isFlashON) {
			turnFlashON();
		}
	}

	@Override
	protected void onPause() {
		Log.d(ACTIVITY_SERVICE, "onPause");
		super.onPause();
		if (isCameraFlashAvailable && isFlashON)
			turnFlashOFF();
	}

	@Override
	protected void onStop() {
		Log.d(ACTIVITY_SERVICE, "onStop");

		super.onStop();

		releaseCamera();
	}

	private void releaseCamera() {
		if (isCameraFlashAvailable && camera != null) {
			Log.d(ACTIVITY_SERVICE, "Releasing camera");
			camera.release();
			camera = null;
		}
	}

}
