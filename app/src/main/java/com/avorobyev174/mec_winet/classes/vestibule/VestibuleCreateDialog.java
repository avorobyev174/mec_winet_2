package com.avorobyev174.mec_winet.classes.vestibule;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.floor.Floor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VestibuleCreateDialog extends Dialog {
    public Activity activity;
    public Button confirmCreateFloorButton, cancelCreateFloorButton;
    public EditText vestibuleNumber;
    private VestibuleAdapter vestibuleAdapter;
    private List<Vestibule> vestibuleList;
    private Floor floor;

    public VestibuleCreateDialog(@NonNull Activity activity, VestibuleAdapter vestibuleAdapter, List<Vestibule> vestibuleList, Floor floor) {
        super(activity);
        this.activity = activity;
        this.vestibuleAdapter = vestibuleAdapter;
        this.vestibuleList = vestibuleList;
        this.floor = floor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_dialog_activity);

        confirmCreateFloorButton = findViewById(R.id.confirmCreateButton);
        cancelCreateFloorButton = findViewById(R.id.cancelCreateButton);

        vestibuleNumber = findViewById(R.id.objectNumber);
        vestibuleNumber.setHint("Номер тамбура");

        cancelCreateFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmCreateFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vestNumber = vestibuleNumber.getText().toString();
                for (Vestibule vestibule : vestibuleList) {
                    if (Integer.parseInt(vestNumber) == vestibule.getNumber()) {
                        Toast.makeText(getContext(), "Тамбур \"" + vestNumber + "\" уже существует на этом этаже", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Call<VestibuleResponseWithParams> createFloorCall = ApiClient.getVestibuleApi().createVestibule(vestNumber, floor.getId());

                createFloorCall.enqueue(new Callback<VestibuleResponseWithParams>() {
                    @Override
                    public void onResponse(Call<VestibuleResponseWithParams> call, Response<VestibuleResponseWithParams> response) {

                        Log.e("create vest response", "success = " + response.body().getSuccess());
                        int vestibuleId = Integer.parseInt(response.body().getResult());
                        VestibuleParams vestibuleParams = response.body().getParams();
                        Log.e("create vest response", "params vest number = " + vestibuleParams.getVestibuleNumber());
                        vestibuleList.add(new Vestibule(vestibuleId, Integer.parseInt(vestibuleParams.getVestibuleNumber()), floor));

                        Toast.makeText(getContext(), "Тамбур \"" + vestibuleParams.getVestibuleNumber() + "\" добавлен в список", Toast.LENGTH_SHORT).show();

                        vestibuleAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<VestibuleResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}
