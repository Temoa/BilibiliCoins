package me.temoa.mariocoins;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Lai
 * on 2017/8/16 16:36
 */

public class utils {

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.getResources().getDisplayMetrics());
    }
}
