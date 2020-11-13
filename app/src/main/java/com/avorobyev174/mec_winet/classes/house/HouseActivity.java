package com.avorobyev174.mec_winet.classes.house;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.InfoBar;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HouseActivity extends Entity {
//    private ListView housesListView;
//    private HouseAdapter adapter;
//    private List<House> houseList;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity);
        init();

        //fillHousesList();
    }

    private void init() {
        initNavMenu(this, null, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HouseFragment()).commit();
//        houseList = new ArrayList<>();
//        housesListView = findViewById(R.id.houses_list_view);
//        progressBar  = findViewById(R.id.progressBar);
//
        InfoBar.init(this);
        InfoBar.changeInfoBarData(null);
//
//        adapter = new HouseAdapter(this, R.layout.simple_list_item_view, houseList, getLayoutInflater());
//        housesListView.setAdapter(adapter);
//
//        housesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                House house = houseList.get(i);
//                Intent intent = new Intent(HouseActivity.this, SectionActivity.class);
//                intent.putExtra(House.class.getSimpleName(), house);
//                startActivity(intent);
//            }
//        });
    }

//    @Override
//    public void showObjCreateDialog() {
//        HouseCreateDialog houseCreateDialog = new HouseCreateDialog(this, adapter,  houseList);
//        houseCreateDialog.show();
//    }

//    public void fillHousesList() {
//        Call<HousesInfoResponse> messages = ApiClient.getHouseApi().getHouses();
//        progressBar.setVisibility(ProgressBar.VISIBLE);
//
//        messages.enqueue(new Callback<HousesInfoResponse>() {
//            @Override
//            public void onResponse(Call<HousesInfoResponse> call, Response<HousesInfoResponse> response) {
//                Log.e("get house response", "success = " + response.body().getSuccess());
//
//                for (HouseInfo houseInfo : response.body().getResult()) {
//                    houseList.add(new House(houseInfo.getId(), houseInfo.getStreet(), houseInfo.getHouseNumber()));
//                }
//
//                progressBar.setVisibility(ProgressBar.INVISIBLE);
//                adapter.notifyDataSetChanged();
//
//                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " домов", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<HousesInfoResponse> call, Throwable t) {
//                Log.e("response", "failure " + t);
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}