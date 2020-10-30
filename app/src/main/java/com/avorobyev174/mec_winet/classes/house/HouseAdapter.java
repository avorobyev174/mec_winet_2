package com.avorobyev174.mec_winet.classes.house;

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

import java.util.List;

public class HouseAdapter extends ArrayAdapter<House> {
    private LayoutInflater inflater;
    private List<House> houseList;
    private Context context;
    private HouseAdapter houseAdapter;


    public HouseAdapter(@NonNull Context context, int resource, List<House> houseList, LayoutInflater inflater) {
        super(context, resource, houseList);
        this.inflater = inflater;
        this.houseList = houseList;
        this.context = context;
        this.houseAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        House house = houseList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);

        final ViewHolder viewHolder = new ViewHolder(convertView, house);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView houseTitle;
        public ImageButton deleteHouseButton;
        private House house;
        //private HouseAdapter houseAdapter;

        private ViewHolder(View rootView, House house) {
            this.house = house;

            houseTitle = rootView.findViewById(R.id.list_item_title);
            deleteHouseButton = rootView.findViewById(R.id.deleteItemButton);

            houseTitle.setText(house.getFullStreetName());

            deleteHouseButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteHouseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteHouse(view);
                }
            });
        }

        private void deleteHouse(View view) {
            HouseDeleteDialog houseDeleteDialog = new HouseDeleteDialog((Activity) context, house, houseAdapter, houseList);
            houseDeleteDialog.show();
        }
    }
}

