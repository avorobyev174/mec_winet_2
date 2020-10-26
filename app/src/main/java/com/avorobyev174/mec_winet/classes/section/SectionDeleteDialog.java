package com.avorobyev174.mec_winet.classes.section;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionDeleteDialog extends Dialog {
    public Activity activity;
    public Button deleteSectionButton, cancelDeleteSectionButton;
    public TextView title;
    private Section section;
    private SectionAdapter sectionAdapter;
    private List<Section> sectionList;

    public SectionDeleteDialog(@NonNull Activity activity, Section section, SectionAdapter sectionAdapter, List<Section> sectionList) {
        super(activity);
        this.activity = activity;
        this.section = section;
        this.sectionAdapter = sectionAdapter;
        this.sectionList = sectionList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.section_delete_dialog_activity);

        deleteSectionButton = findViewById(R.id.deleteSectionDialogButton);
        cancelDeleteSectionButton = findViewById(R.id.cancelDeleteSectionDialogButton);

        title = findViewById(R.id.deleteSectionTitle);
        title.setText("Вы хотите удалить подьезд \"" + section.getNumber() + "\" ?");

        cancelDeleteSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        deleteSectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Call<SimpleResponse> responseCall = ApiClient.getSectionApi().deleteSection("section", section.getId());

                responseCall.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                        Log.e("delete", "sql " + response.body().getSql());
                        Log.e("delete", "result " + response.body().getSuccess());

                        sectionList.remove(section);
                        Toast.makeText(getContext(), "Подьезд \"" + section.getNumber() + "\" удален из списка", Toast.LENGTH_SHORT).show();
                        sectionAdapter.notifyDataSetChanged();
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
