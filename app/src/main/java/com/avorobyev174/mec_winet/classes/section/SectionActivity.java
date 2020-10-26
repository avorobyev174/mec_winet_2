package com.avorobyev174.mec_winet.classes.section;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.classes.floor.FloorActivity;
import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.house.House;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SectionActivity extends AppCompatActivity {
    private List<Section> sectionList;
    private SectionAdapter adapter;
    private ListView sectionListView;
    private House house;
    private TextView infoBar;
    private ProgressBar progressBar;
    private ImageButton createSectionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sections_activity);
        init();

        fillSectionsList();
    }

    private void init() {
        sectionList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);
        sectionListView = findViewById(R.id.sections_list_view);
        createSectionButton = findViewById(R.id.createButtonInfoBar);
        progressBar  = findViewById(R.id.progressBar);
        Bundle arguments = getIntent().getExtras();
        this.house = (House) arguments.getSerializable(House.class.getSimpleName());

        infoBar.setText(house.getFullStreetName());

        adapter = new SectionAdapter(this, R.layout.section_list_item_view, sectionList, getLayoutInflater());
        sectionListView.setAdapter(adapter);

        initOnClick();
    }

    private void initOnClick() {
        sectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Section section = sectionList.get(i);
                Intent intent = new Intent(SectionActivity.this, FloorActivity.class);
                intent.putExtra(Section.class.getSimpleName(), section);
                startActivity(intent);
            }
        });

        createSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewSection(view);
            }
        });

        createSectionButton.setOnTouchListener(new View.OnTouchListener() {
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

    public void fillSectionsList() {
        Call<SectonInfoResponse> messages = ApiClient.getSectionApi().getSections(house.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<SectonInfoResponse>() {
            @Override
            public void onResponse(Call<SectonInfoResponse> call, Response<SectonInfoResponse> response) {
                Log.e("get section response", "success = " + response.body().getSuccess());

                for (SectionInfo sectionInfo : response.body().getResult()) {
//                    Log.e("response", "section number = " + sectionInfo.getSectionNumber());
//                    Log.e("response", "section id = " + sectionInfo.getId());
//                    Log.e("response", "section house id= " + sectionInfo.getHouseId());
                    sectionList.add(new Section(sectionInfo.getId(), Integer.parseInt(sectionInfo.getSectionNumber()), house));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " подьездов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SectonInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createNewSection(View view) {
        SectionCreateDialog houseCreateDialog = new SectionCreateDialog(this, adapter,  sectionList, house);
        houseCreateDialog.show();
    }
}
