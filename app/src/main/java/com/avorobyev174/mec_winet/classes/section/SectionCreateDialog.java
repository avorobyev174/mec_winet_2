package com.avorobyev174.mec_winet.classes.section;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.house.House;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionCreateDialog extends Dialog {
    public Activity activity;
    public Button createSectionButton, cancelCreateSectionButton;
    public EditText sectionNumber;
    private SectionAdapter sectionAdapter;
    private List<Section> sectionList;
    private House house;

    public SectionCreateDialog(@NonNull Activity activity, SectionAdapter sectionAdapter, List<Section> sectionList, House house) {
        super(activity);
        this.activity = activity;
        this.sectionAdapter = sectionAdapter;
        this.sectionList = sectionList;
        this.house = house;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.section_create_dialog_activity);

        createSectionButton = findViewById(R.id.createSectionButton);
        cancelCreateSectionButton = findViewById(R.id.cancelCreateSectionButton);

        sectionNumber = findViewById(R.id.sectionNumber);

        cancelCreateSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        createSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String secNumber = sectionNumber.getText().toString();
                for (Section section : sectionList) {
                    if (Integer.parseInt(secNumber) == section.getNumber()) {
                        Toast.makeText(getContext(), "Подьезд \"" + secNumber + "\" уже существует в этом доме", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Call<SectionResponseWithParams> createSectionCall = ApiClient.getSectionApi().createSection(secNumber, house.getId());

                createSectionCall.enqueue(new Callback<SectionResponseWithParams>() {
                    @Override
                    public void onResponse(Call<SectionResponseWithParams> call, Response<SectionResponseWithParams> response) {

                        Log.e("create section response", "success = " + response.body().getSuccess());
                        int sectionId = Integer.parseInt(response.body().getResult());
                        SectionParams sectionParams = response.body().getParams();
                        Log.e("create section response", "params sec number = " + sectionParams.getSectionNumber());
                        sectionList.add(new Section(sectionId, Integer.parseInt(sectionParams.getSectionNumber()), house));

                        Toast.makeText(getContext(), "Подьезд \"" + sectionParams.getSectionNumber() + "\" добавлен в список", Toast.LENGTH_SHORT).show();

                        sectionAdapter.notifyDataSetChanged();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<SectionResponseWithParams> call, Throwable t) {
                        Log.e("response", "failure " + t);
                    }
                });
            }
        });
    }

}
