package com.avorobyev174.mec_winet.classes.meter;

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
import com.avorobyev174.mec_winet.classes.apartment.ApartmentDeleteDialog;
import com.avorobyev174.mec_winet.classes.common.Utils;

import java.util.List;

public class MeterAdapter extends ArrayAdapter<Meter> {
    private LayoutInflater inflater;
    private List<Meter> meterList;
    private Context context;
    private MeterAdapter meterAdapter;

    public MeterAdapter(@NonNull Context context, int resource, List<Meter> meterList, LayoutInflater inflater) {
        super(context, resource, meterList);
        this.inflater = inflater;
        this.meterList = meterList;
        this.context = context;
        this.meterAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Meter meter = meterList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);
        final ViewHolder viewHolder = new ViewHolder(convertView, meter);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView meterTitle;
        public ImageButton deleteMeterButton;
        private Meter meter;

        private ViewHolder(View rootView, Meter meter) {
            this.meter = meter;

            meterTitle = rootView.findViewById(R.id.list_item_title);
            deleteMeterButton = rootView.findViewById(R.id.deleteItemButton);

            meterTitle.setText(this.meter.getSerNumber());

            deleteMeterButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteMeterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteMeter(view);
                }
            });
        }

        private void deleteMeter(View view) {
            MeterDeleteDialog meterDeleteDialog = new MeterDeleteDialog((Activity) context, meter, meterAdapter, meterList);
            meterDeleteDialog.show();
        }
    }
}
