package com.xupt.outpatientms.bean;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "科室信息")
public class Department {

    private String departmentName;

    private String departmentPos;

}
