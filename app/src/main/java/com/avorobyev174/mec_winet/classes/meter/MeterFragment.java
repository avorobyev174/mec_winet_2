package com.avorobyev174.mec_winet.classes.meter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.apartment.ApartmentFragment;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.EntityFragment;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataFragment;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeterFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private fragmentShowListner fragmentShowListner;
    private Spinner meterTypeSpinner;
    private EditText serNumberInput, passwordInput;
    private Meter meter;
    private ArrayAdapter<CharSequence> adapter;

    public MeterFragment() {
        // Required empty public constructor
    }

    //пропущена иерархия winetData
    @Override
    public Entity getParentEntity() {
        return meter;
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return ApartmentFragment.newInstance(progressBar, fragmentManager);
    }

    @Override
    public void showEntityCreateDialog() {

    }

    public void setSerNumber(String serNumber) {
        this.serNumberInput.setText(serNumber);
    }

    public static MeterFragment newInstance(ProgressBar mainProgressBar, FragmentManager myFragmentManager) {
        progressBar = mainProgressBar;
        fragmentManager = myFragmentManager;

        return new MeterFragment();
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
            meter = (Meter) bundle.getSerializable("entity");
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meter_fragment, container, false);
        fragmentShowListner.onChangeEntity(meter);

        meterTypeSpinner = view.findViewById(R.id.meterType);
        serNumberInput = view.findViewById(R.id.meterSerNumber);
        passwordInput = view.findViewById(R.id.meterPassword);
        ImageButton barCodeButton = view.findViewById(R.id.barCodeButton);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.meter_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meterTypeSpinner.setAdapter(adapter);

        fillMeterInfo();

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
        return view;
    }

    @Override
    public void saveEntityInfoDialog() {
        saveMeterData();
    }

    private void fillMeterInfo() {
        Call<MeterInfoResponse> messages = ApiClient.getMeterApi().getMeter("meter", meter.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<MeterInfoResponse>() {
            @Override
            public void onResponse(Call<MeterInfoResponse> call, Response<MeterInfoResponse> response) {
                Log.e("get meter info res", "success = " + response.body().getSuccess());
                MetertInfo meterInfo = response.body().getResult().get(0);
                Log.e("get meter info id", " = " + meterInfo.getId());

                meterTypeSpinner.setSelection(adapter.getPosition(Utils.getMeterTypeTitle(meterInfo.getMeterType())));
                serNumberInput.setText(meterInfo.getSerNumber());
                passwordInput.setText(meterInfo.getPassword());

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Счетчик \"" + meterInfo.getSerNumber() +  "\" загружен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMeterData() {
        String serialNumber =  serNumberInput.getText().toString();
        String pass =  passwordInput.getText().toString();

        if (serialNumber.isEmpty()) {
            Toast.makeText(getContext(), "Cерийный номер не должен быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.isEmpty()) {
            Toast.makeText(getContext(), "Пароль не должен быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<MeterResponseWithParams> messages = ApiClient.getMeterApi().saveMeter("meter", meter.getId(),
                Utils.getMeterType(meterTypeSpinner.getSelectedItem().toString()), serialNumber, pass);

        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<MeterResponseWithParams>() {
            @Override
            public void onResponse(Call<MeterResponseWithParams> call, Response<MeterResponseWithParams> response) {
                Log.e("save winet info res", "success = " + response.body().getSuccess());
                MeterParams meterParams = response.body().getParams();

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "Счетчик \"" + meterParams.getSerNumber() +  "\" обновлен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void scan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.initiateScan();
    }

}