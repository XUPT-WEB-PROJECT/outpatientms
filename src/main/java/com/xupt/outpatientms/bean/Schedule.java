package com.xupt.outpatientms.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "排班信息")
public class Schedule {

    @ApiModelProperty(value = "科室名", required = true)
    @NotNull
    private String departmentName;

    @ApiModelProperty(value = "doctorId", required = true)
    @NotNull
    private String doctorId;

    @ApiModelProperty(value = "医生姓名", required = true)
    @NotNull
    @Size(min = 2, max = 12, message = "医生姓名长度为2~12字")
    private String doctorName;

    @ApiModelProperty(value = "工作日", required = true)
    @NotNull
    @Min(value = 1, message = "工作日在1~7之间，表示周一到周日")
    @Max(value = 7, message = "工作日在1~7之间，表示周一到周日")
    private Integer workday;

}
