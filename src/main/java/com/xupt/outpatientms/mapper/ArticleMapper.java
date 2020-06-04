package com.xupt.outpatientms.mapper;

import com.xupt.outpatientms.bean.Article;
import com.xupt.outpatientms.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {

    int addArticle(Article article);

    int delArticle(Integer articleId);

    List<ArticleVO> listArticle();

    Article getArticle(Integer articleId);

}
