package com.avorobyev174.mec_winet.classes.floor;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.floor.api.FloorParams;
import com.avorobyev174.mec_winet.classes.floor.api.FloorResponseWithParams;
import com.avorobyev174.mec_winet.classes.section.Section;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloorCreateDialog extends Dialog {
    private Activity activity;
    private Button createFloorButton, cancelCreateFloorButton;
    private EditText floorNumber;
    private TextView floorLabel;
    private FloorAdapter floorAdapter;
    private List<Floor> floorList;
    private Section section;

    public FloorCreateDialog(@NonNull Activity activity, FloorAdapter floorAdapter, List<Floor> floorList, Section section) {
        super(activity);
        this.activity = activity;
        this.floorAdapter = floorAdapter;
        this.floorList = floorList;
        this.section = section;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_dialog_activity);

        createFloorButton = findViewById(R.id.confirmCreateButton);
        cancelCreateFloorButton = findViewById(R.id.cancelCreateButton);

        floorNumber = findViewById(R.id.objectNumber);
        floorLabel = findViewById(R.id.objectNumberLabel);
        floorLabel.setText("Номер этажа");

        cancelCreateFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        createFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floorNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Введите номер этажа", Toast.LENGTH_SHORT).show();
                    return;
                }

                String floNumber = floorNumber.getText().toString();
                for (Floor floor : floorList) {
                    if (Integer.parseInt(floNumber) == floor.getNumber()) {
                        Toast.makeText(getContext(), "Этаж \"" + floNumber + "\" уже существует в этом подьезде", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Call<FloorResponseWithParams> createFloorCall = ApiClient.getFloorApi().createFloor(floNumber, section.getId());

                createFloorCall.enqueue(new Callback<FloorResponseWithParams>() {
                    @Override
                    public void onResponse(Call<FloorResponseWithParams> call, Response<FloorResponseWithParams> response) {

                        Log.e("create floor response", "success = " + response.body().getSuccess());
                        int floorId = Integer.parseInt(response.body().getResult());
                        FloorParams floorParams = response.body().getParams();
                        Log.e("create floor response", "params floor number = " + floorParams.getFloorNumber());
                        floorList.add(new Floor(floorId, Integer.parseInt(floorParams.getFloorNumber()), section));

                        Toast.makeText(getContext(), "Этаж \"" + floorParams.getFloorNumber() + "\" добавлен в список", Toast.LENGTH_SHORT).show();

                        floorAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<FloorResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}
