package com.avorobyev174.mec_winet.classes.vestibule;

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
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.floor.FloorFragment;
import com.avorobyev174.mec_winet.classes.vestibule.api.VestibuleInfo;
import com.avorobyev174.mec_winet.classes.vestibule.api.VestibuleInfoResponse;
import com.avorobyev174.mec_winet.classes.winet.WinetFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VestibuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VestibuleFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private fragmentShowListner fragmentShowListner;
    private ListView vestListView;
    private VestibuleAdapter adapter;
    private List<Vestibule> vestList;
    private Floor floor;

    public VestibuleFragment() {
        // Required empty public constructor
    }

    @Override
    public Entity getParentEntity() {
        return floor;
    }

    @Override
    public void showEntityCreateDialog() {
        VestibuleCreateDialog vestibuleCreateDialog = new VestibuleCreateDialog(getActivity(), adapter,  vestList, floor);
        vestibuleCreateDialog.show();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return FloorFragment.newInstance(progressBar, fragmentManager);
    }

    public static VestibuleFragment newInstance(ProgressBar mainProgressBar, FragmentManager myFragmentManager) {
        progressBar = mainProgressBar;
        fragmentManager = myFragmentManager;

        return new VestibuleFragment();
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
            floor = (Floor) bundle.getSerializable("entity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vestibule_fragment, container, false);
        fragmentShowListner.onChangeEntity(floor);

        vestList = new ArrayList<>();
        ListView vestListView = view.findViewById(R.id.vestibule_list_view);
        adapter = new VestibuleAdapter(view.getContext(), R.layout.simple_list_item_view, vestList, getLayoutInflater());
        vestListView.setAdapter(adapter);

        fillVestibulesList();

        vestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vestibule vestibule = vestList.get(i);

                EntityFragment entityFragment = WinetFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", vestibule);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        return view;
    }

    public void fillVestibulesList() {
        Call<VestibuleInfoResponse> messages = ApiClient.getVestibuleApi().getVestibules(floor.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<VestibuleInfoResponse>() {
            @Override
            public void onResponse(Call<VestibuleInfoResponse> call, Response<VestibuleInfoResponse> response) {
                Log.e("get vest response", "success = " + response.body().getSuccess());

                for (VestibuleInfo vestibuleInfo : response.body().getResult()) {
                    vestList.add(new Vestibule(vestibuleInfo.getId(), Integer.parseInt(vestibuleInfo.getVestibuleNumber()), floor));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " тамбуров", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<VestibuleInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}