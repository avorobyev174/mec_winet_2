package com.avorobyev174.mec_winet.classes.apartment;

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

public class ApartmentAdapter extends ArrayAdapter<Apartment> {
    private LayoutInflater inflater;
    private List<Apartment> apartmentList;
    private List<Apartment> removeApartmentList;
    private List<Apartment> addApartmentList;
    private Context context;
    private ApartmentAdapter apartmentAdapter;

    public ApartmentAdapter(@NonNull Context context, int resource, List<Apartment> apartmentList, LayoutInflater inflater, List<Apartment> removeApartmentList, List<Apartment> addApartmentList) {
        super(context, resource, apartmentList);
        this.inflater = inflater;
        this.apartmentList = apartmentList;
        this.context = context;
        this.apartmentAdapter = this;
        this.removeApartmentList = removeApartmentList;
        this.addApartmentList = addApartmentList;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Apartment apartment = apartmentList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);
        final ViewHolder viewHolder = new ViewHolder(convertView, apartment);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView apartmentTitle;
        public ImageButton deleteApartmentButton;
        private Apartment apartment;

        private ViewHolder(View rootView, Apartment apartment) {
            this.apartment = apartment;

            apartmentTitle = rootView.findViewById(R.id.list_item_title);
            deleteApartmentButton = rootView.findViewById(R.id.deleteItemButton);

            apartmentTitle.setText(this.apartment.getFullNumber());

            deleteApartmentButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteApartmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeApartmentList.add(apartment);
                    apartmentList.remove(apartment);
                    addApartmentList.remove(apartment);
                    apartmentAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
