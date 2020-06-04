package com.xupt.outpatientms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChoseDoctorDTO {

    @ApiModelProperty("科室名称")
    @NotNull(message = "科室名不能为空")
    private String departmentName;

    @ApiModelProperty("预约日期")
    @NotNull(message = "预约日期不能为空")
    private String date;

}
