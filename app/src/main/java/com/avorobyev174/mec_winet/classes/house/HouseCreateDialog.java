package com.avorobyev174.mec_winet.classes.house;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.house.api.HouseParams;
import com.avorobyev174.mec_winet.classes.house.api.HouseResponseWithParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseCreateDialog extends Dialog {
    public Button createHouseButton, cancelCreateHouseButton;
    public EditText street, streetNumber, name, object_x, object_y;
    private HouseAdapter houseAdapter;
    private List<House> houseList;

    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Spinner houseTypeSpinner;

    public HouseCreateDialog(@NonNull Activity activity, HouseAdapter houseAdapter, List<House> houseList) {
        super(activity);
        this.houseAdapter = houseAdapter;
        this.houseList = houseList;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.house_create_dialog_activity);

        createHouseButton = findViewById(R.id.createHouseDialogButton);
        cancelCreateHouseButton = findViewById(R.id.cancelCreateHouseDialogButton);

        street = findViewById(R.id.streetHouseCreateDialog);
        streetNumber = findViewById(R.id.streetNumberHouseCreateDialog);
        name = findViewById(R.id.nameHouseCreateDialog);
        object_x = findViewById(R.id.coordinateXHouseCreateDialog);
        object_y = findViewById(R.id.coordinateYHouseCreateDialog);

        houseTypeSpinner = findViewById(R.id.typeHouseCreateDialog);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.house_type_array, R.xml.spinner_dialog_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseTypeSpinner.setAdapter(spinnerAdapter);

        initOnClick();
    }

    private void initOnClick() {
        cancelCreateHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        houseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FrameLayout streetFrame = findViewById(R.id.streetFrameHouseCreateDialog);
                FrameLayout streetNumberFrame = findViewById(R.id.streetNumberFrameHouseCreateDialog);
                FrameLayout nameObjectFrame = findViewById(R.id.nameFrameHouseCreateDialog);
                FrameLayout coordinateFrame = findViewById(R.id.coordinateFrameHouseCreateDialog);

                if (position == 0) {
                    streetFrame.setVisibility(View.VISIBLE);
                    streetNumberFrame.setVisibility(View.VISIBLE);
                    nameObjectFrame.setVisibility(View.GONE);
                    coordinateFrame.setVisibility(View.GONE);
                } else {
                    streetFrame.setVisibility(View.GONE);
                    streetNumberFrame.setVisibility(View.GONE);
                    nameObjectFrame.setVisibility(View.VISIBLE);
                    coordinateFrame.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createHouseButton.setOnClickListener(new View.OnClickListener() {
            String houseFullName = null;
            Call<HouseResponseWithParams> createHouseCall = null;

            @Override
            public void onClick(View view) {
                if (houseTypeSpinner.getSelectedItemPosition() == 0) {

                    String objStreet = street.getText().toString();
                    String objStreetNum = streetNumber.getText().toString();

                    if (objStreet.isEmpty()) {
                        Toast.makeText(getContext(), "Введите улицу", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (objStreetNum.isEmpty()) {
                        Toast.makeText(getContext(), "Введите номер дома", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    houseFullName = objStreet + " " + objStreetNum;

                    for (House house : houseList) {
                        if (houseFullName.equals(house.getFullStreetName())) {
                            Toast.makeText(getContext(), "Объект \"" + houseFullName + "\" уже существует в списке", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    createHouseCall = ApiClient.getHouseApi().createHouse(objStreet, objStreetNum, null, null, null);
                } else {

                    String objName = name.getText().toString();
                    String objX = object_x.getText().toString();
                    String objY = object_y.getText().toString();

                    if (objName.isEmpty()) {
                        Toast.makeText(getContext(), "Введите название объекта", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (objX.isEmpty() || objY.isEmpty()) {
                        Toast.makeText(getContext(), "Координаты объекта не должны быть пустыми", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    houseFullName = name.getText().toString();

                    for (House house : houseList) {
                        if (houseFullName.equals(house.getName())) {
                            Toast.makeText(getContext(), "Объект \"" + houseFullName + "\" уже существует в списке", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    createHouseCall = ApiClient.getHouseApi().createHouse(null, null, objName, objX, objY);
                }

                //Call<HouseResponseWithParams> createHouseCall = ApiClient.getHouseApi().createHouse(street.getText().toString(), streetNumber.getText().toString());

                createHouseCall.enqueue(new Callback<HouseResponseWithParams>() {
                    @Override
                    public void onResponse(Call<HouseResponseWithParams> call, Response<HouseResponseWithParams> response) {

                        Log.e("response", "success = " + response.body().getSuccess());
                        int houseId = Integer.valueOf(response.body().getResult());
                        HouseParams houseParams = response.body().getParams();

                        if (houseParams.getStreet() != null) {
                            houseList.add(new House(houseId, houseParams.getStreet(), houseParams.getHouseNumber()));
                            Toast.makeText(getContext(), "Дом \"" + houseParams.getStreet() + " " +  houseParams.getHouseNumber() + "\" добавлен в список", Toast.LENGTH_SHORT).show();
                        } else {
                            houseList.add(new House(houseId, houseParams.getName(), houseParams.getHouseX(), houseParams.getHouseY()));
                            Toast.makeText(getContext(), "Объект \"" + houseParams.getName() + "\" добавлен в список", Toast.LENGTH_SHORT).show();
                        }

                        houseAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<HouseResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }
}
