package com.avorobyev174.mec_winet.classes.floor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.common.Utils;

import java.util.ArrayList;
import java.util.List;

public class FloorAdapter extends ArrayAdapter<Floor> {
    private LayoutInflater inflater;
    private List<Floor> floorList = new ArrayList<>();
    private Context context;
    private FloorAdapter floorAdapter;

    public FloorAdapter(@NonNull Context context, int resource, List<Floor> floorList, LayoutInflater inflater) {
        super(context, resource, floorList);
        this.inflater = inflater;
        this.floorList = floorList;
        this.context = context;
        this.floorAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Floor floor = floorList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);
        final ViewHolder viewHolder = new ViewHolder(convertView, floor);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView floorTitle;
        public ImageButton deleteFloorButton;
        private Floor floor;

        private ViewHolder(View rootView, Floor floor) {
            this.floor = floor;

            floorTitle = rootView.findViewById(R.id.list_item_title);
            deleteFloorButton = rootView.findViewById(R.id.winetDeleteItemButton);

            floorTitle.setText(floor.getFullNumber());

            deleteFloorButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteFloorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteSection(view);
                }
            });
        }

        private void deleteSection(View view) {
            FloorDeleteDialog floorDeleteDialog = new FloorDeleteDialog((Activity) context, floor, floorAdapter, floorList);
            floorDeleteDialog.show();
        }
    }
}
