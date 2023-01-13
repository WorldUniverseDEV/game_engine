package com.soapboxrace.jaxb.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetupCarTrans", propOrder = {
    "baseCar",
    "physicsProfileHash",
    "carClassHash",
    "performanceParts",
    "skillModParts",
    "visualParts"
})

public class SetupCarTrans {
    protected int baseCar;
    protected int physicsProfileHash;
    protected int carClassHash;
    protected int id;
    protected List<Integer> performanceParts;
    protected List<Integer> skillModParts;
    protected List<Integer> visualParts;
 
    public int getBaseCar() {
        return baseCar;
    }

    public void setBaseCar(int value) {
        this.baseCar = value;
    }
 
    public int getCarClassHash() {
        return carClassHash;
    }

    public void setCarClassHash(int value) {
        this.carClassHash = value;
    }
 
    public List<Integer> getPerformanceParts() {
        return performanceParts;
    }
 
    public void setPerformanceParts(List<Integer> value) {
        this.performanceParts = value;
    }

    public int getPhysicsProfileHash() {
        return physicsProfileHash;
    }
 
    public void setPhysicsProfileHash(int value) {
        this.physicsProfileHash = value;
    }
 
    public List<Integer> getSkillModParts() {
        return skillModParts;
    }

    public void setSkillModParts(List<Integer> value) {
        this.skillModParts = value;
    }
 
    public List<Integer> getVisualParts() {
        return visualParts;
    }
 
    public void setVisualParts(List<Integer> value) {
        this.visualParts = value;
    }
}
 