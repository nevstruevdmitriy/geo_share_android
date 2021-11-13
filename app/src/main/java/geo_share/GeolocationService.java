package geo_share;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;

public class GeolocationService {
    private final RequestQueue mRequestQueue;
    private final FusedLocationProviderClient fusedLocationClient;
    private final Activity main_screen;
    private Date last_send_location;
    private boolean enable_sharing = false;
    private static final String TAG = GeolocationService.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    public GeolocationService(Activity screen) {
        Log.i(TAG, "Starting GeolocationService");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(screen);
        mRequestQueue = Volley.newRequestQueue(screen);
        main_screen = screen;
    }

    public void setEnable(boolean enable_sharing_) {
        Log.i(TAG, "Starting setEnable");
        enable_sharing = enable_sharing_;
    }

    public boolean getEnable() {
        Log.i(TAG, "Starting getEnable");
        return enable_sharing;
    }

    public Date getLastSendLocation() {
        Log.i(TAG, "Starting getLastSendLocation");
        return last_send_location;
    }

    private boolean checkPermissions() {
        Log.i(TAG, "Starting checkPermissions");
        return ActivityCompat.checkSelfPermission(main_screen, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(main_screen, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        Log.i(TAG, "Starting startLocationPermissionRequest");
        ActivityCompat.requestPermissions(main_screen,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    public void sendLocation() {
        Log.i(TAG, "Starting sendLocation");
        if (!enable_sharing) {
            return;
        }

        if (checkPermissions()) {
            startLocationPermissionRequest();
        }

        if (checkPermissions()) {
            return;
        }
        Log.i(TAG, "Starting get location");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(main_screen, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.e(TAG, "Failed get location");
                            return;
                        }
                        Log.i(TAG, "Location is " + location.toString());
                        last_send_location = Calendar.getInstance().getTime();
                        StringRequest stringRequest = GeoRequestBuilder.request("fromAndroid", location);
                        mRequestQueue.add(stringRequest);
                    }
                });

    }

}
