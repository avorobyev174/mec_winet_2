package com.avorobyev174.mec_winet.classes.apartment;

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
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetAdapter;
import com.avorobyev174.mec_winet.classes.winet.WinetParams;
import com.avorobyev174.mec_winet.classes.winet.WinetResponseWithParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentCreateDialog extends Dialog {
    public Activity activity;
    public Button confirmCreateApartmentButton, cancelCreateApartmentButton;
    public EditText description;
    private ApartmentAdapter apartmentAdapter;
    private List<Apartment> apartmentList;
    private Winet winet;
    private Spinner apartmentType;

    public ApartmentCreateDialog(@NonNull Activity activity, ApartmentAdapter apartmentAdapter, List<Apartment> apartmentList, Winet winet) {
        super(activity);
        this.activity = activity;
        this.apartmentAdapter = apartmentAdapter;
        this.apartmentList = apartmentList;
        this.winet = winet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.winet_create_dialog_activity);

        confirmCreateApartmentButton = findViewById(R.id.confirmCreateWinetButton);
        cancelCreateApartmentButton = findViewById(R.id.cancelCreateWinetButton);
        apartmentType = findViewById(R.id.winetTypeCreateDialog);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.winet_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        apartmentType.setAdapter(adapter);
        description = findViewById(R.id.winetSerNumberCreateDialog);

        cancelCreateApartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmCreateApartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String winetSerNumber =  description.getText().toString();
                String winetType = apartmentType.getSelectedItem().toString();
                for (Apartment apartment : apartmentList) {
//                    if (winetSerNumber.equals(apartment.getSerNumber()) && winetType.equals(apartment.getType())) {
//                        Toast.makeText(getContext(), "Вайнет \"" + winetSerNumber +  "\" с типом \"" + winetType +  "\" уже существует в этом тамбуре", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                }

                Call<WinetResponseWithParams> createFloorCall = ApiClient.getWinetApi().createWinet(winetType, winetSerNumber, winet.getId());

                createFloorCall.enqueue(new Callback<WinetResponseWithParams>() {
                    @Override
                    public void onResponse(Call<WinetResponseWithParams> call, Response<WinetResponseWithParams> response) {

                        Log.e("create winet response", "success = " + response.body().getSuccess());
                        int winetId = Integer.parseInt(response.body().getResult());
                        WinetParams winetParams = response.body().getParams();
                        Log.e("create winet response", "params winet number = " + winetParams.getSerNumber());
                        apartmentList.add(new Winet(winetId, winetParams.getType(), winetParams.getSerNumber(), winet));

                        Toast.makeText(getContext(), "Вайнет \"" + winetParams.getSerNumber()+  "\" с типом \"" + winetParams.getType() +  "\" добавлен в список", Toast.LENGTH_SHORT).show();

                        apartmentAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<WinetResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}
