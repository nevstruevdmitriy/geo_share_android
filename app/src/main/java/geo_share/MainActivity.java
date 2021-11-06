package geo_share;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import geo_share.GeoRequestBuilder;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public void clickSharing(View view) {
        view.setBackgroundColor(Color.BLUE);

        String url = "https://geo-shared.herokuapp.com/get_point";

        StringRequest stringRequest = GeoRequestBuilder.request("from androids", 1, 2);
        mRequestQueue.add(stringRequest);
    }
}