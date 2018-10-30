package moe.nyamori.nutritionlistalter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class FoodDynamicReceiver extends BroadcastReceiver {

    private static final String DYNAMIC_ACTION = "moe.nyamori.nutritionlistalter.fooddynamicfilter";
    private static final String WIDGET_DYNAMIC_ACTION = "moe.nyamori.nutritionlistalter.widgetfooddynamicfilter";

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

        } else if (intent.getAction().equals(WIDGET_DYNAMIC_ACTION)) {
            Bundle bundle = intent.getExtras();

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.food_widget);
            String foodName = bundle.getString("name");
            String action = bundle.getString("action");
            views.setTextViewText(R.id.widget_food_text, action + foodName);


            Intent intentToDetail = FoodDetailActivity.newIntent(context, foodName);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intentToDetail, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_food_icon, pendingIntent); //设置点击事件

            ComponentName componentName = new ComponentName(context, FoodWidget.class);
            manager.updateAppWidget(componentName, views);


        }
    }
}
