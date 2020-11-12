package com.avorobyev174.mec_winet.classes.winetData;

import android.text.BoringLayout;
import android.text.Editable;
import android.util.Log;

import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winet.Winet;

import java.io.Serializable;
import java.util.UUID;

public class WinetData implements Serializable {
    private Winet winet;

    public WinetData(Winet winet) {
        this.winet = winet;
    }

    public Winet getWinet() {
        Log.e("winet", Boolean.toString(winet!=null));
        Log.e("winet", Boolean.toString(winet.getVestibule()!=null));
        return winet;
    }
}
