package com.avorobyev174.mec_winet.classes.winetData;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetActivity;
import com.avorobyev174.mec_winet.classes.winet.WinetInfo;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentAdapter;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfo;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfoResponse;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentResponseWithParams;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WinetDataActivity extends AppCompatActivity {
    private TextView infoBar;
    private Spinner winetTypeSpinner;
    private EditText serNumberInput, commentInput, winetApartmentNumber;
    private Winet winet;
    private ArrayAdapter<CharSequence> adapter;
    private ImageButton infoBarButton, barCodeButton, addApartmentButton;
    private Button saveWinetInfoButton;
    private ProgressBar progressBar;
    private ListView apartmentListView;
    private List<Apartment> apartmentList;
    private List<Apartment> removeApartmentList;
    private List<Apartment> addApartmentList;
    private ApartmentAdapter apartmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winet_info_activity);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
        init();

        fillInfo();
    }

    @SuppressLint("ResourceType")
    private void init() {
        apartmentList = new ArrayList<>();
        removeApartmentList = new ArrayList<>();
        addApartmentList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);
        winetTypeSpinner = findViewById(R.id.winetType);
        serNumberInput = findViewById(R.id.winetSerNumber);
        winetApartmentNumber = findViewById(R.id.winetApartmentNumber);
        commentInput = findViewById(R.id.winetComment);
        progressBar  = findViewById(R.id.progressBar);
        apartmentListView = findViewById(R.id.apartmentList);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        saveWinetInfoButton = findViewById(R.id.saveWinetInfo);
        barCodeButton = findViewById(R.id.barCodeButton);
        addApartmentButton = findViewById(R.id.addApartmentButton);
        infoBarButton = findViewById(R.id.createButtonInfoBar);
        infoBarButton.setVisibility(View.GONE);

        Bundle arguments = getIntent().getExtras();
        winet = (Winet) arguments.getSerializable(Winet.class.getSimpleName());

        infoBar.setText(winet.getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + winet.getVestibule().getFloor().getSection().getShortNumber() + " → "
                        + winet.getVestibule().getFloor().getShortNumber() + " → "
                        + winet.getVestibule().getShortNumber());

        adapter = ArrayAdapter.createFromResource(this, R.array.winet_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winetTypeSpinner.setAdapter(adapter);

        apartmentAdapter = new ApartmentAdapter(this, R.layout.simple_list_item_view, apartmentList, getLayoutInflater(), removeApartmentList, addApartmentList);
        apartmentListView.setAdapter(apartmentAdapter);

        initOnClick();
    }

    public void initOnClick() {
        saveWinetInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });

        barCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

        addApartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addApartmentToList();
            }
        });

        addApartmentButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeAddButtonColor(view, motionEvent, getApplicationContext());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Log.e("back","back");
                    Intent intent = new Intent(WinetDataActivity.this, WinetActivity.class);
                    intent.putExtra(Vestibule.class.getSimpleName(), winet.getVestibule());
                    startActivity(intent);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

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

                //progressBar.setVisibility(ProgressBar.INVISIBLE);
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

    private void saveInfo() {
        Call<WinetDataResponseWithParams> messages = ApiClient.getWinetApi().saveWinet("winet", winet.getId(), winet.getType(), winet.getSerNumber(), winet.getComment());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetDataResponseWithParams>() {
            @Override
            public void onResponse(Call<WinetDataResponseWithParams> call, Response<WinetDataResponseWithParams> response) {
                Log.e("save winet info res", "success = " + response.body().getSuccess());
                WinetDataParams winetDataParams = response.body().getParams();

                //progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
                saveApartmentInfo();
                Toast.makeText(getApplicationContext(), "Вайнет \"" + winetDataParams.getSerNumber()+  "\" с типом \"" + winetDataParams.getType() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WinetDataResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveApartmentInfo() {
        if (!addApartmentList.isEmpty()) {
            for (Apartment apartment : addApartmentList) {
                Log.e("add apart", apartment.getFullNumber());
                createApartment(apartment);
            }
            addApartmentList.clear();
        }

        if (!removeApartmentList.isEmpty()) {
            for (Apartment apartment : removeApartmentList) {
                Log.e("remove apart", apartment.getFullNumber());
                deleteApartment(apartment);
            }
            removeApartmentList.clear();
        }
    }

    private void createApartment(Apartment apartment) {
        Call<ApartmentResponseWithParams> messages = ApiClient.getApartmentApi().createApartment(apartment.getApartmentType(),
                                                                                                 apartment.getApartmentDesc(),
                                                                                                null,
                                                                                                 winet.getId());
        //progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<ApartmentResponseWithParams>() {
            @Override
            public void onResponse(Call<ApartmentResponseWithParams> call, Response<ApartmentResponseWithParams> response) {
                Log.e("create apart res", "success = " + response.body().getSuccess());
                WinetDataParams winetDataParams = response.body().getParams();

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                apartmentAdapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Вайнет \"" + winetDataParams.getSerNumber()+  "\" с типом \"" + winetDataParams.getType() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApartmentResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteApartment(Apartment apartment) {
        Call<SimpleResponse> messages = ApiClient.getApartmentApi().deleteApartment("apartment", apartment.getId());
        //progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Log.e("delete apart res", "success = " + response.body().getSuccess());
                //WinetDataParams winetDataParams = response.body().getParams();

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                apartmentAdapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Вайнет \"" + winetDataParams.getSerNumber()+  "\" с типом \"" + winetDataParams.getType() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addApartmentToList() {
        String apartmentNumber = winetApartmentNumber.getText().toString();

        if (apartmentNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Введите номер квартиры", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Apartment apartment : apartmentList) {
            if (apartment.getNumber().equals(apartmentNumber)) {
                Toast.makeText(getApplicationContext(), "Такая квартира уже существует в списке", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Apartment apartment = new Apartment(0, winetApartmentNumber.getText().toString(), winet.getId(), winet);
        apartmentList.add(apartment);
        addApartmentList.add(apartment);
        apartmentAdapter.notifyDataSetChanged();

        winetApartmentNumber.setText("");
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
