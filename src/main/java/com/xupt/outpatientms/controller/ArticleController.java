package com.xupt.outpatientms.controller;

import com.xupt.outpatientms.bean.Article;
import com.xupt.outpatientms.enumeration.ErrCodeEnum;
import com.xupt.outpatientms.service.ArticleService;
import com.xupt.outpatientms.util.ResponseBuilder;
import com.xupt.outpatientms.vo.ArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "医疗文章相关接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value="增加文章",
            notes = "接口调用成功errCode=0，信息返回在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "article", required = true, dataType = "application/json",
            value = "创建文章所需信息，其中articleTitle, articleAuthor, articleText为必选项\narticleText最长5000字，articleAbbr150字以内" +
                    "articleAbbr, articlePic为可选项(有默认值)\n" +
                    "eg:\n"+
                    "{\n" +
                    "\t\"articleTitle\": \"论xx治疗xxx的药物机理\",\n" +
                    "\t\"articleAuthor\": \"kafm\",\n" +
                    "\t\"articleText\": \"详细内容详细内容详细内容详细内容详细内容详细内容\",\n" +
                    "\t\"articleAbbr\": \"请点击查看文章详细内容\",\n" +
                    "\t\"articlePic\": \"http://qax0m7o02.bkt.clouddn.com/医疗文章默认图片.png\"\n" +
                    "}\n"
            )
    @RequestMapping(value = "addArticle", method = RequestMethod.POST)
    public ResponseBuilder<Article> addArticle(@Validated @RequestBody Article article, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG,bindingResult.getFieldError().getDefaultMessage());
        }
        article.setArticleCreateTime(new Date());
        if(article.getArticleAbbr() == null) article.setArticleAbbr("请点击查看文章详细内容");
        if(article.getArticlePic() == null) article.setArticlePic("http://qax0m7o02.bkt.clouddn.com/%E5%8C%BB%E7%96%97%E6%96%87%E7%AB%A0%E9%BB%98%E8%AE%A4%E5%9B%BE%E7%89%87.png");
        if(articleService.addArticle(article) == 0)
            return new ResponseBuilder<>(ErrCodeEnum.ERR_FAILED, "添加失败");
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, article);
    }

    @ApiOperation(value="删除文章",
            notes = "接口调用成功errCode=0，所删除的id返回在data字段，否则错误信息返回至errMsg\n")
    @ApiImplicitParam(name = "articleId", required = true, paramType = "query",
            value = "articleId")
    @RequestMapping(value = "delArticle", method = RequestMethod.GET)
    public ResponseBuilder<Integer> delArticle(@RequestParam Integer articleId){
        if(articleService.delArticle(articleId) != 1){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG);
        }
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, articleId);
    }

    @ApiOperation(value="查询所有医疗文章缩略信息",
            notes = "接口调用成功errCode=0，信息返回在data字段，否则错误信息返回至errMsg\n")
    @RequestMapping(value = "listArticle", method = RequestMethod.GET)
    public ResponseBuilder<List<ArticleVO>> listArticle(){
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", articleService.listArticle());
    }

    @ApiOperation(value="按articleId获得文章全部信息",
            notes = "接口调用成功errCode=0，信息返回在data字段，否则错误信息返回至errMsg\n")
    @RequestMapping(value = "getArticle", method = RequestMethod.GET)
    public ResponseBuilder<Article> getArticle(Integer articleId){
        Article article = articleService.getArticle(articleId);
        if(article == null){
            return new ResponseBuilder<>(ErrCodeEnum.ERR_ARG);
        }
        return new ResponseBuilder<>(ErrCodeEnum.ERR_SUCCESS, "查询成功", article);
    }

}
