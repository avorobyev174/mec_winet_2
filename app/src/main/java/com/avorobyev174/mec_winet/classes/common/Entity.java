package com.avorobyev174.mec_winet.classes.common;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentActivity;
import com.avorobyev174.mec_winet.classes.floor.FloorActivity;
import com.avorobyev174.mec_winet.classes.house.HouseActivity;
import com.avorobyev174.mec_winet.classes.meter.MeterActivity;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleActivity;
import com.avorobyev174.mec_winet.classes.winet.WinetActivity;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;

public abstract class Entity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Activity activity;
    private Class prevActivity;
    private Object prevObject;
    private ImageButton navMenuButton;

    public DrawerLayout initNavMenu (Activity activity, Class prevActivity, Object prevObject) {
        this.activity = activity;
        this.prevActivity = prevActivity;
        this.prevObject = prevObject;

        navMenuButton = activity.findViewById(R.id.navMenuButton);
        drawerLayout = activity.findViewById(R.id.drawerLayout);
        navigationView = activity.findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);

        navMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        changeNavMenuItems();

        return drawerLayout;
    }

    public void showObjCreateDialog() {}

    public void saveObjData() {}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddObject: {
                showObjCreateDialog();
                break;
            }
            case R.id.menuSaveObject: {
                saveObjData();
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeNavMenuItems() {
        Menu menu = navigationView.getMenu();
        Class<? extends Activity> aClass = activity.getClass();
        if (HouseActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый дом");
            menu.findItem(R.id.menuSaveObject).setVisible(false);
        } else if (SectionActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый подъезд");
            menu.findItem(R.id.menuSaveObject).setVisible(false);
        } else if (FloorActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый этаж");
            menu.findItem(R.id.menuSaveObject).setVisible(false);
        } else if (VestibuleActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый тамбур");
            menu.findItem(R.id.menuSaveObject).setVisible(false);
        } else if (WinetActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый вайнет");
            menu.findItem(R.id.menuSaveObject).setVisible(false);
        } else if (WinetDataActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый объект");
        } else if (ApartmentActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый счетчик");
            menu.findItem(R.id.menuSaveObject).setVisible(false);
        } else if (MeterActivity.class.equals(aClass)) {
            menu.findItem(R.id.menuAddObject).setVisible(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (prevActivity != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Intent intent = new Intent(activity, prevActivity);
                    if (prevObject != null) {
                        intent.putExtra(prevObject.getClass().getSimpleName(), (Serializable) prevObject);
                    }
                    startActivity(intent);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
