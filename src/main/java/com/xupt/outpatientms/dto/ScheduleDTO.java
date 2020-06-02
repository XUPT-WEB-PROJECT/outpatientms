package com.xupt.outpatientms.dto;

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
public class ScheduleDTO {

    @ApiModelProperty(value = "departmentName", required = true)
    @NotNull(message = "科室名不能为空")
    private String departmentName;

    @ApiModelProperty(value = "doctorId", required = true)
    @NotNull(message = "doctorId不能为空")
    private String doctorId;

    @ApiModelProperty(value = "工作日", required = true)
    @NotNull(message = "工作日不能为空")
    @Min(value = 1, message = "工作日在1~7之间，表示周一到周日")
    @Max(value = 7, message = "工作日在1~7之间，表示周一到周日")
    private Integer workday;

}
