package moe.nyamori.nutritionlistalter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.ComposePathEffect;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.io.FileOutputStream;

/**
 * Implementation of App Widget functionality.
 */
public class FoodWidget extends AppWidgetProvider {

    private static final String WIDGET_STATIC_FILTER = "moe.nyamori.nutritionlistalter.widgetstaticfilter";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.food_widget);
        views.setTextViewText(R.id.widget_food_text, widgetText);

        Intent intent = FoodListActivity.newIntentToList(context, false);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_food_icon, pendingIntent);

        ComponentName name = new ComponentName(context, FoodWidget.class);


        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.updateAppWidget(name, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
        String foodName = bundle.getString("name");
        if(intent.getAction().equals(WIDGET_STATIC_FILTER)){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.food_widget);
            views.setTextViewText(R.id.widget_food_text, "今日推荐" + foodName);

            Intent intentToDetail = FoodDetailActivity.newIntent(context, foodName);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intentToDetail, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_food_icon, pendingIntent);

            ComponentName name = new ComponentName(context, FoodWidget.class);
            manager.updateAppWidget(name, views);
        }
    }
}

