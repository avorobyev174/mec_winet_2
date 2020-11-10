package com.avorobyev174.mec_winet.classes.winet;
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
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.floor.FloorActivity;
import com.avorobyev174.mec_winet.classes.section.SectionActivity;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleActivity;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WinetActivity extends Entity {
    private ListView winetListView;
    private WinetAdapter adapter;
    private List<Winet> winetList;
    private TextView infoBar;
    private ProgressBar progressBar;
    private Vestibule vestibule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winet_activity);
        init();

        fillWinetList();
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        vestibule = (Vestibule) arguments.getSerializable(Vestibule.class.getSimpleName());
        initNavMenu(this, VestibuleActivity.class, vestibule.getFloor());

        winetList = new ArrayList<>();
        winetListView = findViewById(R.id.winet_list_view);
        infoBar = findViewById(R.id.info_bar);
        progressBar = findViewById(R.id.progressBar);

        infoBar.setText(vestibule.getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + vestibule.getFloor().getSection().getShortNumber() + " → "
                        + vestibule.getFloor().getShortNumber() + " → "
                        + vestibule.getShortNumber());

        adapter = new WinetAdapter(this, R.layout.simple_list_item_view, winetList, getLayoutInflater());
        winetListView.setAdapter(adapter);

        winetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Winet winet = winetList.get(i);
                Intent intent = new Intent(WinetActivity.this, WinetDataActivity.class);
                intent.putExtra(Winet.class.getSimpleName(), winet);
                startActivity(intent);
            }
        });
    }


    private void fillWinetList() {
        Call<WinetInfoResponse> messages = ApiClient.getWinetApi().getWinets(vestibule.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetInfoResponse>() {
            @Override
            public void onResponse(Call<WinetInfoResponse> call, Response<WinetInfoResponse> response) {
                Log.e("get winet response", "success = " + response.body().getSuccess());

                for (WinetInfo winetInfo : response.body().getResult()) {
                    winetList.add(new Winet(winetInfo.getId(), winetInfo.getType(), winetInfo.getSerNumber(), vestibule));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " вайнет", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WinetInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showObjCreateDialog() {
        WinetCreateDialog winetCreateDialog = new WinetCreateDialog(this, adapter,  winetList, vestibule);
        winetCreateDialog.show();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    Log.e("back","back");
//                    Intent intent = new Intent(WinetActivity.this, VestibuleActivity.class);
//                    intent.putExtra(Floor.class.getSimpleName(), vestibule.getFloor());
//                    startActivity(intent);
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
