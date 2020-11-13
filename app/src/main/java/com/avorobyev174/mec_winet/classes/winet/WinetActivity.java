package com.avorobyev174.mec_winet.classes.winet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.InfoBar;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleActivity;
import com.avorobyev174.mec_winet.classes.winetData.WinetDataActivity;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WinetActivity extends Entity {
    private ListView winetListView;
    private WinetAdapter adapter;
    private List<Winet> winetList;
    private ProgressBar progressBar;
    private Vestibule vestibule;
    private WinetCreateDialog winetCreateDialog;

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
        progressBar = findViewById(R.id.progressBar);

        InfoBar.init(this);
        InfoBar.changeInfoBarData(vestibule);

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

                //Toast.makeText(getApplicationContext(), "Загружено " + response.body().getResult().size() + " вайнет", Toast.LENGTH_SHORT).show();
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
        winetCreateDialog = new WinetCreateDialog(this, adapter, winetList, vestibule);
        winetCreateDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                winetCreateDialog.setSerNumber(intentResult.getContents());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
