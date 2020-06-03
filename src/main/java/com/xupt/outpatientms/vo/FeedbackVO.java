package com.xupt.outpatientms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户评价数据结构")
public class FeedbackVO {

    @ApiModelProperty(value = "用户姓名")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String userName;

    @ApiModelProperty(value = "doctorId")
    private Integer doctorId;

    @ApiModelProperty(value = "医生姓名")
    @Size(min = 2, max = 12, message = "请检查名字长度")
    private String doctorName;

    @ApiModelProperty(value = "评价时间")
    private Date feedbackTime;

    @ApiModelProperty(value = "评分")
    @NotNull(message = "评分字段不可为空，1~5星")
    @Min(value = 1, message = "评分最少1星")
    @Max(value = 5, message = "评分最多5星")
    private Integer feedbackRating;

    @ApiModelProperty(value = "评论，最多250字，不评论则保存为\"无评论\"")
    @Size(max = 250, message = "请检查评论长度")
    private String feedbackInfo;

    @ApiModelProperty(value = "recordId")
    private Integer recordId;

}

