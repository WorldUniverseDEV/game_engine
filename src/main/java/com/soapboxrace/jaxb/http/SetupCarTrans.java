package com.soapboxrace.jaxb.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetupCarTrans", propOrder = {
    "baseCar",
    "physicsProfileHash",
    "carClassHash",
    "id",
    "performanceParts",
    "skillModParts",
    "visualParts"
})

public class SetupCarTrans {
    protected int baseCar;
    protected int physicsProfileHash;
    protected int carClassHash;
    protected int id;
    protected ArrayOfPerformancePartTrans performanceParts;
    protected ArrayOfSkillModPartTrans skillModParts;
    protected ArrayOfVisualPartTrans visualParts;
 
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
 
    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }
 
    public ArrayOfPerformancePartTrans getPerformanceParts() {
        return performanceParts;
    }
 
    public void setPerformanceParts(ArrayOfPerformancePartTrans value) {
        this.performanceParts = value;
    }

    public int getPhysicsProfileHash() {
        return physicsProfileHash;
    }
 
    public void setPhysicsProfileHash(int value) {
        this.physicsProfileHash = value;
    }
 
    public ArrayOfSkillModPartTrans getSkillModParts() {
        return skillModParts;
    }

    public void setSkillModParts(ArrayOfSkillModPartTrans value) {
        this.skillModParts = value;
    }
 
    public ArrayOfVisualPartTrans getVisualParts() {
        return visualParts;
    }
 
    public void setVisualParts(ArrayOfVisualPartTrans value) {
        this.visualParts = value;
    }
}
 