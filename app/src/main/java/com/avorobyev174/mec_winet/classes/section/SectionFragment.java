package com.avorobyev174.mec_winet.classes.section;

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
import com.avorobyev174.mec_winet.classes.floor.FloorFragment;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.house.HouseFragment;
import com.avorobyev174.mec_winet.classes.section.api.SectionInfo;
import com.avorobyev174.mec_winet.classes.section.api.SectonInfoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private SectionAdapter adapter;
    private List<Section> sectionList;
    private fragmentShowListner fragmentShowListner;
    private House house;

    public SectionFragment() {
        // Required empty public constructor
    }

    public static EntityFragment newInstance(ProgressBar myProgressBar, FragmentManager myFragmentManager) {
        progressBar = myProgressBar;
        fragmentManager = myFragmentManager;

        return new SectionFragment();
    }

    @Override
    public Entity getParentEntity() {
        return house;
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return HouseFragment.newInstance(progressBar, fragmentManager);
    }

    @Override
    public void showEntityCreateDialog() {
        SectionCreateDialog sectionCreateDialog = new SectionCreateDialog(getActivity(), adapter,  sectionList, house);
        sectionCreateDialog.show();
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
            house = (House) bundle.getSerializable("entity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.section_fragment, null);
        fragmentShowListner.onChangeEntity(house);

        sectionList = new ArrayList<>();
        ListView sectionListView = view.findViewById(R.id.section_list_view);
        adapter = new SectionAdapter(view.getContext(), R.layout.simple_list_item_view, sectionList, getLayoutInflater());
        sectionListView.setAdapter(adapter);

        fillSectionsList();

        sectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Section section = sectionList.get(i);

                EntityFragment entityFragment = FloorFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", section);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });


        return view;
    }

    public void fillSectionsList() {
        Call<SectonInfoResponse> messages = ApiClient.getSectionApi().getSections(house.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<SectonInfoResponse>() {
            @Override
            public void onResponse(Call<SectonInfoResponse> call, Response<SectonInfoResponse> response) {
                Log.e("get section response", "success = " + response.body().getSuccess());

                for (SectionInfo sectionInfo : response.body().getResult()) {
                    sectionList.add(new Section(sectionInfo.getId(), Integer.parseInt(sectionInfo.getSectionNumber()), house));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " подьездов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SectonInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}