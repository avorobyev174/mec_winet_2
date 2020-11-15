package com.avorobyev174.mec_winet.classes.common;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentFragment;
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.floor.FloorFragment;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.house.HouseFragment;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.section.SectionFragment;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleFragment;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetFragment;
import com.avorobyev174.mec_winet.classes.winetData.WinetData;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataFragment;

public class Utils {

    public static boolean changeOtherButtonColor(View view, MotionEvent motionEvent, Context context) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.grey),
                    PorterDuff.Mode.SRC_ATOP);
        }
        return false;
    }

    public static boolean changeOtherTextViewColor(View view, MotionEvent motionEvent, Context context) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((TextView)view).setTextColor(context.getResources().getColor(R.color.black));
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            ((TextView)view).setTextColor(context.getResources().getColor(R.color.grey));
        }
        return true;
    }

    public static boolean changeRemoveButtonColor(View view, MotionEvent motionEvent, Context context) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageView)view).setColorFilter(context.getResources().getColor(R.color.dark_red),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
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
            case 11: return "Меркурий 206";
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

    public static EntityFragment getFragmentByEntity(Entity moveEntity, ProgressBar progressBar, FragmentManager fragmentManager) {
        if (moveEntity == null) {
            return HouseFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(House.class)) {
            return SectionFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(Section.class)) {
            return FloorFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(Floor.class)) {
            return VestibuleFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(Vestibule.class)) {
            return WinetFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(Winet.class)) {
            return ApartmentFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(WinetData.class)) {
            return WinetDataFragment.newInstance(progressBar, fragmentManager);
        } else if (moveEntity.getClass().equals(Apartment.class)) {
            return ApartmentFragment.newInstance(progressBar, fragmentManager);
        }
        return null;
    }
}
