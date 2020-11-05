package com.avorobyev174.mec_winet.classes.apartment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.house.House;
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
        setContentView(R.layout.apartment_create_dialog_activity);

        confirmCreateApartmentButton = findViewById(R.id.confirmCreateApartmentButton);
        cancelCreateApartmentButton = findViewById(R.id.cancelCreateApartmentButton);
        apartmentType = findViewById(R.id.apartmentTypeCreateDialog);
        description = findViewById(R.id.apartmentDescrCreateDialog);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.apartment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        apartmentType.setAdapter(adapter);

        apartmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    description.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                    description.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        cancelCreateApartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmCreateApartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apartDesc =  description.getText().toString();
                String apartType = (apartmentType.getSelectedItem().toString()).equals(getContext().getResources().getString(R.string.apartment_type_phizical)) ? "1" : "2";
                for (Apartment apartment : apartmentList) {
                    if (apartType.equals(apartment.getApartmentType()) && apartDesc.equals(apartment.getApartmentDesc())) {
                        if (apartType.equals("1")) {
                            Toast.makeText(getContext(), apartType + "\"" + apartDesc +  "\" уже привязана к этой вайнет", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), apartType + "\"" + apartDesc +  "\" уже привязано к этой вайнет", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }

                Call<ApartmentResponseWithParams> createApartmentCall = ApiClient.getApartmentApi().createApartment(apartType, apartDesc, "", winet.getId());

                createApartmentCall.enqueue(new Callback<ApartmentResponseWithParams>() {
                    @Override
                    public void onResponse(Call<ApartmentResponseWithParams> call, Response<ApartmentResponseWithParams> response) {

                        Log.e("create apart response", "success = " + response.body().getSuccess());
                        int apartId = Integer.parseInt(response.body().getResult());
                        ApartmentParams apartmentParams = response.body().getParams();
                        Log.e("create apart response", "params apart number = " + apartmentParams.getApartmentDesc());
                        Log.e("create apart sql", response.body().getSql());
                        apartmentList.add(new Apartment(apartId, apartmentParams.getApartmentType(), apartmentParams.getApartmentDesc(), "", winet));
                        Toast.makeText(getContext(), Utils.getApartmentTypeTitle(getContext(), apartmentParams.getApartmentType()) +
                                                                            " \"" + apartmentParams.getApartmentDesc() +  "\" добавлено в список", Toast.LENGTH_SHORT).show();

                        apartmentAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<ApartmentResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}
