package com.avorobyev174.mec_winet.classes.winet;

import android.text.Editable;

import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.section.Section;
import com.avorobyev174.mec_winet.classes.vestibule.Vestibule;
import com.avorobyev174.mec_winet.classes.winetData.WinetData;

import java.io.Serializable;
import java.util.UUID;

public class Winet extends Entity implements Serializable {

    public void setSerNumber(Editable serNumber) {
        this.serNumber = serNumber.toString();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setComment(Editable comment) {
        this.comment = comment.toString();
    }

    private int id;
    private String serNumber;
    private Vestibule vestibule;
    private String type;
    private String comment;
    private String winetX;
    private String winetY;

    public String getGuid() {
        return guid;
    }

    private String guid;

    public String getSerNumber() {
        return serNumber;
    }

    public WinetData getWinetData() {
        return new WinetData(this);
    }

    public Vestibule getVestibule() {
        return vestibule;
    }

    public String getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {
        return id;
    }

    public String getTypeAndSerNumber() {
        return getType() + "    " + getSerNumber();
    }

    public String getWinetX() {
        return winetX;
    }

    public void setWinetX(String winetX) {
        this.winetX = winetX;
    }

    public String getWinetY() {
        return winetY;
    }

    public void setWinetY(String winetY) {
        this.winetY = winetY;
    }

    public Winet(int id, String type, String serNumber, Vestibule vestibule) {
        this.id = id;
        this.serNumber = serNumber;
        this.type = type;
        this.vestibule = vestibule;
        this.guid = UUID.randomUUID().toString();
    }

    @Override
    public Entity getParent() {
        return vestibule;
    }
}
