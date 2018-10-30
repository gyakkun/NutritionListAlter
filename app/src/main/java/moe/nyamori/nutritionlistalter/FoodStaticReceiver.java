package moe.nyamori.nutritionlistalter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FoodStaticReceiver extends BroadcastReceiver {

    private static final String STATIC_ACTION = "moe.nyamori.nutritionlistalter.foodstaticfilter";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(STATIC_ACTION)){
            Bundle bundle = intent.getExtras();
            //TODO: Add notification.
        }
    }
}
