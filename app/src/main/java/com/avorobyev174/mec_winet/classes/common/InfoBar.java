package com.avorobyev174.mec_winet.classes.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avorobyev174.mec_winet.R;
import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.meter.Meter;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winetData.WinetData;

import java.io.Serializable;

public class InfoBar {
    private static TextView houseTextView;
    private static TextView sectionTextView;
    private static TextView floorTextView;
    private static TextView vestibuleTextView;
    private static TextView winetTextView;
    private static TextView apartmentTextView;
    private static TextView meterTextView;
    private static Activity activity;

    public static void init(Activity entityActivity) {
        activity = entityActivity;
        houseTextView = activity.findViewById(R.id.houseTextView);
        sectionTextView = activity.findViewById(R.id.sectionTextView);
        floorTextView = activity.findViewById(R.id.floorTextView);
        vestibuleTextView = activity.findViewById(R.id.vestibuleTextView);
        winetTextView = activity.findViewById(R.id.winetTextView);
        apartmentTextView = activity.findViewById(R.id.apartmentTextView);
        meterTextView = activity.findViewById(R.id.meterTextView);
    }

    private static void setOnClickListenerForTextView(TextView textView, Serializable moveEntity) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Utils.getActivityClassByEntity(moveEntity));
                if (moveEntity != null) {
                    if (moveEntity.getClass().equals(WinetData.class)) {
                        intent.putExtra(Winet.class.getSimpleName(), ((WinetData)moveEntity).getWinet());
                    } else  {
                        intent.putExtra(moveEntity.getClass().getSimpleName(), moveEntity);
                    }
                }
                activity.startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void changeInfoBarData(Serializable entity) {
        changeInfoBarTextViewsVisibility(entity);
        if (entity == null) {
            houseTextView.setText("Список домов");
        } else if (entity.getClass().equals(House.class)) {
            //список подьездов
            House house = (House) entity;

            houseTextView.setText(house.getFullStreetName());
            setOnClickListenerForTextView(houseTextView, null);
        } else if (entity.getClass().equals(Section.class)) {
            //список этажей
            Section section = (Section) entity;

            houseTextView.setText(section.getHouse().getFullStreetName() + " → ");
            setOnClickListenerForTextView(houseTextView, null);

            sectionTextView.setText(section.getShortNumber());
            setOnClickListenerForTextView(sectionTextView, section.getHouse());
        } else if (entity.getClass().equals(Floor.class)) {
            //список тамбуров
            Floor floor = (Floor) entity;

            houseTextView.setText(floor.getSection().getHouse().getFullStreetName() + " → ");
            setOnClickListenerForTextView(houseTextView, null);

            sectionTextView.setText(floor.getSection().getShortNumber() + " → ");
            setOnClickListenerForTextView(sectionTextView, floor.getSection().getHouse());

            floorTextView.setText(floor.getShortNumber());
            setOnClickListenerForTextView(floorTextView, floor.getSection());
        } else if (entity.getClass().equals(Vestibule.class)) {
            //список вайнет
            Vestibule vestibule = (Vestibule) entity;

            houseTextView.setText(vestibule.getFloor().getSection().getHouse().getFullStreetName() + " → ");
            setOnClickListenerForTextView(houseTextView, null);

            sectionTextView.setText(vestibule.getFloor().getSection().getShortNumber() + " → ");
            setOnClickListenerForTextView(sectionTextView, vestibule.getFloor().getSection().getHouse());

            floorTextView.setText(vestibule.getFloor().getShortNumber()  + " → ");
            setOnClickListenerForTextView(floorTextView, vestibule.getFloor().getSection());

            vestibuleTextView.setText(vestibule.getShortNumber());
            setOnClickListenerForTextView(vestibuleTextView, vestibule.getFloor());
        } else if (entity.getClass().equals(Winet.class)) {
            //вайнет инфо
            Winet winet = (Winet) entity;

            houseTextView.setText(winet.getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → ");
            setOnClickListenerForTextView(houseTextView, null);

            sectionTextView.setText(winet.getVestibule().getFloor().getSection().getShortNumber() + " → ");
            setOnClickListenerForTextView(sectionTextView, winet.getVestibule().getFloor().getSection().getHouse());

            floorTextView.setText(winet.getVestibule().getFloor().getShortNumber()  + " → ");
            setOnClickListenerForTextView(floorTextView, winet.getVestibule().getFloor().getSection());

            vestibuleTextView.setText(winet.getVestibule().getShortNumber() + " → ");
            setOnClickListenerForTextView(vestibuleTextView, winet.getVestibule().getFloor());

            winetTextView.setText(winet.getSerNumber());
            setOnClickListenerForTextView(winetTextView, winet.getVestibule());
        } else if (entity.getClass().equals(Apartment.class)) {
            //апартмент инфо
            Apartment apartment = (Apartment) entity;

            houseTextView.setText(apartment.getWinet().getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → ");
            setOnClickListenerForTextView(houseTextView, null);

            sectionTextView.setText(apartment.getWinet().getVestibule().getFloor().getSection().getShortNumber() + " → ");
            setOnClickListenerForTextView(sectionTextView, apartment.getWinet().getVestibule().getFloor().getSection().getHouse());

            floorTextView.setText(apartment.getWinet().getVestibule().getFloor().getShortNumber()  + " → ");
            setOnClickListenerForTextView(floorTextView, apartment.getWinet().getVestibule().getFloor().getSection());

            vestibuleTextView.setText(apartment.getWinet().getVestibule().getShortNumber() + " → ");
            setOnClickListenerForTextView(vestibuleTextView, apartment.getWinet().getVestibule().getFloor());

            winetTextView.setText(apartment.getWinet().getSerNumber() + " → ");
            setOnClickListenerForTextView(winetTextView, apartment.getWinet().getVestibule());

            apartmentTextView.setText(apartment.getFullApartmentDesc());
            setOnClickListenerForTextView(apartmentTextView, apartment.getWinet().getWinetData());
        } else if (entity.getClass().equals(Meter.class)) {
            //метер инфо
            Meter meter = (Meter) entity;

            houseTextView.setText(meter.getApartment().getWinet().getVestibule().getFloor().getSection().getHouse().getFullStreetName() + " → ");
            setOnClickListenerForTextView(houseTextView, null);

            sectionTextView.setText(meter.getApartment().getWinet().getVestibule().getFloor().getSection().getShortNumber() + " → ");
            setOnClickListenerForTextView(sectionTextView, meter.getApartment().getWinet().getVestibule().getFloor().getSection().getHouse());

            floorTextView.setText(meter.getApartment().getWinet().getVestibule().getFloor().getShortNumber()  + " → ");
            setOnClickListenerForTextView(floorTextView, meter.getApartment().getWinet().getVestibule().getFloor().getSection());

            vestibuleTextView.setText(meter.getApartment().getWinet().getVestibule().getShortNumber() + " → ");
            setOnClickListenerForTextView(vestibuleTextView, meter.getApartment().getWinet().getVestibule().getFloor());

            winetTextView.setText(meter.getApartment().getWinet().getSerNumber() + " → ");
            setOnClickListenerForTextView(winetTextView, meter.getApartment().getWinet().getVestibule());

            apartmentTextView.setText(meter.getApartment().getFullApartmentDesc()  + " → ");
            setOnClickListenerForTextView(apartmentTextView, meter.getApartment().getWinet().getWinetData());

            meterTextView.setText(meter.getSerNumber());
            setOnClickListenerForTextView(meterTextView, meter.getApartment());
        }
    }

    private static void changeInfoBarTextViewsVisibility(Serializable entity) {
        if (entity == null) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.GONE);
            floorTextView.setVisibility(View.GONE);
            vestibuleTextView.setVisibility(View.GONE);
            winetTextView.setVisibility(View.GONE);
            apartmentTextView.setVisibility(View.GONE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(House.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.GONE);
            floorTextView.setVisibility(View.GONE);
            vestibuleTextView.setVisibility(View.GONE);
            winetTextView.setVisibility(View.GONE);
            apartmentTextView.setVisibility(View.GONE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(Section.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.VISIBLE);
            floorTextView.setVisibility(View.GONE);
            vestibuleTextView.setVisibility(View.GONE);
            winetTextView.setVisibility(View.GONE);
            apartmentTextView.setVisibility(View.GONE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(Floor.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.VISIBLE);
            floorTextView.setVisibility(View.VISIBLE);
            vestibuleTextView.setVisibility(View.GONE);
            winetTextView.setVisibility(View.GONE);
            apartmentTextView.setVisibility(View.GONE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(Vestibule.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.VISIBLE);
            floorTextView.setVisibility(View.VISIBLE);
            vestibuleTextView.setVisibility(View.VISIBLE);
            winetTextView.setVisibility(View.GONE);
            apartmentTextView.setVisibility(View.GONE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(Winet.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.VISIBLE);
            floorTextView.setVisibility(View.VISIBLE);
            vestibuleTextView.setVisibility(View.VISIBLE);
            winetTextView.setVisibility(View.VISIBLE);
            apartmentTextView.setVisibility(View.GONE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(Apartment.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.VISIBLE);
            floorTextView.setVisibility(View.VISIBLE);
            vestibuleTextView.setVisibility(View.VISIBLE);
            winetTextView.setVisibility(View.VISIBLE);
            apartmentTextView.setVisibility(View.VISIBLE);
            meterTextView.setVisibility(View.GONE);
        } else if (entity.getClass().equals(Meter.class)) {
            houseTextView.setVisibility(View.VISIBLE);
            sectionTextView.setVisibility(View.VISIBLE);
            floorTextView.setVisibility(View.VISIBLE);
            vestibuleTextView.setVisibility(View.VISIBLE);
            winetTextView.setVisibility(View.VISIBLE);
            apartmentTextView.setVisibility(View.VISIBLE);
            meterTextView.setVisibility(View.VISIBLE);
        }
    }
}
