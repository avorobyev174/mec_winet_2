package com.avorobyev174.mec_winet.classes.house;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HouseActivity extends AppCompatActivity {
    private ListView housesListView;
    private HouseAdapter adapter;
    private List<House> houseList;
    private TextView infoBar;
    private ImageButton createHouseButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houses_activity);
        init();

        fillHousesList();
    }

    private void init() {
        houseList = new ArrayList<>();
        createHouseButton = findViewById(R.id.createButtonInfoBar);
        housesListView = findViewById(R.id.houses_list_view);
        infoBar = findViewById(R.id.info_bar);
        progressBar  = findViewById(R.id.progressBar);

        infoBar.setText(R.string.houses_list);

        adapter = new HouseAdapter(this, R.layout.house_list_item_view, houseList, getLayoutInflater());
        housesListView.setAdapter(adapter);

        initOnClick();
    }

    private void initOnClick() {
        housesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                House house = houseList.get(i);
                Intent intent = new Intent(HouseActivity.this, SectionActivity.class);
                intent.putExtra(House.class.getSimpleName(), house);
                startActivity(intent);
            }
        });

        createHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewHouse(view);
            }
        });

        createHouseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // pointer goes down
                    ((ImageView)view).setColorFilter(getResources().getColor(R.color.green),
                            PorterDuff.Mode.SRC_ATOP);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // pointer goes up
                    ((ImageView)view).setColorFilter(getResources().getColor(R.color.grey),
                            PorterDuff.Mode.SRC_ATOP);
                }
                return false;
            }
        });
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

                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " домов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HousesInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createNewHouse(View view) {
        HouseCreateDialog houseCreateDialog = new HouseCreateDialog(this, adapter,  houseList);
        houseCreateDialog.show();
    }
}