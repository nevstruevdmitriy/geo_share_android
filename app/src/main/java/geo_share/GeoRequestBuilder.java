package geo_share;

import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GeoRequestBuilder {
    private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    private static final DecimalFormat df = new DecimalFormat();
    private static final String TAG = "GeoRequestBuilder";

    public static StringRequest request(String id, Location location) {
        // fixme
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMinimumFractionDigits(6);

        String url = String.format(Locale.getDefault(), "https://geo-shared.herokuapp.com/new_point?userId=%s&latitude=%s&longitude=%s", id, df.format(location.getLatitude()), df.format(location.getLongitude()));

        return new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: did not work!");
            }
        });
    }
}
