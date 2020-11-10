package com.avorobyev174.mec_winet.classes.floor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.house.HouseActivity;
import com.avorobyev174.mec_winet.classes.house.HouseCreateDialog;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.section.Section;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FloorActivity extends Entity {
    private ListView floorListView;
    private FloorAdapter adapter;
    private List<Floor> floorList;
    private TextView infoBar;
    private ProgressBar progressBar;
    private Section section;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floor_activity);
        init();

        fillFloorsList();
    }


    private void init() {
        Bundle arguments = getIntent().getExtras();
        section = (Section) arguments.getSerializable(Section.class.getSimpleName());
        initNavMenu(this, SectionActivity.class, section.getHouse());

        floorList = new ArrayList<>();
        floorListView = findViewById(R.id.floors_list_view);
        infoBar = findViewById(R.id.info_bar);
        progressBar  = findViewById(R.id.progressBar);

        infoBar.setText(section.getHouse().getFullStreetName() + " → " + section.getShortNumber());

        adapter = new FloorAdapter(this, R.layout.simple_list_item_view, floorList, getLayoutInflater());
        floorListView.setAdapter(adapter);

        floorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Floor floor = floorList.get(i);
                Intent intent = new Intent(FloorActivity.this, VestibuleActivity.class);
                intent.putExtra(Floor.class.getSimpleName(), floor);
                startActivity(intent);
            }
        });
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

                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " этажей", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FloorInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showObjCreateDialog() {
        FloorCreateDialog floorCreateDialog = new FloorCreateDialog(this, adapter,  floorList, section);
        floorCreateDialog.show();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    Log.e("back","back");
//                    Intent intent = new Intent(FloorActivity.this, SectionActivity.class);
//                    intent.putExtra(House.class.getSimpleName(), section.getHouse());
//                    startActivity(intent);
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
