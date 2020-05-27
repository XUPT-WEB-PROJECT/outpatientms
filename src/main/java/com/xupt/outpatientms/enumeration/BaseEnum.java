package com.xupt.outpatientms.enumeration;

import java.util.Objects;

public interface BaseEnum {

    int value();

    default String desc() { return String.valueOf(value()); }

    static <T extends BaseEnum> T fromValue(Class<T> enumType, int value){
        for (T object : enumType.getEnumConstants()){
            if (Objects.equals(value,object.value())){
                return object;
            }
        }
        return null;
    }

}
