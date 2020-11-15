package com.avorobyev174.mec_winet.classes.winet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.EntityFragment;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleAdapter;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleFragment;
import com.avorobyev174.mec_winet.classes.vestibule.api.VestibuleInfo;
import com.avorobyev174.mec_winet.classes.vestibule.api.VestibuleInfoResponse;
import com.avorobyev174.mec_winet.classes.winet.api.WinetInfo;
import com.avorobyev174.mec_winet.classes.winet.api.WinetInfoResponse;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WinetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WinetFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private fragmentShowListner fragmentShowListner;
    private WinetAdapter adapter;
    private List<Winet> winetList;
    private Vestibule vestibule;
    private WinetCreateDialog winetCreateDialog;

    public WinetFragment() {
        // Required empty public constructor
    }

    @Override
    public Entity getParentEntity() {
        return vestibule;
    }

    @Override
    public void showEntityCreateDialog() {
        winetCreateDialog = new WinetCreateDialog(getActivity(), adapter, winetList, vestibule);
        winetCreateDialog.show();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return VestibuleFragment.newInstance(progressBar, fragmentManager);
    }

    public WinetCreateDialog getCreateDialog() {
        return winetCreateDialog;
    }

    public static WinetFragment newInstance(ProgressBar mainProgressBar, FragmentManager myFragmentManager) {
        progressBar = mainProgressBar;
        fragmentManager = myFragmentManager;

        return new WinetFragment();
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
            vestibule = (Vestibule) bundle.getSerializable("entity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.winet_fragment, container, false);
        fragmentShowListner.onChangeEntity(vestibule);

        winetList = new ArrayList<>();
        ListView winetListView = view.findViewById(R.id.winet_list_view);
        adapter = new WinetAdapter(view.getContext(), R.layout.simple_list_item_view, winetList, getLayoutInflater());
        winetListView.setAdapter(adapter);

        fillWinetList();

        winetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Winet winet = winetList.get(i);

                EntityFragment entityFragment = WinetDataFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", winet);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        return view;
    }

    private void fillWinetList() {
        Call<WinetInfoResponse> messages = ApiClient.getWinetApi().getWinets(vestibule.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetInfoResponse>() {
            @Override
            public void onResponse(Call<WinetInfoResponse> call, Response<WinetInfoResponse> response) {
                Log.e("get winet response", "success = " + response.body().getSuccess());

                for (WinetInfo winetInfo : response.body().getResult()) {
                    winetList.add(new Winet(winetInfo.getId(), winetInfo.getType(), winetInfo.getSerNumber(), vestibule));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " вайнет", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WinetInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}