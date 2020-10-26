package com.avorobyev174.mec_winet;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;


public class WinetInfoActivity extends AppCompatActivity {
    private House house;
    private Section section;
    private Floor floor;
    private Vestibule vestibule;
    private TextView infoBar;
    private Spinner winetTypeSpinner;
    private EditText serNumberInput;
    private EditText commentInput;
    private Winet winet;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winet_info_activity);
        init();
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    private void init() {
        infoBar = findViewById(R.id.info_bar);

        winetTypeSpinner = findViewById(R.id.winet_type);
        serNumberInput = (EditText)findViewById(R.id.winet_ser_number);
        commentInput = (EditText)findViewById(R.id.winet_comment);

        Bundle arguments = getIntent().getExtras();
        winet = (Winet) arguments.getSerializable(Winet.class.getSimpleName());
        ArrayList<? extends Winet> winetList = arguments.getParcelableArrayList("list");
        Log.e("123", String.valueOf(winetList.size()));
        vestibule = winet.getVestibule();
        floor = winet.getFloor();
        section = winet.getSection();
        house = winet.getHouse();
        infoBar.setText(house.getFullStreetName() + " → " + section.getFullNumber() + " → " + floor.getFullNumber() + " → " + vestibule.getFullNumber());

        adapter = ArrayAdapter.createFromResource(this, R.array.winet_type_array, R.xml.spinner_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        winetTypeSpinner.setAdapter(adapter);

        sharedPreferences = getSharedPreferences(winet.getGuid(), MODE_PRIVATE);

        fillInfoByStorage();
    }

    public void winetSaveInfoOnClick(View view) {
        winet.setType(winetTypeSpinner.getSelectedItem().toString());
        winet.setSerNumber(serNumberInput.getText());
        winet.setComment(commentInput.getText());

//        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
//        final Gson gson = new Gson();
//        String serializedWinet = gson.toJson(winet);
//        sharedPreferencesEditor.putString(winet.getGuid(), serializedWinet);
//        sharedPreferencesEditor.apply();
    }

    private void fillInfoByStorage() {
//        String jsonWinet = sharedPreferences.getString(winet.getGuid(), "-");
//        Log.e("winet-error", jsonWinet);
//        Gson gson = new Gson();
//        try {
//            Winet winet = gson.fromJson(jsonWinet, Winet.class);
//        } catch (IllegalStateException | JsonSyntaxException exception) {}
//
//        }
        String winetType = winet.getType();
        if (!winetType.equals("")) {
            winetTypeSpinner.setSelection(adapter.getPosition(winet.getType()));
        }
        serNumberInput.setText(winet.getSerNumber());
        commentInput.setText(winet.getComment());
    }
}
