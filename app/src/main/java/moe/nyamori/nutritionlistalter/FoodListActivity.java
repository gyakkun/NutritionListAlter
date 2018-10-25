package moe.nyamori.nutritionlistalter;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView mAllFoodRecyclerView;
    private RecyclerView mShoppingListRecyclerView;
    private AllFoodAdapter mAllFoodsAdapter;
    private ShoppingListFoodAdapter mShoppingListAdapter;
    private static List<Food> mAllFoods;
    private static List<Food> mShoppingListFoods;
    private boolean mIsShoppingListShown;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        mAllFoods = new ArrayList<>();
        mShoppingListFoods = new ArrayList<>();

        mAllFoodRecyclerView = (RecyclerView) findViewById(R.id.all_food_list);
        mShoppingListRecyclerView = (RecyclerView) findViewById(R.id.shoppling_list);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mAllFoodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mShoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Food> foodStore = FoodStore.get(FoodListActivity.this).getFoods();

        for (int ctr = 0; ctr < foodStore.size(); ctr++) {
            mAllFoods.add(foodStore.get(ctr));
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsShoppingListShown = !mIsShoppingListShown;
                updateUI();
            }
        });

        updateUI();
    }

    private void updateUI() {
        if (mIsShoppingListShown) {
            mShoppingListAdapter = new ShoppingListFoodAdapter(mShoppingListFoods);
            mShoppingListRecyclerView.setAdapter(mShoppingListAdapter);
            mAllFoodRecyclerView.setVisibility(View.INVISIBLE);
            mShoppingListRecyclerView.setVisibility(View.VISIBLE);
            return;
        }
        mAllFoodsAdapter = new AllFoodAdapter(mAllFoods);
        mAllFoodRecyclerView.setAdapter(mAllFoodsAdapter);
        mShoppingListRecyclerView.setVisibility(View.INVISIBLE);
        mAllFoodRecyclerView.setVisibility(View.VISIBLE);
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
        public abstract boolean onLongClick(View view) ;
    }

    private class AllFoodHolder extends FoodHolder{

        public AllFoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent);
        }

        @Override
        public boolean onLongClick(View view)
            {
                Toast.makeText(getApplicationContext(), "删除" + super.mItemFood.getName(), Toast.LENGTH_SHORT).show();
                mAllFoods.remove(super.mItemFood);
                updateUI();

                return true;
            }

    }

    private class ShoppingListFoodHolder extends FoodHolder{

        public ShoppingListFoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent);
        }

        @Override
        public boolean onLongClick(View view)
        {
//            AlertDialog alertDialog = new AlertDialog()


            mAllFoods.remove(super.mItemFood);
            updateUI();

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

    public static boolean addFoodToCart(String foodName) {
        //What should be the context here? : new FoodListActivity() / getApplicationContext()
        if (mShoppingListFoods.contains(FoodStore.get(new FoodListActivity()).getFood(foodName))) {
            return false;
        }

        mShoppingListFoods.add(FoodStore.get(new FoodListActivity()).getFood(foodName));
        return true;
    }
}
