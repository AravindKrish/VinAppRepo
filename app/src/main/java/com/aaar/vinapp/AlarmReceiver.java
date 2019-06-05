package com.aaar.vinapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.aaar.vinapp.ShowAllTopicsActivity.CHANNEL_ID;

public class AlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "From Broadcast Receiver", Toast.LENGTH_SHORT).show();
        Log.d("My Alarm Aravind", "Alarmjusttriggered");

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notificationCompat = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("Revision Coming up")
//                .setContentText("Your next revision time is now ready")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        Intent contentIntent = new Intent(context, ShowAllTopicsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(contentIntent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        // if(ShowAllTopicsActivity.isactivityrunning = false){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Read Revise Remember")
                    .setContentText("Few Revisions today when you get a chance?")
                    .setContentIntent(resultPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        //}

        //else {

          //  NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            //        .setSmallIcon(R.drawable.notification_icon)
              //      .setContentTitle("Read Revise Remember")
                //    .setContentText("Few Revisions today when you get a chance?")
                  //  .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            //notificationManager.notify(1, builder.build());

        //}






    }


}
