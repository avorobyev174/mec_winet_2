package com.avorobyev174.mec_winet.classes.house;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseDeleteDialog extends Dialog {
    public Activity activity;
    public Button deleteHouseButton, cancelDeleteHouseButton;
    public TextView title;
    private House house;
    private HouseAdapter houseAdapter;
    private List<House> houseList;

    public HouseDeleteDialog(@NonNull Activity activity, House house, HouseAdapter houseAdapter, List<House> houseList) {
        super(activity);
        this.activity = activity;
        this.house = house;
        this.houseAdapter = houseAdapter;
        this.houseList = houseList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.house_delete_dialog_activity);

        deleteHouseButton = findViewById(R.id.deleteHouseDialogButton);
        cancelDeleteHouseButton = findViewById(R.id.cancelDeleteHouseDialogButton);

        title = findViewById(R.id.deleteHouseTitle);
        title.setText("Вы хотите удалить дом \"" + house.getFullStreetName() + "\" ?");

        cancelDeleteHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        deleteHouseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getHouseApi().deleteHouse("house", house.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        houseList.remove(house);
                        Toast.makeText(getContext(), "Дом \"" + house.getFullStreetName() + "\" удален из списка", Toast.LENGTH_SHORT).show();
                        houseAdapter.notifyDataSetChanged();
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
