package geo_share;

import android.location.Location;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;

public class GeolocationService {
    private final FusedLocationProviderClient fusedLocationClient;
    private Date last_send_location;
    private boolean enable_sharing = false;
    private static final String TAG = GeolocationService.class.getSimpleName();
    private static GeolocationService geolocation_service_instance = null;

    private GeolocationService() {
        Log.i(TAG, "Starting GeolocationService");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainApplication.getContext());
    }

    public static GeolocationService getInstance() {
        if (geolocation_service_instance == null) {
            geolocation_service_instance = new GeolocationService();
        }
        return geolocation_service_instance;
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

    public void sendLocation() {
        Log.i(TAG, "Starting sendLocation");
        if (!enable_sharing) {
            Log.i(TAG, "Sharing disabled");
            return;
        }
        Log.i(TAG, "Starting get location");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.e(TAG, "Failed get location");
                            return;
                        }
                        Log.i(TAG, "Location is " + location.toString());
                        last_send_location = Calendar.getInstance().getTime();
                        StringRequest stringRequest = GeoRequestBuilder.request("fromAndroid", location);
                        MainApplication.getRequestQueue().add(stringRequest);
                    }
                });

    }

}
