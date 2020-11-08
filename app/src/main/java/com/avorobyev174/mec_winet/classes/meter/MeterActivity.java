package com.avorobyev174.mec_winet.classes.meter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentActivity;
import com.avorobyev174.mec_winet.classes.common.Utils;

import java.util.ArrayList;
import java.util.List;


public class MeterActivity extends AppCompatActivity {
    private List<Meter> meterList;
    //private MeterAdapter adapter;
    private Meter meter;
    private TextView infoBar;
    private ProgressBar progressBar;
    private ImageButton saveMeterButton, createButtonInfoBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartment_activity);
        init();

        fillMeterInfo();
    }

    private void init() {
        meterList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);

        //saveMeterButton = findViewById(R.id.addMeterButton);
        createButtonInfoBar = findViewById(R.id.createButtonInfoBar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        //createButtonInfoBar.setVisibility(View.INVISIBLE);

        Bundle arguments = getIntent().getExtras();
        this.meter = (Meter) arguments.getSerializable(Meter.class.getSimpleName());
        infoBar.setText(meter.getApartment().getWinet().getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + meter.getApartment().getWinet().getVestibule().getFloor().getSection().getShortNumber() + " → "
                        + meter.getApartment().getWinet().getVestibule().getFloor().getShortNumber() + " → "
                        + meter.getApartment().getWinet().getVestibule().getShortNumber() + " → "
                        + meter.getApartment().getWinet().getSerNumber() + " → "
                        + meter.getSerNumber());

        //adapter = new MeterAdapter(this, R.layout.simple_list_item_view, meterList, getLayoutInflater());

        initOnClick();
    }

    public void initOnClick() {
        saveMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createNewMeter(view);
            }
        });

        saveMeterButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeAddButtonColor(view, motionEvent, getApplicationContext());
            }
        });
    }

    private void fillMeterInfo() {
//        Call<WinetInfoResponse> messages = ApiClient.getWinetApi().getWinets(winet.getId());
//        progressBar.setVisibility(ProgressBar.VISIBLE);
//
//        messages.enqueue(new Callback<WinetInfoResponse>() {
//            @Override
//            public void onResponse(Call<WinetInfoResponse> call, Response<WinetInfoResponse> response) {
//                Log.e("get winet response", "success = " + response.body().getSuccess());
//
//                for (WinetInfo winetInfo : response.body().getResult()) {
//                    apartmentList.add(new Winet(winetInfo.getId(), winetInfo.getType(), winetInfo.getSerNumber(), winet));
//                }
//
//                progressBar.setVisibility(ProgressBar.INVISIBLE);
//                adapter.notifyDataSetChanged();
//
//                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " вайнет", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<WinetInfoResponse> call, Throwable t) {
//                Log.e("response", "failure " + t);
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Log.e("back","back");
                    Intent intent = new Intent(MeterActivity.this, ApartmentActivity.class);
                    intent.putExtra(Apartment.class.getSimpleName(), meter.getApartment());
                    startActivity(intent);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
