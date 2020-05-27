package com.xupt.outpatientms.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel implements Serializable {

    private Date createTime;
    private Date updateTime;

}
