package com.avorobyev174.mec_winet.classes.house;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.DialogFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseCreateDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseCreateDialogFragment extends DialogFragment {
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    private static FragmentManager fragmentManager;
    private HouseAdapter adapter;
    private List<House> houseList;
    private fragmentShowListner fragmentShowListner;

    public HouseCreateDialogFragment() {
        // Required empty public constructor
    }

    public static DialogFragment newInstance() {
        return new HouseCreateDialogFragment();
    }

    @Override
    public void showEntityCreateDialog() {
        //HouseCreateDialog houseCreateDialog = new HouseCreateDialog(getActivity(), adapter,  houseList);
        //houseCreateDialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

       // fragmentShowListner = (fragmentShowListner) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      //  fragmentShowListner.onChangeEntity(null);
        View view = inflater.inflate(R.layout.house_create_dialog_fragment, container, false);


        return view;
    }
}