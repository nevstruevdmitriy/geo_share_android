package geo_share;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static RequestQueue request_queue;
    private Button button;
    MapView map = null;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean isPermissionsGranted() {
        Log.i(TAG, "Starting checkPermissions");
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void startLocationPermissionRequest() {
        Log.i(TAG, "Starting startLocationPermissionRequest");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.Button);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        request_queue = Volley.newRequestQueue(this);

        setCenterAndMarker();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setCenterAndMarker() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!isPermissionsGranted()) {
            startLocationPermissionRequest();
            if (!isPermissionsGranted()) {
                Log.i(TAG, "Permission denied");
            }
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.e(TAG, "Failed get location");
                            return;
                        }
                        Log.i(TAG, "Location is " + location.toString());

                        IMapController mapController = map.getController();
                        mapController.setZoom(15.0);
                        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        mapController.setCenter(startPoint);

                        Marker startMarker = new Marker(map);
                        startMarker.setTitle("Your location");
                        startMarker.setPosition(startPoint);
                        map.getOverlays().add(startMarker);
                    }
                });
    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void clickSharing(View view) {
        if (!isPermissionsGranted()) {
            startLocationPermissionRequest();
            if (!isPermissionsGranted()) {
                Log.i(TAG, "Permission denied");
            }
        }

        GeolocationService.getInstance().sendLocation(this, request_queue);
    }
}