package com.avorobyev174.mec_winet.classes.common;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

public abstract class Entity2 extends AppCompatActivity {
    //private DrawerLayout drawerLayout;
    //private NavigationView navigationView;
    private Activity activity;
    private Class prevActivity;
    private Serializable prevEntity;
    private BottomNavigationView bottomNavigationView;

    public void initNavMenu (Activity activity, Class prevActivityClass, Serializable prevEntity) {
        this.activity = activity;
        this.prevActivity = prevActivityClass;
        this.prevEntity = prevEntity;

        //drawerLayout = activity.findViewById(R.id.drawerLayout);
        //navigationView = activity.findViewById(R.id.navView);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);

//        navMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        //changeNavMenuItems();

        //return drawerLayout;
    }

    public void showObjCreateDialog() {}

    public void saveObjData() {}

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuAddObject: {
//                showObjCreateDialog();
//                break;
//            }
//            case R.id.menuSaveObject: {
//                saveObjData();
//                break;
//            }
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    private void changeNavMenuItems() {
//        Menu menu = navigationView.getMenu();
//        Class<? extends Activity> aClass = activity.getClass();
//        if (HouseActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый дом");
//            menu.findItem(R.id.menuSaveObject).setVisible(false);
//        } else if (SectionActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый подъезд");
//            menu.findItem(R.id.menuSaveObject).setVisible(false);
//        } else if (FloorActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый этаж");
//            menu.findItem(R.id.menuSaveObject).setVisible(false);
//        } else if (VestibuleActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый тамбур");
//            menu.findItem(R.id.menuSaveObject).setVisible(false);
//        } else if (WinetActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый вайнет");
//            menu.findItem(R.id.menuSaveObject).setVisible(false);
//        } else if (WinetDataActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый объект");
//        } else if (ApartmentActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setTitle("Добавить новый счетчик");
//            menu.findItem(R.id.menuSaveObject).setVisible(false);
//        } else if (MeterActivity.class.equals(aClass)) {
//            menu.findItem(R.id.menuAddObject).setVisible(false);
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (prevActivity != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Intent intent = new Intent(activity, prevActivity);
                    if (prevEntity != null) {
                        intent.putExtra(prevEntity.getClass().getSimpleName(), (Serializable) prevEntity);
                    }
                    startActivity(intent);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}