package moe.nyamori.nutritionlistalter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FoodDetailActivity extends AppCompatActivity {

    private static final String EXTRA_FOOD_NAME = "moe.nyamori.nutritionlistalter.foodname";
    private static final String DYNAMIC_FILTER = "moe.nyamori.nutritionlistalter.fooddynamicfilter";
    private static final String WIDGET_DYNAMIC_FILTER = "moe.nyamori.nutritionlistalter.widgetfooddynamicfilter";

    private Food mFood;
    private RelativeLayout mDetailTop;
    private TextView mDetailFoodName;
    private TextView mDetailFoodCat;
    private TextView mDetailFoodNutrition;
    private ImageButton mDetailStar;
    private ImageButton mDetailCart;
    private ImageButton mBack;
    private ListView mFunctionList;

    private BroadcastReceiver dynamicReceiver;
    private BroadcastReceiver widgetDynamicReceiver;


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
        mBack = (ImageButton) findViewById(R.id.detail_back_button);
        mFunctionList = (ListView) findViewById(R.id.detail_functions);


        String foodName = getIntent().getStringExtra(EXTRA_FOOD_NAME);
        mFood = FoodStore.get(getApplicationContext()).getFood(foodName);

        mDetailTop.setBackgroundColor(Color.parseColor(mFood.getBgColor()));
        mDetailFoodName.setText(mFood.getName());
        mDetailFoodCat.setText(mFood.getCat());
        mDetailFoodNutrition.setText("富含 " + mFood.getNutrition());

        mDetailStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFood.isStar()) {
                    mFood.setStar(false);
                    Toast.makeText(getApplicationContext(),
                            "已取消" + mFood.getName() + "收藏",

                            Toast.LENGTH_SHORT).show();
                } else {
                    mFood.setStar(true);
                    Toast.makeText(getApplicationContext(),
                            mFood.getName() + "已收藏",
                            Toast.LENGTH_SHORT).show();
                }
                updateStar();
            }
        });

        updateStar();

        mDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventBus.getDefault().post(new MessageEvent(mFood.getName()));

                Intent intentBroadcast = new Intent();
                intentBroadcast.setAction(DYNAMIC_FILTER);

                Bundle bundle = new Bundle();
                bundle.putString("name", mFood.getName());
                bundle.putString("action", "已收藏");

                intentBroadcast.putExtras(bundle);
                sendBroadcast(intentBroadcast);

                Intent widgetDynamicBroadcastIntent = new Intent();
                widgetDynamicBroadcastIntent.setAction(WIDGET_DYNAMIC_FILTER);

                widgetDynamicBroadcastIntent.putExtras(bundle);
                sendBroadcast(widgetDynamicBroadcastIntent);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<Map<String, String>> functionList = new ArrayList<>();
        String[] info = new String[]{"分享信息", "不感兴趣", "查看更多信息", "出错反馈"};
        for (int ctr = 0; ctr < 4; ctr++) {
            Map<String, String> temp = new LinkedHashMap<>();
            temp.put("MSG", info[ctr]);
            functionList.add(temp);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, functionList,
                R.layout.list_item_function,
                new String[]{"MSG"},
                new int[]{R.id.function});

        mFunctionList.setAdapter(simpleAdapter);

        IntentFilter dynamicFilter = new IntentFilter();
        dynamicFilter.addAction(DYNAMIC_FILTER);
        dynamicReceiver = new FoodDynamicReceiver();
        registerReceiver(dynamicReceiver, dynamicFilter);

        IntentFilter widgetDynamicFilter = new IntentFilter();
        widgetDynamicFilter.addAction(WIDGET_DYNAMIC_FILTER);
        widgetDynamicReceiver = new FoodDynamicReceiver();
        registerReceiver(widgetDynamicReceiver, widgetDynamicFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicReceiver);
        unregisterReceiver(widgetDynamicReceiver);
    }

    private void updateStar() {
        if (mFood.isStar()) {
            mDetailStar.setImageResource(R.drawable.ic_action_starred);
        } else {
            mDetailStar.setImageResource(R.drawable.ic_action_star_empty);
        }
    }


    public static Intent newIntent(Context packageContext, String foodName) {
        Intent intent = new Intent(packageContext, FoodDetailActivity.class);
        intent.putExtra(EXTRA_FOOD_NAME, foodName);
        return intent;
    }
}
