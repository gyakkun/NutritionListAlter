package moe.nyamori.nutritionlistalter;

import java.io.Serializable;

public class Food implements Serializable {


    private String mName;
    private String mCat;
    private String mNutrition;
    private String mBgColor;
    private boolean mStar;

    public boolean isStar() {
        return mStar;
    }

    public void setStar(boolean star) {
        mStar = star;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCat() {
        return mCat;
    }

    public void setCat(String cat) {
        mCat = cat;
    }

    public String getNutrition() {
        return mNutrition;
    }

    public void setNutrition(String nutrition) {
        mNutrition = nutrition;
    }

    public String getBgColor() {
        return mBgColor;
    }

    public void setBgColor(String bgColor) {
        mBgColor = bgColor;
    }


    public Food(String name, String cat, String nutrition, String bgColor) {
        mName = name;
        mCat = cat;
        mNutrition = nutrition;
        mBgColor = bgColor;
    }
}
