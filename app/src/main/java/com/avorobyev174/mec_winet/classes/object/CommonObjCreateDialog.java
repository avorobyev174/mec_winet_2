package com.avorobyev174.mec_winet.classes.object;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.floor.api.FloorParams;
import com.avorobyev174.mec_winet.classes.floor.api.FloorResponseWithParams;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.object.api.CommonParams;
import com.avorobyev174.mec_winet.classes.object.api.CommonResponseWithParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommonObjCreateDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommonObjCreateDialog extends Dialog {
    private Activity activity;
    private Button createObjButton, cancelCreateObjButton;
    private EditText objName;
    private CommonObjAdapter objAdapter;
    private List<CommonObj> objList;
    private Entity parent;

    public CommonObjCreateDialog(@NonNull Activity activity, CommonObjAdapter objAdapter, List<CommonObj> objList, Entity parent) {
        super(activity);
        this.activity = activity;
        this.objAdapter = objAdapter;
        this.objList = objList;
        this.parent = parent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.obj_create_dialog_activity);

        createObjButton = findViewById(R.id.createObjDialogButton);
        cancelCreateObjButton = findViewById(R.id.cancelCreateObjDialogButton);

        objName = findViewById(R.id.nameObjCreateDialog);

        cancelCreateObjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        createObjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRestrictions();
                createObj();
            }
        });
    }

    private void setRestrictions() {
        String name = objName.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Введите название объекта", Toast.LENGTH_SHORT).show();
            return;
        }

        for (CommonObj commonObj : objList) {
            if (name.equals(commonObj.getName())) {
                Toast.makeText(getContext(), "Объект \"" + name + "\" уже существует в этом списке", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void createObj() {
        int parentId = parent instanceof House ? ((House)parent).getId() : ((CommonObj)parent).getId();

        Call<CommonResponseWithParams> createObjCall = ApiClient.getCommonApi().createCommonObject(parentId, objName.getText().toString());

        createObjCall.enqueue(new Callback<CommonResponseWithParams>() {
            @Override
            public void onResponse(Call<CommonResponseWithParams> call, Response<CommonResponseWithParams> response) {

                Log.e("create obj response", "success = " + response.body().getSuccess());
                int floorId = Integer.parseInt(response.body().getResult());
                CommonParams commonParams = response.body().getParams();
                Log.e("create floor response", "params floor number = " + commonParams.getFloorNumber());
                objList.add(new Floor(floorId, Integer.parseInt(commonParams.getFloorNumber()), section));

                Toast.makeText(getContext(), "Этаж \"" + commonParams.getFloorNumber() + "\" добавлен в список", Toast.LENGTH_SHORT).show();

                objAdapter.notifyDataSetChanged();
                dismiss();
            }

            @Override
            public void onFailure(Call<CommonResponseWithParams> call, Throwable t) {
                Log.e("response", "failure " + t);
            }
        });
    }
}