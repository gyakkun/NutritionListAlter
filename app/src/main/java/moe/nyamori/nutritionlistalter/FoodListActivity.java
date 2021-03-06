package moe.nyamori.nutritionlistalter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView mAllFoodRecyclerView;
    private RecyclerView mShoppingListRecyclerView;
    private AllFoodAdapter mAllFoodsAdapter;
    private ShoppingListFoodAdapter mShoppingListAdapter;
    private static List<Food> mAllFoods;
    private static List<Food> mShoppingListFoods;
    public static boolean mIsShoppingListShown;
    private FloatingActionButton mFab;

    private static final String STATIC_FILTER = "moe.nyamori.nutritionlistalter.foodstaticfilter";
    private static final String WIDGET_STATIC_FILTER = "moe.nyamori.nutritionlistalter.widgetstaticfilter";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        EventBus.getDefault().register(this);

        mAllFoods = new ArrayList<>();
        mShoppingListFoods = new ArrayList<>();

        mAllFoodRecyclerView = (RecyclerView) findViewById(R.id.all_food_list);
        mShoppingListRecyclerView = (RecyclerView) findViewById(R.id.shoppling_list);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mAllFoodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mShoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Food> foodStore = FoodStore.get(FoodListActivity.this).getFoods();

        //Add all food from food store
        for (int ctr = 0; ctr < foodStore.size(); ctr++) {
            mAllFoods.add(foodStore.get(ctr));
        }

        //Add a dummy item for cart
        mShoppingListFoods.add(new Food("收藏夹", "*", "", "#FFFFFF"));


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsShoppingListShown = !mIsShoppingListShown;
                updateUI();
            }
        });

        updateUI();

        //Static Broadcast, randomly select a food name to send

        Intent intentBroadcast = new Intent(STATIC_FILTER);
        Random r = new Random();
        int randChoice = r.nextInt(9);
        Bundle bundle = new Bundle();

        bundle.putString("name", foodStore.get(randChoice).getName());
        bundle.putString("action", "今日推荐");
        intentBroadcast.putExtras(bundle);


        Intent widgetBroadcastIntent = new Intent(WIDGET_STATIC_FILTER);
        widgetBroadcastIntent.putExtras(bundle);

        sendBroadcast(widgetBroadcastIntent);
        sendBroadcast(intentBroadcast);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI();

    }

    private void updateUI() {
        if (mIsShoppingListShown) {
            mShoppingListAdapter = new ShoppingListFoodAdapter(mShoppingListFoods);
            mShoppingListRecyclerView.setAdapter(mShoppingListAdapter);
            mAllFoodRecyclerView.setVisibility(View.INVISIBLE);
            mShoppingListRecyclerView.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_action_homepage);
            return;
        }
        mAllFoodsAdapter = new AllFoodAdapter(mAllFoods);
        mAllFoodRecyclerView.setAdapter(mAllFoodsAdapter);
        mShoppingListRecyclerView.setVisibility(View.INVISIBLE);
        mAllFoodRecyclerView.setVisibility(View.VISIBLE);
        mFab.setImageResource(R.drawable.ic_action_shopping_cart);

    }

    private abstract class FoodHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private Food mItemFood;
        private TextView mItemCat;
        private TextView mItemName;

        public FoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_food,
                    parent,
                    false));

            Log.w("tmp", "Food holder created.");

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


            mItemCat = (TextView) itemView.findViewById(R.id.item_cat);
            mItemName = (TextView) itemView.findViewById(R.id.item_name);
        }

        public void bind(Food food) {
            mItemFood = food;

            mItemCat.setText(mItemFood.getCat().substring(0, 1));
            mItemName.setText(mItemFood.getName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = FoodDetailActivity.newIntent(getApplicationContext(), mItemFood.getName());
            startActivity(intent);
        }


        @Override
        public abstract boolean onLongClick(View view);
    }

    private class AllFoodHolder extends FoodHolder {

        public AllFoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent);
        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(getApplicationContext(), "删除" + super.mItemFood.getName(), Toast.LENGTH_SHORT).show();
            mAllFoods.remove(super.mItemFood);
            updateUI();

            return true;
        }

    }

    private class ShoppingListFoodHolder extends FoodHolder {

        public ShoppingListFoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent);
        }

        @Override
        public boolean onLongClick(View view) {
            final String foodNameToRemove = super.mItemFood.getName();
            AlertDialog dialog = new AlertDialog.Builder(FoodListActivity.this)
                    .create();//创建对话框
            dialog.setTitle("提示");//设置对话框标题
            dialog.setMessage("确认从购物车中删除" + super.mItemFood.getName() + " 吗?");//设置文字显示内容
            //分别设置三个button
            dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FoodListActivity.this,
                                    foodNameToRemove + "已从购物车删除",
                                    Toast.LENGTH_SHORT).show();
                            mShoppingListFoods.remove(FoodStore
                                    .get(getApplicationContext())
                                    .getFood(foodNameToRemove));
                            updateUI();
                            dialog.dismiss();//关闭对话框
                        }
                    });

            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();//关闭对话框
                        }
                    });
            dialog.show();//显示对话框
            return true;
        }

    }

    private class AllFoodAdapter extends RecyclerView.Adapter<AllFoodHolder> {

        private List<Food> mFoods;

        public AllFoodAdapter(List<Food> foods) {

            mFoods = foods;
        }

        @Override
        public AllFoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

            return new AllFoodHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AllFoodHolder holder, int position) {
            Food food = mFoods.get(position);
            holder.bind(food);
        }

        @Override
        public int getItemCount() {
            return mFoods.size();
        }
    }

    private class ShoppingListFoodAdapter extends RecyclerView.Adapter<ShoppingListFoodHolder> {

        private List<Food> mFoods;

        public ShoppingListFoodAdapter(List<Food> foods) {

            mFoods = foods;
        }

        @Override
        public ShoppingListFoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

            return new ShoppingListFoodHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ShoppingListFoodHolder holder, int position) {
            Food food = mFoods.get(position);
            holder.bind(food);
        }

        @Override
        public int getItemCount() {
            return mFoods.size();
        }
    }

    private boolean addFoodToCart(String foodName) {
        //What should be the context here? : new FoodListActivity() / getApplicationContext()
        if (mShoppingListFoods.contains(FoodStore.get(new FoodListActivity()).getFood(foodName))) {
            Toast.makeText(this,
                    foodName + " 已在购物车，不能重复添加",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(getApplicationContext(),
                foodName + " 已经加入购物车",
                Toast.LENGTH_SHORT).show();
        mShoppingListFoods.add(FoodStore.get(new FoodListActivity()).getFood(foodName));
        return true;
    }

    public static Intent newIntentToList(Context packageContext, boolean isShoppingListShown) {
        Intent intent = new Intent(packageContext, FoodListActivity.class);

        mIsShoppingListShown = isShoppingListShown;

        return intent;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        String foodName = messageEvent.getMessage();
        addFoodToCart(foodName);
    }

}
