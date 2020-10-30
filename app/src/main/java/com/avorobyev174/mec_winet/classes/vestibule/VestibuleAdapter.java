package com.avorobyev174.mec_winet.classes.vestibule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.Utils;
import com.avorobyev174.mec_winet.classes.floor.FloorDeleteDialog;

import java.util.ArrayList;
import java.util.List;

public class VestibuleAdapter extends ArrayAdapter<Vestibule> {
    private LayoutInflater inflater;
    private List<Vestibule> vestibuleList = new ArrayList<>();
    private Context context;
    private VestibuleAdapter vestibuleAdapter;


    public VestibuleAdapter(@NonNull Context context, int resource, List<Vestibule> vestibuleList, LayoutInflater inflater) {
        super(context, resource, vestibuleList);
        this.inflater = inflater;
        this.vestibuleList = vestibuleList;
        this.context = context;
        this.vestibuleAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Vestibule vestibule = vestibuleList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);
        final ViewHolder viewHolder = new ViewHolder(convertView, vestibule);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView vestibuleTitle;
        public ImageButton deleteVestibuleButton;
        private Vestibule vestibule;

        private ViewHolder(View rootView, Vestibule vestibule) {
            this.vestibule = vestibule;

            vestibuleTitle = rootView.findViewById(R.id.list_item_title);
            deleteVestibuleButton = rootView.findViewById(R.id.deleteItemButton);

            vestibuleTitle.setText(vestibule.getFullNumber());

            deleteVestibuleButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteVestibuleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteVestibule(view);
                }
            });
        }

        private void deleteVestibule(View view) {
            VestibuleDeleteDialog vestibuleDeleteDialog = new VestibuleDeleteDialog((Activity) context, vestibule, vestibuleAdapter, vestibuleList);
            vestibuleDeleteDialog.show();
        }
    }
}
