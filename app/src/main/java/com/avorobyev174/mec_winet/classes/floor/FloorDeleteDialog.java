package com.avorobyev174.mec_winet.classes.floor;

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
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.section.SectionAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloorDeleteDialog extends Dialog {
    public Activity activity;
    public Button deleteFloorButton, cancelDeleteFloorButton;
    public TextView title;
    private Floor floor;
    private FloorAdapter floorAdapter;
    private List<Floor> floorList;

    public FloorDeleteDialog(@NonNull Activity activity, Floor floor, FloorAdapter floorAdapter, List<Floor> floorList) {
        super(activity);
        this.activity = activity;
        this.floor = floor;
        this.floorAdapter = floorAdapter;
        this.floorList = floorList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.floor_delete_dialog_activity);

        deleteFloorButton = findViewById(R.id.deleteFloorDialogButton);
        cancelDeleteFloorButton = findViewById(R.id.cancelDeleteFloorDialogButton);

        title = findViewById(R.id.deleteFloorTitle);
        title.setText("Вы хотите удалить этаж \"" + floor.getNumber() + "\" ?");

        cancelDeleteFloorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        deleteFloorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getFloorApi().deleteFloor("floor", floor.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        floorList.remove(floor);
                        Toast.makeText(getContext(), "Этаж \"" + floor.getNumber() + "\" удален из списка", Toast.LENGTH_SHORT).show();
                        floorAdapter.notifyDataSetChanged();
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
