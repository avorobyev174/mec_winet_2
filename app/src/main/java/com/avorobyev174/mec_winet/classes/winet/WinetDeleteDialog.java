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
    public Button confirmDeleteVestibuleButton, cancelDeleteVestibuleButton;
    public TextView vestibuleDeleteTitle;
    private Vestibule vestibule;
    private VestibuleAdapter vestibuleAdapter;
    private List<Vestibule> vestibuleList;

    public WinetDeleteDialog(@NonNull Activity activity, Vestibule vestibule, VestibuleAdapter vestibuleAdapter, List<Vestibule> vestibuleList) {
        super(activity);
        this.activity = activity;
        this.vestibule = vestibule;
        this.vestibuleAdapter = vestibuleAdapter;
        this.vestibuleList = vestibuleList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog_activity);

        confirmDeleteVestibuleButton = findViewById(R.id.confirmDeleteDialogButton);
        cancelDeleteVestibuleButton = findViewById(R.id.cancelDeleteDialogButton);

        vestibuleDeleteTitle = findViewById(R.id.deleteDialogTitle);
        vestibuleDeleteTitle.setText("Вы хотите удалить этаж \"" + vestibule.getNumber() + "\" ?");

        cancelDeleteVestibuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmDeleteVestibuleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getVestibuleApi().deleteVestibule("vestibule", vestibule.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        vestibuleList.remove(vestibule);
                        Toast.makeText(getContext(), "Тамбур \"" + vestibule.getNumber() + "\" удален из списка", Toast.LENGTH_SHORT).show();
                        vestibuleAdapter.notifyDataSetChanged();
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
