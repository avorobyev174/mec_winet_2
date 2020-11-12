package com.avorobyev174.mec_winet.classes.section;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.InfoBar;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.floor.FloorActivity;
import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.house.HouseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SectionActivity extends Entity {
    private ListView sectionListView;
    private SectionAdapter adapter;
    private List<Section> sectionList;
    private House house;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_activity);
        init();

        fillSectionsList();
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        house = (House) arguments.getSerializable(House.class.getSimpleName());
        initNavMenu(this, HouseActivity.class, null);

        sectionList = new ArrayList<>();
        sectionListView = findViewById(R.id.sections_list_view);
        progressBar  = findViewById(R.id.progressBar);

        //Utils.changeInfoBarData(this, house);
        InfoBar.init(this);
        InfoBar.changeInfoBarData(house);

        adapter = new SectionAdapter(this, R.layout.simple_list_item_view, sectionList, getLayoutInflater());
        sectionListView.setAdapter(adapter);

        sectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Section section = sectionList.get(i);
                Intent intent = new Intent(SectionActivity.this, FloorActivity.class);
                intent.putExtra(Section.class.getSimpleName(), section);
                startActivity(intent);
            }
        });
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
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showObjCreateDialog() {
        SectionCreateDialog houseCreateDialog = new SectionCreateDialog(this, adapter,  sectionList, house);
        houseCreateDialog.show();
    }
}
