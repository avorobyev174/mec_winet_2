package com.avorobyev174.mec_winet.classes.apartment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.meter.Meter;
import com.avorobyev174.mec_winet.classes.meter.MeterActivity;
import com.avorobyev174.mec_winet.classes.meter.MeterAdapter;
import com.avorobyev174.mec_winet.classes.meter.MeterCreateDialog;
import com.avorobyev174.mec_winet.classes.meter.MeterInfoResponse;
import com.avorobyev174.mec_winet.classes.meter.MetertInfo;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApartmentActivity extends Entity {
    private ListView meterListView;
    private MeterAdapter adapter;
    private List<Meter> meterList;
    private TextView infoBar;
    private ProgressBar progressBar;
    private Apartment apartment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartment_activity);
        init();

        fillMeterList();
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        this.apartment = (Apartment) arguments.getSerializable(Apartment.class.getSimpleName());
        initNavMenu(this, WinetDataActivity.class, apartment.getWinet());

        meterList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);
        meterListView = findViewById(R.id.meterListView);
        progressBar = findViewById(R.id.progressBar);

        infoBar.setText(apartment.getWinet().getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + apartment.getWinet().getVestibule().getFloor().getSection().getShortNumber() + " → "
                        + apartment.getWinet().getVestibule().getFloor().getShortNumber() + " → "
                        + apartment.getWinet().getVestibule().getShortNumber() + " → "
                        + apartment.getWinet().getSerNumber() + " → "
                        + apartment.getFullApartmentDesc());

        adapter = new MeterAdapter(this, R.layout.simple_list_item_view, meterList, getLayoutInflater());
        meterListView.setAdapter(adapter);

        meterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meter meter = meterList.get(i);
                Intent intent = new Intent(ApartmentActivity.this, MeterActivity.class);
                intent.putExtra(Meter.class.getSimpleName(), meter);
                startActivity(intent);
            }
        });
    }

    private void fillMeterList() {
        Call<MeterInfoResponse> messages = ApiClient.getMeterApi().getMeters(apartment.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<MeterInfoResponse>() {
            @Override
            public void onResponse(Call<MeterInfoResponse> call, Response<MeterInfoResponse> response) {
                Log.e("get meter response", "success = " + response.body().getSuccess());

                for (MetertInfo metertInfo : response.body().getResult()) {
                    meterList.add(new Meter(metertInfo.getId(), metertInfo.getMeterType(), metertInfo.getSerNumber(), metertInfo.getPassword(), apartment));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " счетчиков", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showObjCreateDialog() {
        MeterCreateDialog meterCreateDialog = new MeterCreateDialog(this, adapter, meterList, apartment);
        meterCreateDialog.show();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    Log.e("back","back");
//                    Intent intent = new Intent(ApartmentActivity.this, WinetDataActivity.class);
//                    intent.putExtra(Winet.class.getSimpleName(), apartment.getWinet());
//                    startActivity(intent);
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
