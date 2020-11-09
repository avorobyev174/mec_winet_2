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

    public static boolean changeOtherButtonColor(View view, MotionEvent motionEvent, Context context) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            // pointer goes up
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.grey),
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

    public static String getApartmentTypeTitle(Context context, String apartmentType) {
        if (apartmentType.equals("1")) {
            return context.getResources().getString(R.string.apartment_type_phizical);
        } else  {
            return context.getResources().getString(R.string.apartment_type_commercial);
        }
    }

    public static String getMeterTypeTitle(int meterType) {
        switch (meterType) {
            case 1: return "СОЭ-55/60Ш-Т-415";
            case 2: return "АГАТ 2-45";
            case 3: return "Нева МТ 324";
            case 5: return "Меркурий 200.Х";
            case 9: return "Меркурий 23X";
            case 11: return  "Меркурий 206";
            default: return "неизвестно";
        }
    }

    public static int getMeterType(String meterType) {
        switch (meterType) {
            case "СОЭ-55/60Ш-Т-415" : return 1;
            case "АГАТ 2-45" : return 2;
            case "Нева МТ 324" : return 3;
            case "Меркурий 200.Х" : return 5;
            case "Меркурий 23X" : return 9;
            case "Меркурий 206" : return 11;
            default: return 0;
        }
    }
}
