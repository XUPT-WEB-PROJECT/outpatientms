package com.xupt.outpatientms.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecordStatusEnum implements BaseEnum{

    //0：未付款，1：待就诊，2：已就诊，3：已失效，4：已完成，5：已删除
    RECORD_UNPAID(0,"未付款"),
    RECORD_UNRESOLVED(1,"待就诊"),
    RECORD_RESOLVED(2,"已就诊"),
    RECORD_INVALID(3,"已失效"),
    RECORD_COMPLETED(4,"已完成"),//评分后
    RECORD_DELETED(5,"已删除"),
    ;

    private int value;
    private String desc;

    RecordStatusEnum(int value, String desc){
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
        for (RecordStatusEnum g : RecordStatusEnum.values()){
            if (g.value == value){
                return g.desc;
            }
        }
        return null;
    }
}
