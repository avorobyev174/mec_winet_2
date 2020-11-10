package com.avorobyev174.mec_winet.classes.vestibule;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.floor.FloorActivity;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.winet.WinetActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.floor.Floor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VestibuleActivity extends Entity {
    private ListView vestListView;
    private VestibuleAdapter adapter;
    private List<Vestibule> vestList;
    private TextView infoBar;
    private ProgressBar progressBar;
    private Floor floor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vestibule_activity);
        init();

        fillVestibulesList();
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        floor = (Floor) arguments.getSerializable(Floor.class.getSimpleName());
        initNavMenu(this, FloorActivity.class, floor.getSection());

        vestList = new ArrayList<>();
        vestListView = findViewById(R.id.vestibule_list_view);
        infoBar = findViewById(R.id.info_bar);
        progressBar  = findViewById(R.id.progressBar);

        infoBar.setText(floor.getSection().getHouse().getFullStreetName() + " → " + floor.getSection().getShortNumber() + " → " + floor.getShortNumber());

        adapter = new VestibuleAdapter(this, R.layout.simple_list_item_view, vestList, getLayoutInflater());
        vestListView.setAdapter(adapter);

        vestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vestibule vestibule = vestList.get(i);
                Intent intent = new Intent(VestibuleActivity.this, WinetActivity.class);
                intent.putExtra(Vestibule.class.getSimpleName(), vestibule);
                startActivity(intent);
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

    @Override
    public void showObjCreateDialog() {
        VestibuleCreateDialog vestibuleCreateDialog = new VestibuleCreateDialog(this, adapter,  vestList, floor);
        vestibuleCreateDialog.show();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    Log.e("back","back");
//                    Intent intent = new Intent(VestibuleActivity.this, FloorActivity.class);
//                    intent.putExtra(Section.class.getSimpleName(), floor.getSection());
//                    startActivity(intent);
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
