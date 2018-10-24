package moe.nyamori.nutritionlistalter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class FoodStore {
    private static FoodStore sFoodStore;

    private List<Food> mFoods;

    public static FoodStore get(Context context) {
        if (sFoodStore == null) {
            sFoodStore = new FoodStore(context);
        }

        return sFoodStore;
    }

    private FoodStore(Context context) {
        mFoods = new ArrayList<>();

        mFoods.add(new Food("大豆", "粮食", "蛋白质", "#BB4C3B"));
        mFoods.add(new Food("十字花科蔬菜", "蔬菜", "维生素C", "#C48D30"));
        mFoods.add(new Food("牛奶", "饮品", "钙", "#4469B0"));
        mFoods.add(new Food("海鱼", "肉食", "蛋白质", "#20A17B"));
        mFoods.add(new Food("菌菇类", "蔬菜", "微量元素", "#BB4C3B"));
        mFoods.add(new Food("番茄", "蔬菜", "番茄红素", "#4469B0"));
        mFoods.add(new Food("胡萝卜", "蔬菜", "胡萝卜素", "#20A17B"));
        mFoods.add(new Food("荞麦", "粮食", "膳食纤维", "#BB4C3B"));
        mFoods.add(new Food("鸡蛋", "杂", "几乎所有营养物质", "#C48D30"));
    }

    public List<Food> getFoods(){
        return mFoods;
    }

    public Food getFood(String foodName){
        for(Food food:mFoods){
            if(food.getName().equals(foodName)){
                return food;
            }
        }
        return null;
    }
}
