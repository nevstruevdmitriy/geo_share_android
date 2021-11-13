package geo_share;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class GeolocationWorker extends Worker {

    public GeolocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        GeolocationService.getInstance().sendLocation();
        return Result.success();
    }
}
