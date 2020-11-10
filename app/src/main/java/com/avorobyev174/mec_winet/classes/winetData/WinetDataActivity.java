package com.avorobyev174.mec_winet.classes.winetData;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentActivity;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentCreateDialog;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetActivity;
import com.avorobyev174.mec_winet.classes.winet.WinetInfo;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentAdapter;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfo;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfoResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WinetDataActivity extends Entity {
    private ApartmentAdapter apartmentAdapter;
    private List<Apartment> apartmentList;
    private TextView infoBar;
    private ProgressBar progressBar;
    private Spinner winetTypeSpinner;
    private EditText serNumberInput, commentInput;
    private ArrayAdapter<CharSequence> adapter;
    private ImageButton infoBarButton, barCodeButton;
    private ListView apartmentListView;
    private Winet winet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winet_data_activity);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
        init();

        fillInfo();
    }

    @SuppressLint("ResourceType")
    private void init() {
        Bundle arguments = getIntent().getExtras();
        winet = (Winet) arguments.getSerializable(Winet.class.getSimpleName());
        initNavMenu(this, WinetActivity.class, winet.getVestibule());

        apartmentList = new ArrayList<>();
        apartmentListView = findViewById(R.id.apartmentList);
        infoBar = findViewById(R.id.info_bar);
        progressBar  = findViewById(R.id.progressBar);
        winetTypeSpinner = findViewById(R.id.winetType);
        serNumberInput = findViewById(R.id.winetSerNumber);
        commentInput = findViewById(R.id.winetComment);
        barCodeButton = findViewById(R.id.barCodeButton);

        infoBar.setText(winet.getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + winet.getVestibule().getFloor().getSection().getShortNumber() + " → "
                        + winet.getVestibule().getFloor().getShortNumber() + " → "
                        + winet.getVestibule().getShortNumber());

        adapter = ArrayAdapter.createFromResource(this, R.array.winet_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winetTypeSpinner.setAdapter(adapter);

        apartmentAdapter = new ApartmentAdapter(this, R.layout.simple_list_item_view, apartmentList, getLayoutInflater());
        apartmentListView.setAdapter(apartmentAdapter);

        initOnClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initOnClick() {
        barCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

        barCodeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeOtherButtonColor(view, motionEvent, getApplicationContext());
            }
        });

        apartmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Apartment apartment = apartmentList.get(i);
                Intent intent = new Intent(WinetDataActivity.this, ApartmentActivity.class);
                intent.putExtra(Apartment.class.getSimpleName(), apartment);
                startActivity(intent);
            }
        });
    }

    @Override
    public void saveObjData() {
        saveWinetData();
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    Log.e("back","back");
//                    Intent intent = new Intent(WinetDataActivity.this, WinetActivity.class);
//                    intent.putExtra(Vestibule.class.getSimpleName(), winet.getVestibule());
//                    startActivity(intent);
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private void fillInfo() {
        Call<WinetDataInfoResponse> messages = ApiClient.getWinetApi().getWinet("winet", winet.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetDataInfoResponse>() {
            @Override
            public void onResponse(Call<WinetDataInfoResponse> call, Response<WinetDataInfoResponse> response) {
                Log.e("get winet info res", "success = " + response.body().getSuccess());
                WinetInfo winetDataInfo = response.body().getResult().get(0);
                Log.e("get winet info id", " = " + winetDataInfo.getId());
                Log.e("get winet info comment", " = " + winetDataInfo.getComment());

                winetTypeSpinner.setSelection(adapter.getPosition(winetDataInfo.getType()));
                serNumberInput.setText(winetDataInfo.getSerNumber());
                commentInput.setText(winetDataInfo.getComment());

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
                fillApartmentInfo();


                Toast.makeText(getApplicationContext(), "Вайнет \"" + winetDataInfo.getSerNumber()+  "\" с типом \"" + winetDataInfo.getType() +  "\" загружен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WinetDataInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillApartmentInfo() {
        Call<ApartmentInfoResponse> messages = ApiClient.getApartmentApi().getApartments(winet.getId());
        //progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<ApartmentInfoResponse>() {
            @Override
            public void onResponse(Call<ApartmentInfoResponse> call, Response<ApartmentInfoResponse> response) {
                for (ApartmentInfo apartmentInfo : response.body().getResult()) {
                    Apartment apartment = new Apartment(apartmentInfo.getId(),
                                                        apartmentInfo.getApartmentType(),
                                                        apartmentInfo.getApartmentDescription(),
                                                        apartmentInfo.getApartmentComment(),
                                                        winet);
                    apartmentList.add(apartment);
                }

                Log.e("get apart info res", "success = " + response.body().getSuccess());

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                apartmentAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Список квартир загружен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApartmentInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveWinetData() {
        Call<WinetDataResponseWithParams> messages = ApiClient.getWinetApi().saveWinet("winet",
                                                                                              winet.getId(),
                                                                                              winetTypeSpinner.getSelectedItem().toString(),
                                                                                              serNumberInput.getText().toString(),
                                                                                              commentInput.getText().toString());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetDataResponseWithParams>() {
            @Override
            public void onResponse(Call<WinetDataResponseWithParams> call, Response<WinetDataResponseWithParams> response) {
                Log.e("save winet info res", "success = " + response.body().getSuccess());
                WinetDataParams winetDataParams = response.body().getParams();

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
                //saveApartmentInfo();
                Toast.makeText(getApplicationContext(), "Вайнет \"" + winetDataParams.getSerNumber()+  "\" с типом \"" + winetDataParams.getType() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WinetDataResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showObjCreateDialog() {
        ApartmentCreateDialog apartmentCreateDialog = new ApartmentCreateDialog(this, apartmentAdapter,  apartmentList, winet);
        apartmentCreateDialog.show();
    }

    public void scan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                serNumberInput.setText(intentResult.getContents());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
