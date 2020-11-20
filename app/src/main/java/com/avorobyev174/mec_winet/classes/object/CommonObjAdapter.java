package com.avorobyev174.mec_winet.classes.object;

import android.annotation.SuppressLint;
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

import java.util.List;

public class CommonObjAdapter extends ArrayAdapter<CommonObj> {
    private LayoutInflater inflater;
    private List<CommonObj> objectList;
    private Context context;
    private CommonObjAdapter objectAdapter;


    public CommonObjAdapter(@NonNull Context context, int resource, List<CommonObj> objectList, LayoutInflater inflater) {
        super(context, resource, objectList);
        this.inflater = inflater;
        this.objectList = objectList;
        this.context = context;
        this.objectAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommonObj obj = objectList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);

        final ViewHolder viewHolder = new ViewHolder(convertView, obj);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView objTitle;
        public ImageButton deleteObjButton;
        private CommonObj obj;

        private ViewHolder(View rootView, CommonObj obj) {
            this.obj = obj;

            objTitle = rootView.findViewById(R.id.list_item_title);
            deleteObjButton = rootView.findViewById(R.id.winetDeleteItemButton);

            objTitle.setText(obj.getName());

            deleteObjButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteObjButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteHouse(view);
                }
            });
        }

        private void deleteHouse(View view) {
            ///HouseDeleteDialog houseDeleteDialog = new HouseDeleteDialog((Activity)context, obj, objectAdapter, objectList);
            //houseDeleteDialog.show();
        }
    }
}

