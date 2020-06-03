package com.xupt.outpatientms.dto;

import com.xupt.outpatientms.enumeration.TimeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "创建挂号单所需数据")
public class RecordCreateDTO {

    @ApiModelProperty(value = "预约就诊日期", required = true)
    @NotNull(message = "就诊日期不能为空！")
    private String recordDate;

    @ApiModelProperty(value = "预约就诊时段(0：上午，1：下午)", required = true)
    @NotNull(message = "就诊时段不能为空！")
    private TimeEnum recordTime;

    @ApiModelProperty(value = "就诊医生ID", required = true)
    @NotNull(message = "doctorId不能为空")
    private String doctorId;

    @ApiModelProperty(value = "患者ID")
    private String userId;

    @ApiModelProperty(value = "用于校验日期与医生")
    private Integer workday;

}
