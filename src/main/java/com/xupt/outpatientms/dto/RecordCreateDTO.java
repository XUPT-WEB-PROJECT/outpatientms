package com.xupt.outpatientms.dto;

import com.xupt.outpatientms.enumeration.GenderEnum;
import com.xupt.outpatientms.enumeration.RecordStatusEnum;
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
    @FutureOrPresent
    private Date recordDate;

    @NotNull(message = "doctorId不能为空")
    @ApiModelProperty(value = "就诊医生ID", required = true)
    private String doctorId;

    @ApiModelProperty(value = "患者ID")
    private String userId;

    @ApiModelProperty(value = "患者姓名")
    @NotNull(message = "用户姓名不能为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String userName;

    @ApiModelProperty(value = "患者性别")
    private GenderEnum userGender;

    @ApiModelProperty(value = "患者性别")
    @Min(value = 0, message = "年龄在0~120之间")
    @Max(value = 120, message = "年龄在0~120之间")
    private Integer userAge;

}
