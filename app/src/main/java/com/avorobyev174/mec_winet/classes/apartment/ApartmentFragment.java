package com.avorobyev174.mec_winet.classes.apartment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.EntityFragment;
import com.avorobyev174.mec_winet.classes.meter.Meter;
import com.avorobyev174.mec_winet.classes.meter.MeterAdapter;
import com.avorobyev174.mec_winet.classes.meter.MeterCreateDialog;
import com.avorobyev174.mec_winet.classes.meter.MeterFragment;
import com.avorobyev174.mec_winet.classes.meter.api.MeterInfoResponse;
import com.avorobyev174.mec_winet.classes.meter.api.MetertInfo;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApartmentFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private fragmentShowListner fragmentShowListner;
    private MeterAdapter adapter;
    private List<Meter> meterList;
    private Apartment apartment;
    private MeterCreateDialog meterCreateDialog;

    public ApartmentFragment() {
        // Required empty public constructor
    }

    //пропущена иерархия winetData
    @Override
    public Entity getParentEntity() {
        return apartment;
    }

    @Override
    public void showEntityCreateDialog() {
        meterCreateDialog = new MeterCreateDialog(getActivity(), adapter, meterList, apartment);
        meterCreateDialog.show();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return WinetDataFragment.newInstance(progressBar, fragmentManager);
    }

    public MeterCreateDialog getCreateDialog() {
        return meterCreateDialog;
    }

    public static ApartmentFragment newInstance(ProgressBar mainProgressBar, FragmentManager myFragmentManager) {
        progressBar = mainProgressBar;
        fragmentManager = myFragmentManager;

        return new ApartmentFragment();
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
            apartment = (Apartment) bundle.getSerializable("entity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apartment_fragment, container, false);
        fragmentShowListner.onChangeEntity(apartment);

        meterList = new ArrayList<>();
        ListView meterListView = view.findViewById(R.id.meterListView);
        adapter = new MeterAdapter(view.getContext(), R.layout.simple_list_item_view, meterList, getLayoutInflater());
        meterListView.setAdapter(adapter);

        fillMeterList();

        meterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meter meter = meterList.get(i);

                EntityFragment entityFragment = MeterFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", meter);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        return view;
    }

    private void fillMeterList() {
        Call<MeterInfoResponse> messages = ApiClient.getMeterApi().getMeters(apartment.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<MeterInfoResponse>() {
            @Override
            public void onResponse(Call<MeterInfoResponse> call, Response<MeterInfoResponse> response) {
                Log.e("get meter response", "success = " + response.body().getSuccess());

                for (MetertInfo metertInfo : response.body().getResult()) {
                    meterList.add(new Meter(metertInfo.getId(), metertInfo.getMeterType(), metertInfo.getSerNumber(), metertInfo.getPassword(), apartment));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " счетчиков", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MeterInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}