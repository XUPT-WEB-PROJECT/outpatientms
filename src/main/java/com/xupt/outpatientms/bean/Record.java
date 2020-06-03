package com.xupt.outpatientms.bean;

import com.xupt.outpatientms.enumeration.GenderEnum;
import com.xupt.outpatientms.enumeration.RecordStatusEnum;
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
@ApiModel(description = "预约挂号的订单信息")
public class Record {

    @ApiModelProperty(value = "订单id")
    private String recordId;

    @ApiModelProperty(value = "预约订单状态 0：未付款，1：待就诊，2：已就诊，3：已失效，4：已完成，5：已删除")
    private RecordStatusEnum recordStatus;

    @ApiModelProperty(value = "订单创建时间")
    private Date recordCreateTime;

    @ApiModelProperty(value = "预约就诊日期")
    @NotNull(message = "就诊日期不能为空！")
    private String recordDate;

    @ApiModelProperty(value = "预约就诊时段(0：上午，1：下午)")
    @NotNull(message = "就诊时段不能为空！")
    private TimeEnum recordTime;

    @ApiModelProperty(value = "就诊当日序号")
    private Integer recordOrder;

    @ApiModelProperty(value = "挂号费，整数，5~50")
    @Min(5)
    @Max(50)
    private int recordFare;

    @NotNull(message = "科室名称不能为空")
    @ApiModelProperty(value = "所属科室名称")
    private String departmentName;

    @ApiModelProperty(value = "所属科室位置")
    private String departmentPos;

    @ApiModelProperty(value = "就诊医生ID")
    private String doctorId;

    @ApiModelProperty(value = "就诊医生姓名")
    private String doctorName;

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

    @ApiModelProperty(value = "诊断结果")
    @Size(min = 0, max = 250, message = "诊断结果250字以内")
    private String medicalRecord;

}
