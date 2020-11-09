package com.avorobyev174.mec_winet.classes.meter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataParams;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataResponseWithParams;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeterActivity extends AppCompatActivity {
    private Spinner meterTypeSpinner;
    private EditText serNumberInput, passwordInput;
    private Meter meter;
    private TextView infoBar;
    private ProgressBar progressBar;
    private ImageButton infoBarButton;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_activity);
        init();

        fillMeterInfo();
    }

    @SuppressLint("ResourceType")
    private void init() {
        //meterList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);

        meterTypeSpinner = findViewById(R.id.meterType);
        serNumberInput = findViewById(R.id.meterSerNumber);
        passwordInput = findViewById(R.id.meterPassword);
        infoBarButton = findViewById(R.id.createButtonInfoBar);
        infoBarButton.setImageResource(R.drawable.save_icon_3);
        //createButtonInfoBar = findViewById(R.id.createButtonInfoBar);
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
                        + meter.getApartment().getFullApartmentDesc());

        adapter = ArrayAdapter.createFromResource(this, R.array.meter_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meterTypeSpinner.setAdapter(adapter);

        initOnClick();
    }

    public void initOnClick() {
        infoBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });

        infoBarButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeAddButtonColor(view, motionEvent, getApplicationContext());
            }
        });
    }

    private void fillMeterInfo() {
        Call<MeterInfoResponse> messages = ApiClient.getMeterApi().getMeter("meter", meter.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<MeterInfoResponse>() {
            @Override
            public void onResponse(Call<MeterInfoResponse> call, Response<MeterInfoResponse> response) {
                Log.e("get meter info res", "success = " + response.body().getSuccess());
                MetertInfo meterInfo = response.body().getResult().get(0);
                Log.e("get meter info id", " = " + meterInfo.getId());

                meterTypeSpinner.setSelection(adapter.getPosition(Utils.getMeterTypeTitle(meterInfo.getMeterType())));
                serNumberInput.setText(meterInfo.getSerNumber());
                passwordInput.setText(meterInfo.getPassword());

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Счетчик \"" + meterInfo.getSerNumber() +  "\" загружен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void saveInfo() {
        Call<MeterResponseWithParams> messages = ApiClient.getMeterApi().saveMeter("meter",
                meter.getId(),
                Utils.getMeterType(meterTypeSpinner.getSelectedItem().toString()),
                serNumberInput.getText().toString(),
                passwordInput.getText().toString());

        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<MeterResponseWithParams>() {
            @Override
            public void onResponse(Call<MeterResponseWithParams> call, Response<MeterResponseWithParams> response) {
                Log.e("save winet info res", "success = " + response.body().getSuccess());
                MeterParams meterParams = response.body().getParams();

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Счетчик \"" + meterParams.getSerNumber() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
