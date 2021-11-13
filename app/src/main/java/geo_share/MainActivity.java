package geo_share;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import geo_share.GeolocationService;

public class MainActivity extends AppCompatActivity {
    GeolocationService geo_location_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geo_location_service = new GeolocationService(this);
    }

    public void clickSharing(View view) {
        geo_location_service.setEnable(!geo_location_service.getEnable());
        geo_location_service.sendLocation();
    }
}