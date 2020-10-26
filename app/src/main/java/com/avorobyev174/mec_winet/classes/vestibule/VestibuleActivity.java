package com.avorobyev174.mec_winet.classes.vestibule;
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

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.winet.WinetActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.floor.Floor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VestibuleActivity extends AppCompatActivity {
    private List<Vestibule> vestList;
    private VestibuleAdapter adapter;
    private ListView vestListView;
    private Floor floor;
    private TextView infoBar;
    private ProgressBar progressBar;
    private ImageButton createVestibuleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vestibule_activity);
        init();

        fillVestibulesList();
    }

    private void init() {
        vestList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);
        vestListView = findViewById(R.id.vestibule_list_view);
        createVestibuleButton = findViewById(R.id.createButtonInfoBar);
        progressBar  = findViewById(R.id.progressBar);
        Bundle arguments = getIntent().getExtras();
        Floor floor = (Floor) arguments.getSerializable(Floor.class.getSimpleName());
        this.floor = floor;

        infoBar.setText(floor.getSection().getHouse().getFullStreetName() + " → " + floor.getSection().getShortNumber() + " → " + floor.getShortNumber());

        adapter = new VestibuleAdapter(this, R.layout.vestibule_list_item_view, vestList, getLayoutInflater());
        vestListView.setAdapter(adapter);

        initOnClick();
    }

    private void initOnClick() {
        vestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vestibule vestibule = vestList.get(i);
                Intent intent = new Intent(VestibuleActivity.this, WinetActivity.class);
                intent.putExtra(Vestibule.class.getSimpleName(), vestibule);
                startActivity(intent);
            }
        });

        createVestibuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewVestibule(view);
            }
        });

        createVestibuleButton.setOnTouchListener(new View.OnTouchListener() {
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

    public void fillVestibulesList() {
        Call<VestibuleInfoResponse> messages = ApiClient.getVestibuleApi().getVestibules(floor.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<VestibuleInfoResponse>() {
            @Override
            public void onResponse(Call<VestibuleInfoResponse> call, Response<VestibuleInfoResponse> response) {
                Log.e("get vest response", "success = " + response.body().getSuccess());

                for (VestibuleInfo vestibuleInfo : response.body().getResult()) {
//                    Log.e("response", "section number = " + sectionInfo.getSectionNumber());
//                    Log.e("response", "section id = " + sectionInfo.getId());
//                    Log.e("response", "section house id= " + sectionInfo.getHouseId());
                    vestList.add(new Vestibule(vestibuleInfo.getId(), Integer.parseInt(vestibuleInfo.getVestibuleNumber()), floor));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " тамбуров", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<VestibuleInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createNewVestibule(View view) {
        VestibuleCreateDialog vestibuleCreateDialog = new VestibuleCreateDialog(this, adapter,  vestList, floor);
        vestibuleCreateDialog.show();
    }
}
