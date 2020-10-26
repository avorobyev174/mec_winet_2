package com.avorobyev174.mec_winet.classes.winet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;

import java.util.ArrayList;
import java.util.List;

public class WinetAdapter extends ArrayAdapter<Winet> {
    private LayoutInflater inflater;
    private List<Winet> winetList = new ArrayList<>();
    private Context context;

    public WinetAdapter(@NonNull Context context, int resource, List<Winet> winetList, LayoutInflater inflater) {
        super(context, resource, winetList);
        this.inflater = inflater;
        this.winetList = winetList;
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Winet winet = winetList.get(position);
        convertView = inflater.inflate(R.layout.winet_view, null, false);
        final ViewHolder viewHolder = new ViewHolder(convertView, winet);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView winetTypeTV;
        public TextView winetSerNumberTV;

        private ViewHolder(View rootView, Winet winet) {
            winetTypeTV = rootView.findViewById(R.id.winet_type_tv);
            winetSerNumberTV = rootView.findViewById(R.id.winet_ser_number_tv);
            winetTypeTV.setText(winet.getType().toString());
            if (winet.getSerNumber().equals("0")) {
                winetSerNumberTV.setText("отсутствует");
            } else {
                winetSerNumberTV.setText(String.valueOf(winet.getSerNumber()));
            }
        }
    }
}
