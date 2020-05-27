package com.xupt.outpatientms.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderEnum implements BaseEnum{

    MALE(0,"男"), FEMALE(1,"女"), SECRETE(2,"保密");

    private int value;

    @JsonValue
    private String desc;

    GenderEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String desc() {
        return desc;
    }


    @JsonCreator
    public static String fromValue(int value){
        for (GenderEnum g : GenderEnum.values()){
            if (g.value == value){
                return g.desc;
            }
        }
        return null;
    }
}
