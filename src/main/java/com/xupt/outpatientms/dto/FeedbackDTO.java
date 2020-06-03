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
@ApiModel(description = "用户评价所需信息")
public class FeedbackDTO {

    @ApiModelProperty(value = "评分", required = true)
    @NotNull(message = "评分字段不可为空，1~5星")
    @Min(value = 1, message = "评分最少1星")
    @Max(value = 5, message = "评分最多5星")
    private Integer feedbackRating;

    @ApiModelProperty(value = "评论，最多250字，不评论则默认保存为\"未评论\"")
    @Size(max = 250, message = "请检查评论长度")
    private String feedbackInfo;

    @ApiModelProperty(value = "recordId", required = true)
    private Integer recordId;

    @ApiModelProperty(value = "userId，无需提交")
    private Integer userId;

}
