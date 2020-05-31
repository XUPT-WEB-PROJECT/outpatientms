package com.xupt.outpatientms.bean;

import com.xupt.outpatientms.enumeration.RecordStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "预约挂号的订单信息")
public class Record {

    private String recordId;

    @ApiModelProperty(value = "预约订单状态 0：未付款，1：待就诊，2：已就诊，3：已失效，4：已完成，5：已删除")
    private RecordStatusEnum recordStatus;

    @Past
    private Date recordCreateTime;

    @ApiModelProperty(value = "预约就诊日期")
    private Date recordDate;


}
