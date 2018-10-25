package moe.nyamori.nutritionlistalter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class FoodDetailActivity extends AppCompatActivity {

    private static final String EXTRA_FOOD_NAME = "moe.nyamori.nutritionlistalter.foodname";
    private Food mFood;
    private RelativeLayout mDetailTop;
    private TextView mDetailFoodName;
    private TextView mDetailFoodCat;
    private TextView mDetailFoodNutrition;
    private ImageButton mDetailStar;
    private ImageButton mDetailCart;


    public FoodDetailActivity newInstance(String foodName) {
        mFood = FoodStore.get(FoodDetailActivity.this).getFood(foodName);
        FoodDetailActivity activity = new FoodDetailActivity();

        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mDetailTop = (RelativeLayout) findViewById(R.id.detail_top);
        mDetailFoodName = (TextView) findViewById(R.id.detail_food_name);
        mDetailFoodCat = (TextView) findViewById(R.id.detail_food_cat);
        mDetailFoodNutrition = (TextView) findViewById(R.id.detail_food_nutrition);
        mDetailStar = (ImageButton) findViewById(R.id.detail_star_button);
        mDetailCart = (ImageButton) findViewById(R.id.detail_shoppling_cart);

        String foodName = getIntent().getStringExtra(EXTRA_FOOD_NAME);
        mFood = FoodStore.get(getApplicationContext()).getFood(foodName);

        mDetailTop.setBackgroundColor(Color.parseColor(mFood.getBgColor()));
        mDetailFoodName.setText(mFood.getName());
        mDetailFoodCat.setText(mFood.getCat());
        mDetailFoodNutrition.setText("富含 " + mFood.getNutrition());

        if (mFood.isStar()) {
            mDetailStar.setImageResource(R.drawable.ic_action_starred);
        } else {
            mDetailStar.setImageResource(R.drawable.ic_action_star_empty);
        }

        mDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FoodListActivity.addFoodToCart(mFood.getName())) {
                    Toast.makeText(getApplicationContext(),
                            mFood.getName() + " 已经加入购物车",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            mFood.getName() + " 已在购物车，不能重复添加",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public static Intent newIntent(Context packageContext, String foodName) {
        Intent intent = new Intent(packageContext, FoodDetailActivity.class);
        intent.putExtra(EXTRA_FOOD_NAME, foodName);
        return intent;
    }
}
