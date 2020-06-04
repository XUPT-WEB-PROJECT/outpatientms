package com.xupt.outpatientms.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "科室信息")
public class Department {

    @ApiModelProperty(name = "科室名称")
    @NotNull(message = "科室名称不能为空")
    private String departmentName;

    @ApiModelProperty(name = "科室位置不能为空")
    @NotNull(message = "科室位置不能为空")
    private String departmentPos;

}
