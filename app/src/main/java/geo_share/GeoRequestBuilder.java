package geo_share;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Locale;

public class GeoRequestBuilder {
    public static StringRequest request(String id, double latitude, double longitude) {
        String url = String.format(Locale.getDefault(), "https://geo-shared.herokuapp.com/new_point?userId=%s&latitude=%f&longitude=%f", id, latitude, longitude);

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("tag", "onResponse: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag", "onResponse: did not work!");
            }
        });
    }
}
