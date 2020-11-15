package com.avorobyev174.mec_winet.classes.winetData;

import android.text.BoringLayout;
import android.text.Editable;
import android.util.Log;

import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winet.Winet;

import java.io.Serializable;
import java.util.UUID;

public class WinetData extends Entity implements Serializable {
    private Winet winet;

    public WinetData(Winet winet) {
        this.winet = winet;
    }

    public Winet getWinet() {
        return winet;
    }

}
