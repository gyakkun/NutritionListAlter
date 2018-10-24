package moe.nyamori.nutritionlistalter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class FoodDetailActivity extends AppCompatActivity {

    private static final String EXTRA_FOOD_NAME = "moe.nyamori.nutritionlistalter.foodname";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
    }

    public static Intent newIntent(Context packageContext, String foodName){
        Intent intent = new Intent(packageContext, FoodDetailActivity.class);
        intent.putExtra(EXTRA_FOOD_NAME, foodName);
        return intent;
    }
}
