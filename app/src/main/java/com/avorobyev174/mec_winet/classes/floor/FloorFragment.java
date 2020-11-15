package com.avorobyev174.mec_winet.classes.floor;

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
import com.avorobyev174.mec_winet.classes.floor.api.FloorInfo;
import com.avorobyev174.mec_winet.classes.floor.api.FloorInfoResponse;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.section.SectionFragment;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FloorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FloorFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private FloorAdapter adapter;
    private List<Floor> floorList;
    private fragmentShowListner fragmentShowListner;
    private Section section;

    public FloorFragment() {
        // Required empty public constructor
    }

    @Override
    public Entity getParentEntity() {
        return section;
    }

    @Override
    public void showEntityCreateDialog() {
        FloorCreateDialog floorCreateDialog = new FloorCreateDialog(getActivity(), adapter,  floorList, section);
        floorCreateDialog.show();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return SectionFragment.newInstance(progressBar, fragmentManager);
    }

    public static FloorFragment newInstance(ProgressBar mainProgressBar, FragmentManager myFragmentManager) {
        progressBar = mainProgressBar;
        fragmentManager = myFragmentManager;

        return new FloorFragment();
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
            section = (Section) bundle.getSerializable("entity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.floor_fragment, container, false);
        fragmentShowListner.onChangeEntity(section);

        floorList = new ArrayList<>();
        ListView floorListView = view.findViewById(R.id.floor_list_view);
        adapter = new FloorAdapter(view.getContext(), R.layout.simple_list_item_view, floorList, getLayoutInflater());
        floorListView.setAdapter(adapter);

        fillFloorsList();

        floorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Floor floor = floorList.get(i);

                EntityFragment entityFragment = VestibuleFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", floor);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        return view;
    }

    public void fillFloorsList() {
        Call<FloorInfoResponse> messages = ApiClient.getFloorApi().getFloors(section.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<FloorInfoResponse>() {
            @Override
            public void onResponse(Call<FloorInfoResponse> call, Response<FloorInfoResponse> response) {
                Log.e("get floor response", "success = " + response.body().getSuccess());

                for (FloorInfo floorInfo : response.body().getResult()) {
                    floorList.add(new Floor(floorInfo.getId(), Integer.parseInt(floorInfo.getFloorNumber()), section));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " этажей", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FloorInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}