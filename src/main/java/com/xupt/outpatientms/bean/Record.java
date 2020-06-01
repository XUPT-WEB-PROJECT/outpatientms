package com.xupt.outpatientms.bean;

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
@ApiModel(description = "预约挂号的订单信息")
public class Record {

    private String recordId;

    @ApiModelProperty(value = "预约订单状态 0：未付款，1：待就诊，2：已就诊，3：已失效，4：已完成，5：已删除")
    private RecordStatusEnum recordStatus;

    @ApiModelProperty(value = "订单创建时间")
    @Past
    private Date recordCreateTime;

    @NotNull(message = "就诊日期不能为空！")
    @ApiModelProperty(value = "预约就诊日期")
    @FutureOrPresent
    private Date recordDate;

    @ApiModelProperty(value = "就诊当日序号")
    private Date recordOrder;

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

    @ApiModelProperty(value = "患者ID")
    private String userId;

    @NotNull(message = "用户姓名不能为空")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String userName;

    private GenderEnum userGender;

    @Min(value = 0, message = "年龄在0~120之间")
    @Max(value = 120, message = "年龄在0~120之间")
    private Integer userAge;

    @Size(min = 0, max = 250, message = "诊断状况250字以内")
    private String medicalRecord;

}
