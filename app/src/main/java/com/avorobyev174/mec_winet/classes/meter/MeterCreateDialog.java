package com.avorobyev174.mec_winet.classes.meter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeterCreateDialog extends Dialog {
    private Activity activity;
    private Button confirmCreateMeterButton, cancelCreateMeterButton;
    private EditText serNumber;
    private EditText password;
    private MeterAdapter meterAdapter;
    private List<Meter> meterList;
    private Apartment apartment;
    private Spinner meterType;
    private ImageButton barCodeButton;

    public MeterCreateDialog(@NonNull Activity activity, MeterAdapter meterAdapter, List<Meter> meterList, Apartment apartment) {
        super(activity);
        this.activity = activity;
        this.meterAdapter = meterAdapter;
        this.meterList = meterList;
        this.apartment = apartment;
    }

    @SuppressLint("ClickableViewAccessibility")
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
        barCodeButton = findViewById(R.id.barCodeButton);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.meter_type_array, android.R.layout.simple_spinner_item);
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

                if (serialNumber.isEmpty()) {
                    Toast.makeText(getContext(), "Введите серийный номер", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                    return;
                }

                String typeStr = meterType.getSelectedItem().toString();
                int type = Utils.getMeterType(typeStr);
                for (Meter meter : meterList) {
                    if (type == meter.getType() && serialNumber.equals(meter.getSerNumber())) {
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
                        Toast.makeText(getContext(), "Счетчик " + "\"" + meterParams.getSerNumber() +  "\" добавлен в список", Toast.LENGTH_SHORT).show();

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

        barCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

        barCodeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeOtherButtonColor(view, motionEvent, activity.getApplicationContext());
            }
        });
    }

    public void scan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.initiateScan();
    }

    public void setSerNumber(String serNumber) {
        this.serNumber.setText(serNumber);
    }
}
