package com.avorobyev174.mec_winet.classes.common;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

public abstract class Entity {
    public Entity getParent() {
        return null;
    }
}
