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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataActivity;

import java.util.ArrayList;
import java.util.List;


public class ApartmentActivity extends AppCompatActivity {
    private List<Apartment> apartmentList;
    private ApartmentAdapter adapter;
    private ListView meterListView;
    private Apartment apartment;
    private TextView infoBar;
    private ProgressBar progressBar;
    private ImageButton createMeterButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartment_activity);
        init();

        fillMeterList();
    }

    private void init() {
        apartmentList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);
        meterListView = findViewById(R.id.meterListView);
        createMeterButton = findViewById(R.id.addMeterButton);
        progressBar = findViewById(R.id.progressBar);
        Bundle arguments = getIntent().getExtras();
        this.apartment = (Apartment) arguments.getSerializable(Apartment.class.getSimpleName());
        infoBar.setText(apartment.getWinet().getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + apartment.getWinet().getVestibule().getFloor().getSection().getShortNumber() + " → "
                        + apartment.getWinet().getVestibule().getFloor().getShortNumber() + " → "
                        + apartment.getWinet().getVestibule().getShortNumber() + " → "
                        + apartment.getWinet().getSerNumber());

        adapter = new ApartmentAdapter(this, R.layout.simple_list_item_view, apartmentList, getLayoutInflater());
        meterListView.setAdapter(adapter);

        initOnClick();
    }

    public void initOnClick() {
        meterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TO DO METER
                //Winet winet = apartmentList.get(i);
                Intent intent = new Intent(ApartmentActivity.this, WinetDataActivity.class);
                intent.putExtra(Winet.class.getSimpleName(), apartment);
                startActivity(intent);
            }
        });

        createMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMeter(view);
            }
        });

        createMeterButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeAddButtonColor(view, motionEvent, getApplicationContext());
            }
        });
    }

    private void fillMeterList() {
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

    public void createNewMeter(View view) {
//        WinetCreateDialog winetCreateDialog = new WinetCreateDialog(this, adapter, apartmentList, winet);
//        winetCreateDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Log.e("back","back");
                    Intent intent = new Intent(ApartmentActivity.this, WinetDataActivity.class);
                    intent.putExtra(Floor.class.getSimpleName(), apartment);
                    startActivity(intent);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
