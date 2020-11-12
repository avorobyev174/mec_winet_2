package com.avorobyev174.mec_winet.classes.meter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.InfoBar;
import com.avorobyev174.mec_winet.classes.common.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeterActivity extends Entity {
    private ProgressBar progressBar;
    private Spinner meterTypeSpinner;
    private EditText serNumberInput, passwordInput;
    private ArrayAdapter<CharSequence> adapter;
    private Meter meter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_activity);
        init();

        fillMeterInfo();
    }

    @SuppressLint("ResourceType")
    private void init() {
        Bundle arguments = getIntent().getExtras();
        meter = (Meter) arguments.getSerializable(Meter.class.getSimpleName());
        initNavMenu(this, ApartmentActivity.class, meter.getApartment());
        progressBar = findViewById(R.id.progressBar);
        meterTypeSpinner = findViewById(R.id.meterType);
        serNumberInput = findViewById(R.id.meterSerNumber);
        passwordInput = findViewById(R.id.meterPassword);

        InfoBar.init(this);
        InfoBar.changeInfoBarData(meter);

        adapter = ArrayAdapter.createFromResource(this, R.array.meter_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meterTypeSpinner.setAdapter(adapter);
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

                //Toast.makeText(getApplicationContext(), "Счетчик \"" + meterInfo.getSerNumber() +  "\" загружен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void saveObjData() {
        saveMeterData();
    }

    private void saveMeterData() {
        String serialNumber =  serNumberInput.getText().toString();
        String pass =  passwordInput.getText().toString();

        if (serialNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Cерийный номер не должен быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Пароль не должен быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<MeterResponseWithParams> messages = ApiClient.getMeterApi().saveMeter("meter", meter.getId(),
                                                                                Utils.getMeterType(meterTypeSpinner.getSelectedItem().toString()), serialNumber, pass);

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
