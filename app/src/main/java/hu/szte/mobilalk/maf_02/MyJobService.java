package hu.szte.mobilalk.maf_02;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyJobService extends JobService {

    private NotificationManager mMotify;
    private static final String PRIMARY_CHANNEL = "myjobservice_primary_channel";

    public MyJobService() {
        this.mMotify = getSystemService(NotificationManager.class);
    }

    public void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "jobservice channel";
            String description = "ez egy pelda broadcast csatorna";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                new NotificationChannel(PRIMARY_CHANNEL, name, importance);
            channel.setDescription(description);
            this.mMotify.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, PRIMARY_CHANNEL)
                .setContentTitle(this.getClass().getName())
                .setContentText("The job finished successful")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        this.mMotify.notify(0, builder.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(getClass().getName(), "The job has been stopped");
        return false;
    }


}
