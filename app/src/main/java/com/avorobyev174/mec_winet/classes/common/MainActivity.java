package com.avorobyev174.mec_winet.classes.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentFragment;
import com.avorobyev174.mec_winet.classes.house.HouseFragment;
import com.avorobyev174.mec_winet.classes.meter.MeterCreateDialog;
import com.avorobyev174.mec_winet.classes.meter.MeterFragment;
import com.avorobyev174.mec_winet.classes.winet.WinetCreateDialog;
import com.avorobyev174.mec_winet.classes.winet.WinetFragment;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements EntityFragment.fragmentShowListner {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
    }

    @Override
    public void onChangeEntity(Entity entity) {
        InfoBar.changeInfoBarData((Serializable) entity);
    }

    private void init() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        fragmentManager = getSupportFragmentManager();
        InfoBar.init(this, progressBar, fragmentManager);

        fragmentManager.beginTransaction().replace(R.id.fragment_container, HouseFragment.newInstance(progressBar, fragmentManager)).commitNow();

        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuAddObject :
                        getCurrentFragment().showEntityCreateDialog();
                        break;
                    case R.id.menuSaveObject: {
                        getCurrentFragment().saveEntityInfoDialog();
                        break;
                    }
                    case R.id.menuBack: {
                        EntityFragment fragment = getCurrentFragment().getPreviousFragment();
                        if (fragment == null)
                            return false;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("entity", (Serializable) getCurrentFragment().getParentEntity().getParent());
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commitNow();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private EntityFragment getCurrentFragment() {
        return (EntityFragment) fragmentManager.findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null && intentResult.getContents() != null) {
            if (getCurrentFragment() instanceof WinetFragment) {
                WinetCreateDialog winetCreateDialog = ((WinetFragment)getCurrentFragment()).getCreateDialog();
                if (winetCreateDialog != null) {
                    winetCreateDialog.setSerNumber(intentResult.getContents());
                }
            } else if (getCurrentFragment() instanceof WinetDataFragment) {
                String str = intentResult.getContents();
                String type = "";
                String serNumber = str;
                if (str.contains("/")) {
                    int index = str.indexOf("/");
                    //Log.e("scan", String.valueOf(index));
                    //Log.e("scan text", serNumber);
                    serNumber = str.substring(index + 1);
                    Log.e("scan serNumber", serNumber);
                    type = str.substring(index - 3, index);
                    Log.e("scan type", type);
                    ((WinetDataFragment) getCurrentFragment()).setType(type);
                    //Toast.makeText(getCurrentFragment().getContext(), "Серийный номер успешно считан", Toast.LENGTH_SHORT);
                }

                ((WinetDataFragment) getCurrentFragment()).setSerNumber(serNumber);
            } else if (getCurrentFragment() instanceof ApartmentFragment) {
                MeterCreateDialog meterCreateDialog = ((ApartmentFragment)getCurrentFragment()).getCreateDialog();
                if (meterCreateDialog != null) {
                    meterCreateDialog.setSerNumber(intentResult.getContents());
                }
            }
        } else  {
            Toast.makeText(getApplicationContext(), "Ошибка при попытке сканирования штрих-кода", Toast.LENGTH_SHORT);
            //((MeterFragment) getCurrentFragment()).setSerNumber(intentResult.getContents());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}