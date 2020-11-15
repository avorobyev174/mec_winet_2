package com.avorobyev174.mec_winet.classes.winetData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentAdapter;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentCreateDialog;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentFragment;
import com.avorobyev174.mec_winet.classes.apartment.api.ApartmentInfo;
import com.avorobyev174.mec_winet.classes.apartment.api.ApartmentInfoResponse;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.EntityFragment;
import com.avorobyev174.mec_winet.classes.common.SimpleDialog;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winet.WinetFragment;
import com.avorobyev174.mec_winet.classes.winet.api.WinetInfo;
import com.avorobyev174.mec_winet.classes.winetData.api.WinetDataInfoResponse;
import com.avorobyev174.mec_winet.classes.winetData.api.WinetDataParams;
import com.avorobyev174.mec_winet.classes.winetData.api.WinetDataResponseWithParams;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WinetDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WinetDataFragment extends EntityFragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private fragmentShowListner fragmentShowListner;
    private int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ApartmentAdapter apartmentAdapter;
    private List<Apartment> apartmentList;
    private Spinner winetTypeSpinner;
    private EditText serNumberInput, commentInput, coordinateX, coordinateY;
    private ArrayAdapter<CharSequence> adapter;
    private ImageButton barCodeButton, gpsButton;
    private ListView apartmentListView;
    private Winet winet;
    private SimpleDialog changeCoordinatesDialog;

    public WinetDataFragment() {
        // Required empty public constructor
    }

    @Override
    public Entity getParentEntity() {
        return winet;
    }

    @Override
    public void showEntityCreateDialog() {
        ApartmentCreateDialog apartmentCreateDialog = new ApartmentCreateDialog(getActivity(), apartmentAdapter,  apartmentList, winet);
        apartmentCreateDialog.show();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return WinetFragment.newInstance(progressBar, fragmentManager);
    }

    public void setSerNumber(String serNumber) {
        this.serNumberInput.setText(serNumber);
    }

    public static WinetDataFragment newInstance(ProgressBar mainProgressBar, FragmentManager myFragmentManager) {
        progressBar = mainProgressBar;
        fragmentManager = myFragmentManager;

        return new WinetDataFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentShowListner = (fragmentShowListner) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            winet = (Winet) bundle.getSerializable("entity");
        }
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

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.winet_data_fragment, container, false);
        fragmentShowListner.onChangeEntity(winet);

        apartmentList = new ArrayList<>();
        apartmentListView = view.findViewById(R.id.apartmentList);
        winetTypeSpinner = view.findViewById(R.id.winetType);
        serNumberInput = view.findViewById(R.id.winetSerNumber);
        commentInput = view.findViewById(R.id.winetComment);
        coordinateX = view.findViewById(R.id.winetCoordinateX);
        coordinateY = view.findViewById(R.id.winetCoordinateY);
        barCodeButton = view.findViewById(R.id.barCodeButton);
        gpsButton = view.findViewById(R.id.gpsButton);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.winet_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winetTypeSpinner.setAdapter(adapter);

        apartmentAdapter = new ApartmentAdapter(getContext(), R.layout.simple_list_item_view, apartmentList, getLayoutInflater());
        apartmentListView.setAdapter(apartmentAdapter);

        fillInfo();

        apartmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Apartment apartment = apartmentList.get(i);

                EntityFragment entityFragment = ApartmentFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", apartment);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        initOnClick();

        return view;
    }

    @Override
    public void saveEntityInfoDialog() {
        saveWinetData();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initOnClick() {
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!coordinateX.getText().toString().isEmpty() || !coordinateY.getText().toString().isEmpty()) {
                    changeCoordinatesDialog = new SimpleDialog(getActivity(),
                                                    WinetDataFragment.this,
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
                return Utils.changeOtherButtonColor(view, motionEvent, getContext());
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
                return Utils.changeOtherButtonColor(view, motionEvent, getContext());
            }
        });

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
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveWinetData() {
        if (serNumberInput.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Cерийный номер не должен быть пустым", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Вайнет \"" + winetDataParams.getSerNumber()+  "\" с типом \"" + winetDataParams.getType() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WinetDataResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void scan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.initiateScan();
    }

    private void getCoordinates() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else  {
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    coordinateX.setText(String.valueOf(latitude));
                    coordinateY.setText(String.valueOf(longitude));
                    Toast.makeText(getContext(), "Координаты получены", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Доступ к локации не предоставлен", Toast.LENGTH_SHORT).show();
            }
        }
    }
}