package moe.nyamori.nutritionlistalter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class FoodDynamicReceiver extends BroadcastReceiver {

    private static final String DYNAMIC_ACTION = "moe.nyamori.nutritionlistalter.fooddynamicfilter";
    private static final String EXTRA_IS_SHOPPING_LIST_SHOWN = "moe.nyamori.nutritionlistalter.isshoppinglistshown";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMIC_ACTION)) {
            Bundle bundle = intent.getExtras();

            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentTitle("收藏通知")
                    .setContentText(bundle.getString("name") + "已收藏")
                    .setTicker("您收藏了新的食物")
                    .setSmallIcon(R.drawable.ic_action_starred)
                    .setAutoCancel(true);

            Intent intentToList = FoodListActivity.newIntentToList(context, true);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intentToList, FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = builder.build();
            manager.notify(0, notification);

        }
    }
}
