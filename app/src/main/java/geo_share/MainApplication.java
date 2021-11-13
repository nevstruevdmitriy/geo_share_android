package geo_share;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainApplication extends Application {
    private static Context context;
    private static RequestQueue request_queue;

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        return request_queue;
    }

    public static void setContext(Context context_) {
        context = context_;
        request_queue = Volley.newRequestQueue(context_);
    }
}