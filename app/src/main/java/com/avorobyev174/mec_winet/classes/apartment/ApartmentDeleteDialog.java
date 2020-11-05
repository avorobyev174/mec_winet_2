package com.avorobyev174.mec_winet.classes.apartment;

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
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentDeleteDialog extends Dialog {
    public Activity activity;
    public Button confirmDeleteApartmentButton, cancelDeleteApartmentButton;
    public TextView apartmentDeleteTitle;
    private Apartment apartment;
    private ApartmentAdapter apartmentAdapter;
    private List<Apartment> apartmentList;

    public ApartmentDeleteDialog(@NonNull Activity activity, Apartment apartment, ApartmentAdapter apartmentAdapter, List<Apartment> apartmentList) {
        super(activity);
        this.activity = activity;
        this.apartment = apartment;
        this.apartmentAdapter = apartmentAdapter;
        this.apartmentList = apartmentList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog_activity);

        confirmDeleteApartmentButton = findViewById(R.id.confirmDeleteDialogButton);
        cancelDeleteApartmentButton = findViewById(R.id.cancelDeleteDialogButton);

        apartmentDeleteTitle = findViewById(R.id.deleteDialogTitle);

        apartmentDeleteTitle.setText("Вы хотите удалить " + Utils.getApartmentTypeTitle(getContext(), apartment.getApartmentType()) + " \"" + apartment.getFullApartmentDesc() + "\" ?");

        cancelDeleteApartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmDeleteApartmentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getWinetApi().deleteWinet("apartment", apartment.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        apartmentList.remove(apartment);
                        Toast.makeText(getContext(), Utils.getApartmentTypeTitle(getContext(), apartment.getApartmentType()) + " \"" + apartment.getApartmentDesc() + "\" удалено из списка", Toast.LENGTH_SHORT).show();
                        apartmentAdapter.notifyDataSetChanged();
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
