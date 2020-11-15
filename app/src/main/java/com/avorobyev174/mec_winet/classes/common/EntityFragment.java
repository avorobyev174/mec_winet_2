package com.avorobyev174.mec_winet.classes.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.house.HouseFragment;
import com.avorobyev174.mec_winet.classes.section.SectionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

public abstract class EntityFragment extends Fragment {
    private Entity entity;

    public interface fragmentShowListner {
        public void onChangeEntity(Entity entity);
    }

    abstract public Entity getParentEntity();

    abstract public EntityFragment getPreviousFragment();

    abstract public void showEntityCreateDialog();

    public void saveEntityInfoDialog() {

    };
}
