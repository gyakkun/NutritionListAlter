package moe.nyamori.nutritionlistalter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FoodDynamicReceiver extends BroadcastReceiver {

    private static final String DYNAMIC_ACTION = "moe.nyamori.nutritionlistalter.fooddynamicfilter";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DYNAMIC_ACTION)){
            Bundle bundle = intent.getExtras();
            //TODO: Add notification part.
        }
    }
}
