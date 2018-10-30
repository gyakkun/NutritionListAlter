package moe.nyamori.nutritionlistalter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class FoodStaticReceiver extends BroadcastReceiver {

    private static final String STATIC_ACTION = "moe.nyamori.nutritionlistalter.foodstaticfilter";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(STATIC_ACTION)){
            Bundle bundle = intent.getExtras();

            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentTitle("今日推荐")
                    .setContentText("推荐食物" + bundle.getString("name"))
                    .setTicker("为您推送推荐食物")
                    .setSmallIcon(R.drawable.ic_action_starred)
                    .setAutoCancel(true);

            Intent intentToDetail = FoodDetailActivity.newIntent(context, bundle.getString("name"));

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intentToDetail, FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = builder.build();
            manager.notify(0, notification);

        }
    }
}
