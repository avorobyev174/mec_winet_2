package com.avorobyev174.mec_winet.classes.winet;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.api.SimpleResponse;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinetDeleteDialog extends Dialog {
    public Activity activity;
    public Button confirmDeleteWinetButton, cancelDeleteWinetButton;
    public TextView winetDeleteTitle;
    private Winet winet;
    private WinetAdapter winetAdapter;
    private List<Winet> winetList;

    public WinetDeleteDialog(@NonNull Activity activity, Winet winet, WinetAdapter winetAdapter, List<Winet> winetList) {
        super(activity);
        this.activity = activity;
        this.winet = winet;
        this.winetAdapter = winetAdapter;
        this.winetList = winetList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog_activity);

        confirmDeleteWinetButton = findViewById(R.id.confirmDeleteDialogButton);
        cancelDeleteWinetButton = findViewById(R.id.cancelDeleteDialogButton);

        winetDeleteTitle = findViewById(R.id.deleteDialogTitle);
        winetDeleteTitle.setText("Вы хотите удалить вайнет \"" + winet.getSerNumber() + "\" ?");

        cancelDeleteWinetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmDeleteWinetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getWinetApi().deleteWinet("winet", winet.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        winetList.remove(winet);
                        Toast.makeText(getContext(), "Вайнет \"" + winet.getSerNumber() + "\" удален из списка", Toast.LENGTH_SHORT).show();
                        winetAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Log.e("response", "failure " + t);
                        dismiss();
                    }
                });
            }
        });
    }

}
