package com.avorobyev174.mec_winet.classes.section;

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
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.house.HouseDeleteDialog;

import java.util.List;

public class SectionAdapter extends ArrayAdapter<Section> {
    private LayoutInflater inflater;
    private List<Section> sectionList;
    private Context context;
    private SectionAdapter sectionAdapter;

    public SectionAdapter(@NonNull Context context, int resource, List<Section> sectionList, LayoutInflater inflater) {
        super(context, resource, sectionList);
        this.inflater = inflater;
        this.sectionList = sectionList;
        this.context = context;
        this.sectionAdapter = this;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Section section = sectionList.get(position);
        convertView = inflater.inflate(R.layout.simple_list_item_view, null, false);

        final ViewHolder viewHolder = new ViewHolder(convertView, section);
        convertView.setTag(viewHolder);

        return convertView;
    }

    private class ViewHolder {
        public TextView sectionTitle;
        public ImageButton deleteSectionButton;
        private Section section;

        private ViewHolder(View rootView, Section section) {
            this.section = section;

            sectionTitle = rootView.findViewById(R.id.list_item_title);
            deleteSectionButton = rootView.findViewById(R.id.deleteItemButton);

            sectionTitle.setText(section.getFullNumber());

            deleteSectionButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return Utils.changeRemoveButtonColor(view, motionEvent, getContext());
                }
            });

            deleteSectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteSection(view);
                }
            });
        }

        private void deleteSection(View view) {
            SectionDeleteDialog sectionDeleteDialog = new SectionDeleteDialog((Activity) context, section, sectionAdapter, sectionList);
            sectionDeleteDialog.show();
        }
    }
}
