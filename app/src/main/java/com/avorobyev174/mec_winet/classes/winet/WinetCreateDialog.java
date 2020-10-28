package com.avorobyev174.mec_winet.classes.winet;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleParams;
import com.avorobyev174.mec_winet.classes.vestibule.VestibuleResponseWithParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinetCreateDialog extends Dialog {
    public Activity activity;
    public Button confirmCreateWinetButton, cancelCreateWinetButton;
    public EditText serNumber;
    private WinetAdapter winetAdapter;
    private List<Winet> winetList;
    private Vestibule vestibule;
    private Spinner type;

    public WinetCreateDialog(@NonNull Activity activity, WinetAdapter winetAdapter, List<Winet> winetList, Vestibule vestibule) {
        super(activity);
        this.activity = activity;
        this.winetAdapter = winetAdapter;
        this.winetList = winetList;
        this.vestibule = vestibule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.winet_create_dialog_activity);

        confirmCreateWinetButton = findViewById(R.id.confirmCreateWinetButton);
        cancelCreateWinetButton = findViewById(R.id.cancelCreateWinetButton);
        type = findViewById(R.id.winetTypeCreateDialog);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.winet_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        type.setAdapter(adapter);
        serNumber = findViewById(R.id.winetSerNumberCreateDialog);

        cancelCreateWinetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        confirmCreateWinetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String winetSerNumber =  serNumber.getText().toString();
                String winetType = type.getSelectedItem().toString();
                for (Winet winet : winetList) {
                    if (winetSerNumber.equals(winet.getSerNumber()) && winetType.equals(winet.getType())) {
                        Toast.makeText(getContext(), "Вайнет \"" + winetSerNumber +  "\" с типом \"" + winetType +  "\" уже существует в этом тамбуре", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Call<WinetResponseWithParams> createFloorCall = ApiClient.getWinetApi().createWinet(winetType, winetSerNumber, vestibule.getId());

                createFloorCall.enqueue(new Callback<WinetResponseWithParams>() {
                    @Override
                    public void onResponse(Call<WinetResponseWithParams> call, Response<WinetResponseWithParams> response) {

                        Log.e("create winet response", "success = " + response.body().getSuccess());
                        int winetId = Integer.parseInt(response.body().getResult());
                        WinetParams winetParams = response.body().getParams();
                        Log.e("create winet response", "params winet number = " + winetParams.getSerNumber());
                        winetList.add(new Winet(winetId, winetParams.getType(), winetParams.getSerNumber(), vestibule));

                        Toast.makeText(getContext(), "Вайнет \"" + winetParams.getSerNumber()+  "\" с типом \"" + winetParams.getType() +  "\" добавлен в список", Toast.LENGTH_SHORT).show();

                        winetAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<WinetResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}
