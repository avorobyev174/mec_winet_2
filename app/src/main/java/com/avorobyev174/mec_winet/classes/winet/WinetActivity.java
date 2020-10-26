package com.avorobyev174.mec_winet.classes.winet;
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
import com.avorobyev174.mec_winet.WinetInfoActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleCreateDialog;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleInfo;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleInfoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WinetActivity extends AppCompatActivity {
    private List<Winet> winetList;
    private WinetAdapter adapter;
    private ListView winetListView;
    private Vestibule vestibule;
    private TextView infoBar;
    private ProgressBar progressBar;
    private ImageButton createWinetButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winet_activity);
        init();

        fillWinetList();
    }

    private void init() {
        winetList = new ArrayList<>();
        infoBar = findViewById(R.id.info_bar);
        winetListView = findViewById(R.id.winet_list_view);
        createWinetButton = findViewById(R.id.createButtonInfoBar);
        progressBar = findViewById(R.id.progressBar);
        Bundle arguments = getIntent().getExtras();
        this.vestibule = (Vestibule) arguments.getSerializable(Vestibule.class.getSimpleName());
        infoBar.setText(vestibule.getFloor().getSection().getHouse().getFullStreetName() + " → "
                        + vestibule.getFloor().getSection().getShortNumber() + " → "
                        + vestibule.getFloor().getShortNumber() + " → "
                        + vestibule.getShortNumber());

        adapter = new WinetAdapter(this, R.layout.simple_list_item_view, winetList, getLayoutInflater());
        winetListView.setAdapter(adapter);

        initOnClick();
    }

    public void initOnClick() {
        winetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Winet winet = winetList.get(i);
                Intent intent = new Intent(WinetActivity.this, WinetInfoActivity.class);
                intent.putExtra(Winet.class.getSimpleName(), winet);
                startActivity(intent);
            }
        });

        createWinetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewWinet(view);
            }
        });

        createWinetButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // pointer goes down
                    ((ImageView) view).setColorFilter(getResources().getColor(R.color.green),
                            PorterDuff.Mode.SRC_ATOP);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // pointer goes up
                    ((ImageView) view).setColorFilter(getResources().getColor(R.color.grey),
                            PorterDuff.Mode.SRC_ATOP);
                }
                return false;
            }
        });
    }

    private void fillWinetList() {
        Call<WinetInfoResponse> messages = ApiClient.getWinetApi().getWinets(vestibule.getId());
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<WinetInfoResponse>() {
            @Override
            public void onResponse(Call<WinetInfoResponse> call, Response<WinetInfoResponse> response) {
                Log.e("get vest response", "success = " + response.body().getSuccess());

                for (WinetInfo winetInfo : response.body().getResult()) {
                    winetList.add(new Winet(winetInfo.getId(), winetInfo.getSerNumber(), winetInfo.getType(), vestibule));
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

    public void createNewWinet(View view) {
        WinetCreateDialog winetCreateDialog = new WinetCreateDialog(this, adapter,  winetList, vestibule);
        winetCreateDialog.show();
    }
}
