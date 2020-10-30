package com.avorobyev174.mec_winet.classes.common;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.avorobyev174.mec_winet.R;

public class Utils {
    public static boolean changeAddButtonColor(View view, MotionEvent motionEvent, Context context) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.dark_green),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // pointer goes up
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.green),
                    PorterDuff.Mode.SRC_ATOP);
        }
        return false;
    }

    public static boolean changeRemoveButtonColor(View view, MotionEvent motionEvent, Context context) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.dark_red),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // pointer goes up
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.red),
                    PorterDuff.Mode.SRC_ATOP);
        }
        return false;
    }
}
