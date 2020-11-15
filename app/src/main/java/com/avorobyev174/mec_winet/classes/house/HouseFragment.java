package com.avorobyev174.mec_winet.classes.house;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.EntityFragment;
import com.avorobyev174.mec_winet.classes.house.api.HouseInfo;
import com.avorobyev174.mec_winet.classes.house.api.HousesInfoResponse;
import com.avorobyev174.mec_winet.classes.section.SectionFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private HouseAdapter adapter;
    private List<House> houseList;
    private fragmentShowListner fragmentShowListner;

    public HouseFragment() {
        // Required empty public constructor
    }

    public static EntityFragment newInstance(ProgressBar myProgressBar, FragmentManager myFragmentManager) {
        fragmentManager = myFragmentManager;
        progressBar = myProgressBar;

        return new HouseFragment();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return null;
    }

    @Override
    public void showEntityCreateDialog() {
        HouseCreateDialog houseCreateDialog = new HouseCreateDialog(getActivity(), adapter,  houseList);
        houseCreateDialog.show();
    }

    @Override
    public Entity getParentEntity() {
        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentShowListner = (fragmentShowListner) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentShowListner.onChangeEntity(null);
        View view = inflater.inflate(R.layout.house_fragment, container, false);
        houseList = new ArrayList<>();
        ListView housesListView = view.findViewById(R.id.house_list_view);
        adapter = new HouseAdapter(view.getContext(), R.layout.simple_list_item_view, houseList, getLayoutInflater());
        housesListView.setAdapter(adapter);

        fillHousesList();

        housesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                House house = houseList.get(i);

                EntityFragment entityFragment = SectionFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("entity", house);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        return view;
    }

    public void fillHousesList() {
        Call<HousesInfoResponse> messages = ApiClient.getHouseApi().getHouses();
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<HousesInfoResponse>() {
            @Override
            public void onResponse(Call<HousesInfoResponse> call, Response<HousesInfoResponse> response) {
                Log.e("get house response", "success = " + response.body().getSuccess());

                for (HouseInfo houseInfo : response.body().getResult()) {
                    houseList.add(new House(houseInfo.getId(), houseInfo.getStreet(), houseInfo.getHouseNumber()));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getContext(), "Загружено " + response.body().getResult().size() + " домов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HousesInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}