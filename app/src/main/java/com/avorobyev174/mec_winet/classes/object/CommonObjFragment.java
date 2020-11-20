package com.avorobyev174.mec_winet.classes.object;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.api.ApiClient;
import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.common.EntityFragment;
import com.avorobyev174.mec_winet.classes.object.api.CommonInfo;
import com.avorobyev174.mec_winet.classes.object.api.CommonInfoResponse;
import com.avorobyev174.mec_winet.classes.house.House;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommonObjFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommonObjFragment extends EntityFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private CommonObjAdapter adapter;
    private List<CommonObj> objList;
    private EntityFragment.fragmentShowListner fragmentShowListner;
    //private House house;
    private Entity parentObj;
    private int positionItTree = 0;

    public CommonObjFragment() {
        // Required empty public constructor
    }

    public static EntityFragment newInstance(ProgressBar myProgressBar, FragmentManager myFragmentManager) {
        fragmentManager = myFragmentManager;
        progressBar = myProgressBar;

        return new CommonObjFragment();
    }

    @Override
    public EntityFragment getPreviousFragment() {
        return null;
    }

    @Override
    public void showEntityCreateDialog() {
        CommonObjCreateDialog objCreateDialog = new CommonObjCreateDialog(getActivity(), adapter, objList, parentObj);
        objCreateDialog.show();
    }

    @Override
    public Entity getParentEntity() {
        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentShowListner = (fragmentShowListner) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("entity") instanceof House) {
                parentObj = (House) bundle.getSerializable("entity");
            } else {
                parentObj = (CommonObj) bundle.getSerializable("commonObj");
            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.e("Obj", Boolean.toString(house != null));

        //if (house != null) {
        //    fragmentShowListner.onChangeEntity(house);
        //} else {
            fragmentShowListner.onChangeEntity(parentObj);
        //}

        View view = inflater.inflate(R.layout.common_obj_fragment, container, false);
        objList = new ArrayList<>();
        ListView objListView = view.findViewById(R.id.common_obj_list_view);

        adapter = new CommonObjAdapter(view.getContext(), R.layout.simple_list_item_view, objList, getLayoutInflater());
        objListView.setAdapter(adapter);

        fillObjList();

        objListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommonObj commonObj = objList.get(i);
                EntityFragment entityFragment = null;

                entityFragment = CommonObjFragment.newInstance(progressBar, fragmentManager);

                Bundle bundle = new Bundle();
                bundle.putSerializable("commonObj", commonObj);
                entityFragment.setArguments(bundle);

                fragmentManager.beginTransaction().replace(R.id.fragment_container, entityFragment).commitNow();
            }
        });

        return view;
    }

    public void fillObjList() {
        Call<CommonInfoResponse> messages = ApiClient.getCommonApi().getCommonObjects();
        progressBar.setVisibility(ProgressBar.VISIBLE);

        messages.enqueue(new Callback<CommonInfoResponse>() {
            @Override
            public void onResponse(Call<CommonInfoResponse> call, Response<CommonInfoResponse> response) {
                Log.e("get common response", "success = " + response.body().getSuccess());

                for (CommonInfo commonInfo : response.body().getResult()) {
                    objList.add(new CommonObj(commonInfo.getId(), commonInfo.getParentId(), commonInfo.getName()));
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                adapter.notifyDataSetChanged();

                //Toast.makeText(getContext(), "Загружено " + response.body().getResult().size() + " домов", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CommonInfoResponse> call, Throwable t) {
                Log.e("response", "failure " + t);
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}