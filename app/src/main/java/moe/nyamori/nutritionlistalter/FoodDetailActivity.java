package moe.nyamori.nutritionlistalter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class FoodDetailActivity extends AppCompatActivity {

    private static final String EXTRA_FOOD_NAME = "moe.nyamori.nutritionlistalter.foodname";
    private static final String ARG_FOOD_NAME = "food_name";
    private Food mFood;


    public FoodDetailActivity newInstance(String foodName){
        mFood = FoodStore.get(FoodDetailActivity.this).getFood(foodName);
        FoodDetailActivity activity = new FoodDetailActivity();

        return activity;
    }

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
