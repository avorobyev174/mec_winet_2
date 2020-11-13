package com.avorobyev174.mec_winet.classes.winetData;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentActivity;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentCreateDialog;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.InfoBar;
import com.avorobyev174.mec_winet.classes.common.SimpleDialog;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetActivity;
import com.avorobyev174.mec_winet.classes.winet.WinetDeleteDialog;
import com.avorobyev174.mec_winet.classes.winet.WinetInfo;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentAdapter;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfo;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfoResponse;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WinetDataActivity extends Entity implements View.OnClickListener {

    private int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ApartmentAdapter apartmentAdapter;
    private List<Apartment> apartmentList;
    private ProgressBar progressBar;
    private Spinner winetTypeSpinner;
    private EditText serNumberInput, commentInput, coordinateX, coordinateY;
    private ArrayAdapter<CharSequence> adapter;
    private ImageButton barCodeButton, gpsButton;
    private ListView apartmentListView;
    private Winet winet;
    private SimpleDialog changeCoordinatesDialog;

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
        progressBar  = findViewById(R.id.progressBar);
        winetTypeSpinner = findViewById(R.id.winetType);
        serNumberInput = findViewById(R.id.winetSerNumber);
        commentInput = findViewById(R.id.winetComment);
        coordinateX = findViewById(R.id.winetCoordinateX);
        coordinateY = findViewById(R.id.winetCoordinateY);
        barCodeButton = findViewById(R.id.barCodeButton);
        gpsButton = findViewById(R.id.gpsButton);

        InfoBar.init(this);
        InfoBar.changeInfoBarData(winet);

        adapter = ArrayAdapter.createFromResource(this, R.array.winet_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winetTypeSpinner.setAdapter(adapter);

        apartmentAdapter = new ApartmentAdapter(this, R.layout.simple_list_item_view, apartmentList, getLayoutInflater());
        apartmentListView.setAdapter(apartmentAdapter);

        initOnClick();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confirmSimpleDialogButton) {
            getCoordinates();
            if (changeCoordinatesDialog != null) {
                changeCoordinatesDialog.dismiss();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initOnClick() {
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!coordinateX.getText().toString().isEmpty() || !coordinateY.getText().toString().isEmpty()) {
                    changeCoordinatesDialog = new SimpleDialog(WinetDataActivity.this,
                                                                            "Вы хотите изменить координаты?",
                                                                            "изменить");
                    changeCoordinatesDialog.show();
                } else {
                    getCoordinates();
                }
            }
        });

        gpsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return Utils.changeOtherButtonColor(view, motionEvent, getApplicationContext());
            }
        });

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

    private void getCoordinates() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(WinetDataActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else  {
            getCurrentLocation();
        }
    }

    @Override
    public void saveObjData() {
        saveWinetData();
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
                Log.e("get winet info comment", " = " + winetDataInfo.getWinetX());

                winetTypeSpinner.setSelection(adapter.getPosition(winetDataInfo.getType()));
                serNumberInput.setText(winetDataInfo.getSerNumber());
                commentInput.setText(winetDataInfo.getComment());
                coordinateX.setText(winetDataInfo.getWinetX());
                coordinateY.setText(winetDataInfo.getWinetY());

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
                fillApartmentInfo();
                //Toast.makeText(getApplicationContext(), "Вайнет \"" + winetDataInfo.getSerNumber()+  "\" с типом \"" + winetDataInfo.getType() +  "\" загружен", Toast.LENGTH_SHORT).show();
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
        progressBar.setVisibility(ProgressBar.VISIBLE);

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

                //Toast.makeText(getApplicationContext(), "Список квартир загружен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApartmentInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveWinetData() {
        if (serNumberInput.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Cерийный номер не должен быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        //Toast.makeText(getApplicationContext(), Utils.getWinetX(coordinates.getText().toString()) + " " + Utils.getWinetY(coordinates.getText().toString()), Toast.LENGTH_SHORT).show();

        Call<WinetDataResponseWithParams> messages = ApiClient.getWinetApi().saveWinet("winet",
                                                                                        winet.getId(),
                                                                                        winetTypeSpinner.getSelectedItem().toString(),
                                                                                        serNumberInput.getText().toString(),
                                                                                        commentInput.getText().toString(),
                                                                                        coordinateX.getText().toString(),
                                                                                        coordinateY.getText().toString());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetDataResponseWithParams>() {
            @Override
            public void onResponse(Call<WinetDataResponseWithParams> call, Response<WinetDataResponseWithParams> response) {
                Log.e("save winet info res", "success = " + response.body().getSuccess());
                WinetDataParams winetDataParams = response.body().getParams();

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Доступ к локации не предоставлен", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(WinetDataActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(WinetDataActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    coordinateX.setText(String.valueOf(latitude));
                    coordinateY.setText(String.valueOf(longitude));
                    Toast.makeText(getApplicationContext(), "Координаты получены", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, Looper.getMainLooper());
    }
}
