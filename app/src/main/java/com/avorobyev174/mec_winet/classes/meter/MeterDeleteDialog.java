package com.avorobyev174.mec_winet.classes.meter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentAdapter;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.common.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeterDeleteDialog extends Dialog {
    public Activity activity;
    public Button confirmDeleteMeterButton, cancelDeleteMeterButton;
    public TextView meterDeleteTitle;
    private Meter meter;
    private MeterAdapter meterAdapter;
    private List<Meter> meterList;

    public MeterDeleteDialog(@NonNull Activity activity, Meter meter, MeterAdapter meterAdapter, List<Meter> meterList) {
        super(activity);
        this.activity = activity;
        this.meter = meter;
        this.meterAdapter = meterAdapter;
        this.meterList = meterList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog_activity);

        confirmDeleteMeterButton = findViewById(R.id.confirmDeleteDialogButton);
        cancelDeleteMeterButton = findViewById(R.id.cancelDeleteDialogButton);

        meterDeleteTitle = findViewById(R.id.deleteDialogTitle);

        meterDeleteTitle.setText("Вы хотите удалить " + meter.getSerNumber() + " ?");

        cancelDeleteMeterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmDeleteMeterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getMeterApi().deleteMeter("meter", meter.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        meterList.remove(meter);
                        Toast.makeText(getContext(), "Счетчик " + meter.getSerNumber() + "\" удален из списка", Toast.LENGTH_SHORT).show();
                        meterAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Log.e("response", "failure " + t);
                        dismiss();
                    }
                });
            }
        });
    }

}
