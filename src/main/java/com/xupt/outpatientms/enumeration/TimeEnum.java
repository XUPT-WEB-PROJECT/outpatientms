package com.xupt.outpatientms.enumeration;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum  TimeEnum implements BaseEnum{

    TIME_AM(0,"am"),
    TIME_PM(1,"pm")
    ;

    private int value;

    @JsonValue
    private String desc;

    TimeEnum(int value, String desc) {
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
        for (TimeEnum g : TimeEnum.values()){
            if (g.value == value){
                return g.desc;
            }
        }
        return null;
    }
}
