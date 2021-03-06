package com.avorobyev174.mec_winet.classes.apartment;

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

import java.util.List;

public class ApartmentAdapter extends ArrayAdapter<Apartment> {
    private LayoutInflater inflater;
    private List<Apartment> apartmentList;
    private Context context;
    private ApartmentAdapter apartmentAdapter;

    public ApartmentAdapter(@NonNull Context context, int resource, List<Apartment> apartmentList, LayoutInflater inflater) {
        super(context, resource, apartmentList);
        this.inflater = inflater;
        this.apartmentList = apartmentList;
        this.context = context;
        this.apartmentAdapter = this;
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
            deleteApartmentButton = rootView.findViewById(R.id.winetDeleteItemButton);

            apartmentTitle.setText(this.apartment.getFullApartmentDesc());

            deleteApartmentButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteApartmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteApartment(view);
                }
            });
        }

        private void deleteApartment(View view) {
            ApartmentDeleteDialog apartmentDeleteDialog = new ApartmentDeleteDialog((Activity) context, apartment, apartmentAdapter, apartmentList);
            apartmentDeleteDialog.show();
        }
    }
}
