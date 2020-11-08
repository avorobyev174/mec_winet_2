package com.avorobyev174.mec_winet.classes.meter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentAdapter;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentParams;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentResponseWithParams;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winet.Winet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeterCreateDialog extends Dialog {
    public Activity activity;
    public Button confirmCreateMeterButton, cancelCreateMeterButton;
    public EditText serNumber;
    public EditText password;
    private MeterAdapter meterAdapter;
    private List<Meter> meterList;
    private Apartment apartment;
    private Spinner meterType;

    public MeterCreateDialog(@NonNull Activity activity, MeterAdapter meterAdapter, List<Meter> meterList, Apartment apartment) {
        super(activity);
        this.activity = activity;
        this.meterAdapter = meterAdapter;
        this.meterList = meterList;
        this.apartment = apartment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.meter_create_dialog_activity);

        confirmCreateMeterButton = findViewById(R.id.confirmCreateMeterButton);
        cancelCreateMeterButton = findViewById(R.id.cancelCreateMeterButton);
        meterType = findViewById(R.id.meterTypeCreateDialog);
        serNumber = findViewById(R.id.meterSerNumberCreateDialog);
        password = findViewById(R.id.meterPasswordCreateDialog);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.apartment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        meterType.setAdapter(adapter);

        cancelCreateMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmCreateMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serialNumber =  serNumber.getText().toString();
                String pass =  password.getText().toString();
                String type = meterType.getSelectedItem().toString();
                for (Meter meter : meterList) {
                    if (type.equals(meter.getType()) && serialNumber.equals(meter.getSerNumber())) {
                        Toast.makeText(getContext(), "Счетчик" + "\"" + serialNumber +  "\" уже привязан к этой квартире", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Call<MeterResponseWithParams> createMeterCall = ApiClient.getMeterApi().createMeter(type, serialNumber, pass, apartment.getId());

                createMeterCall.enqueue(new Callback<MeterResponseWithParams>() {
                    @Override
                    public void onResponse(Call<MeterResponseWithParams> call, Response<MeterResponseWithParams> response) {

                        Log.e("create meter response", "success = " + response.body().getSuccess());
                        int meterId = Integer.parseInt(response.body().getResult());
                        MeterParams meterParams = response.body().getParams();
                        Log.e("create meter response", "params meter number = " + meterParams.getSerNumber());
                        Log.e("create meter sql", response.body().getSql());
                        meterList.add(new Meter(meterId, meterParams.getMeterType(), meterParams.getSerNumber(), meterParams.getPassword(),apartment));
                        Toast.makeText(getContext(), meterParams.getSerNumber() +  "\" добавлен в список", Toast.LENGTH_SHORT).show();

                        meterAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<MeterResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}