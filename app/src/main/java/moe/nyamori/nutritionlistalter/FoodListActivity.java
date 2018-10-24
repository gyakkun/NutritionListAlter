package moe.nyamori.nutritionlistalter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView mAllFoodRecyclerView;
    private RecyclerView mShoppingListRecyclerView;
    private List<Food> mAllFoods;
    private List<Food> mShoppingListFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        List<Food> foodStore = FoodStore.get(FoodListActivity.this).getFoods();
        for(Food food:foodStore){
            mAllFoods.add(food);
        }
    }

    private class FoodHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        public FoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_food,
                    parent,
                    false));
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class FoodAdapter extends  RecyclerView.Adapter<FoodHolder>{

        private List<Food> mFoods;

        @Override
        public FoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(FoodHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mFoods.size();
        }
    }
}
