package com.avorobyev174.mec_winet.classes.winet;

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

public class WinetAdapter extends ArrayAdapter<Winet> {
    private LayoutInflater inflater;
    private List<Winet> winetList = new ArrayList<>();
    private Context context;
    private WinetAdapter winetAdapter;

    public WinetAdapter(@NonNull Context context, int resource, List<Winet> winetList, LayoutInflater inflater) {
        super(context, resource, winetList);
        this.inflater = inflater;
        this.winetList = winetList;
        this.context = context;
        this.winetAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Winet winet = winetList.get(position);
        convertView = inflater.inflate(R.layout.winet_list_item_view, null, false);
        final ViewHolder viewHolder = new ViewHolder(convertView, winet);
        convertView.setTag(viewHolder);

        return convertView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private class ViewHolder {
        public TextView winetType, winetSerNmber;
        public ImageButton deleteWinetButton;
        private Winet winet;

        private ViewHolder(View rootView, Winet winet) {
            this.winet = winet;

            winetType = rootView.findViewById(R.id.winetTypeItem);
            winetSerNmber = rootView.findViewById(R.id.winetSerNumberItem);
            deleteWinetButton = rootView.findViewById(R.id.winetDeleteItemButton);

            winetType.setText(winet.getType());
            winetSerNmber.setText(winet.getSerNumber());

            deleteWinetButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteWinetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteWinet(view);
                }
            });
        }

        private void deleteWinet(View view) {
            WinetDeleteDialog winetDeleteDialog = new WinetDeleteDialog((Activity) context, winet, winetAdapter, winetList);
            winetDeleteDialog.show();
        }
    }
}
