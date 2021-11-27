package geo_share;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

public class GeolocationWorker extends Worker {
    private static RequestQueue request_queue;
    private static final String SHARING_GRO_TAG = "SHARING_GRO_TAG";

    public GeolocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        request_queue = Volley.newRequestQueue(context);
    }

    public static void enqueueWork() {
        OneTimeWorkRequest work =
                new OneTimeWorkRequest.Builder(GeolocationWorker.class)
                        .setInitialDelay(10, TimeUnit.SECONDS)
                        .addTag(SHARING_GRO_TAG)
                        .build();
        WorkManager.getInstance().enqueueUniqueWork(SHARING_GRO_TAG, ExistingWorkPolicy.REPLACE, work);
    }

    public static void cancelWork() {
        WorkManager.getInstance().cancelAllWorkByTag(SHARING_GRO_TAG);
    }

    public static void immediatelyWork() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(GeolocationWorker.class).build();
        WorkManager.getInstance().enqueue(work);
    }

    @NonNull
    @Override
    public Result doWork() {
        GeolocationService.getInstance().sendLocation(getApplicationContext(), request_queue);
        enqueueWork();
        return Result.success();
    }
}
