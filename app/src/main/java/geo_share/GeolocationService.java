package geo_share;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Callable;

public class GeolocationService {
    private static final String TAG = GeolocationService.class.getSimpleName();
    private static GeolocationService geolocation_service_instance = null;

    public static GeolocationService getInstance() {
        if (geolocation_service_instance == null) {
            geolocation_service_instance = new GeolocationService();
        }
        return geolocation_service_instance;
    }

    public void sendLocation(Context context, RequestQueue request_queue) {
        Log.i(TAG, "Starting sendLocation");
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.e(TAG, "Failed get location");
                            return;
                        }
                        Log.i(TAG, "Location is " + location.toString());
                        StringRequest stringRequest = GeoRequestBuilder.request("fromAndroid", location);
                        request_queue.add(stringRequest);
                    }
                });

    }

}
