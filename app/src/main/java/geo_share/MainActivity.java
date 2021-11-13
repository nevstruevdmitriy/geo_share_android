package geo_share;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private GeolocationService geo_location_service;
    private WorkManager work_manager;
    private static final String TAG = "MainActivity";
    private static final String SHARING_GRO_TAG = "GeoSharing";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private boolean isPermissionsGranted() {
        Log.i(TAG, "Starting checkPermissions");
        return ActivityCompat.checkSelfPermission(MainApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        Log.i(TAG, "Starting startLocationPermissionRequest");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainApplication.setContext(this);

        geo_location_service = GeolocationService.getInstance();
        work_manager = WorkManager.getInstance();
    }

    public void clickSharing(View view) {
        if (!isPermissionsGranted()) {
            startLocationPermissionRequest();
        }

        if (!isPermissionsGranted()) {
            Log.i(TAG, "Permission denied");
            geo_location_service.setEnable(false);
        } else {
            geo_location_service.setEnable(!geo_location_service.getEnable());
            geo_location_service.sendLocation();
        }
        ToggleButton button = (ToggleButton)view;
        button.setChecked(geo_location_service.getEnable());

        if (geo_location_service.getEnable()) {
            PeriodicWorkRequest GeolocationWork = new PeriodicWorkRequest.Builder(GeolocationWorker.class, 30, TimeUnit.MINUTES).addTag(SHARING_GRO_TAG).build();
            work_manager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, GeolocationWork);
        }
    }
}