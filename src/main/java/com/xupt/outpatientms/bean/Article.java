package com.xupt.outpatientms.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "文章信息")
public class Article {

    @ApiModelProperty(name = "articleId")
    private Integer articleId;

    @ApiModelProperty(name = "文章标题")
    @NotNull(message = "标题不为空")
    private String articleTitle;

    @ApiModelProperty(name = "文章内容")
    @NotNull(message = "作者不为空")
    private String articleAuthor;

    @ApiModelProperty(name = "文章上传时间")
    private Date articleCreateTime;

    @ApiModelProperty(name = "文章简介")
    @Size(max = 150, message = "文章简介150字以内")
    private String articleAbbr;

    @ApiModelProperty(name = "文章标题图片url")
    private String articlePic;

    @ApiModelProperty(name = "文章内容")
    @Size(max = 5000, message = "文章长度在5000个字符以内")
    @NotNull(message = "文章内容不为空")
    private String articleText;

}
