package com.avorobyev174.mec_winet.classes.house;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.avorobyev174.mec_winet.classes.common.InfoBar;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;

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
public class HouseFragment extends Fragment {
    private ListView housesListView;
    private HouseAdapter adapter;
    private List<House> houseList;
    //private ProgressBar progressBar;

    public HouseFragment() {
        // Required empty public constructor

    }

    public static HouseFragment newInstance(String param1, String param2) {
        HouseFragment fragment = new HouseFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fillHousesList();

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house, container, false);
        houseList = new ArrayList<>();
        housesListView = view.findViewById(R.id.houses_list_view);
        //progressBar  = view.findViewById(R.id.progressBar);
        housesListView = view.findViewById(R.id.houses_list_view);

        adapter = new HouseAdapter(view.getContext(), R.layout.simple_list_item_view, houseList, getLayoutInflater());
        housesListView.setAdapter(adapter);

        housesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                House house = houseList.get(i);
                Intent intent = new Intent(view.getContext(), SectionActivity.class);
                intent.putExtra(House.class.getSimpleName(), house);
                startActivity(intent);
            }
        });

        return view;
    }

    public void fillHousesList() {
        Call<HousesInfoResponse> messages = ApiClient.getHouseApi().getHouses();
        //progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<HousesInfoResponse>() {
            @Override
            public void onResponse(Call<HousesInfoResponse> call, Response<HousesInfoResponse> response) {
                Log.e("get house response", "success = " + response.body().getSuccess());

                for (HouseInfo houseInfo : response.body().getResult()) {
                    houseList.add(new House(houseInfo.getId(), houseInfo.getStreet(), houseInfo.getHouseNumber()));
                }

                //progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " домов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HousesInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}